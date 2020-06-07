package de.dbvis.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class Grammar {
    private static final Logger LOGGER = LoggerFactory.getLogger(Grammar.class);

    private List<TreeNode> rules = new ArrayList<>();

    private Grammar() {}

    public static Grammar build() {
        return new Grammar();
    }

    public Grammar addRule(String root, String... children) {
        assert root != null;
        assert children.length > 0;

        if(children.length > 2) {
            throw new IllegalArgumentException("More than two children are currently not supported");
        }

        List<TreeNode> treeNodeChildren = Arrays.stream(children)
                .map(childStr -> new TreeNode(childStr, new ArrayList<>()))
                .collect(Collectors.toList());

        TreeNode node = new TreeNode(root, treeNodeChildren);

        rules.add(node);
        return this;
    }

    public int size() {
        return rules.size();
    }

    public TreeNode getRoot() {
        return rules.get(0);
    }

    public Set<TreeNode> findRulesByChildren(final TreeNode... children) {
        return findRulesByChildren(Arrays.asList(children));
    }

    public Set<TreeNode> findRulesByChildren(final List<TreeNode> children) {
        final List<String> compare = children.stream().map(TreeNode::getName).collect(Collectors.toList());

        return rules.stream()
                .filter(treeNode -> {
                    List<String> ownRuleCompare = treeNode.getChildren().stream().map(TreeNode::getName).collect(Collectors.toList());
                    return compare.equals(ownRuleCompare);
                })
                .map(treeNode -> new TreeNode(treeNode.getName(), children)) //either return a new node with the input children
                .collect(Collectors.toSet());
    }

    public TreeNode findRuleByChildren(final List<TreeNode> children) {
        final List<String> compare = children.stream().map(TreeNode::getName).collect(Collectors.toList());


        return rules.stream()
                .filter(treeNode -> {
                    List<String> ownRuleCompare = treeNode.getChildren().stream().map(TreeNode::getName).collect(Collectors.toList());
                    return compare.equals(ownRuleCompare);
                })
                .findFirst()
                .map(treeNode -> new TreeNode(treeNode.getName(), children)) //either return a new node with the input children
                .orElse(null); //or null if it cannot be found
    }
}
