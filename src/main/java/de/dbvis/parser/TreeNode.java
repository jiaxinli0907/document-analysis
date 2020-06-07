package de.dbvis.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Wolfgang Jentner (University of Konstanz) [wolfgang.jentner@uni.kn] on 11/23/2017.
 */
public class TreeNode {
    private final String name;
    private final List<TreeNode> children = new ArrayList<>();

    public TreeNode(String root) {
        assert root != null;
        this.name = root;
    }

    public TreeNode(String root, TreeNode... child) {
        this(root);
        assert child != null;
        this.children.addAll(Arrays.asList(child));
    }

    public TreeNode(String root, List<TreeNode> children) {
        this(root);
        assert children != null;
        this.children.addAll(children);
    }

    public String getName() {
        return name;
    }

    public List<TreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Taken from https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
     */
    public void print() {
        print("", true);
    }

    /**
     * Taken from https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
     */
    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + name);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .print(prefix + (isTail ?"    " : "│   "), true);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode treeNode = (TreeNode) o;

        return getName().equals(treeNode.getName()) && getChildren().equals(treeNode.getChildren());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getChildren().hashCode();
        return result;
    }
}
