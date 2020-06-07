package de.dbvis.parser;

import de.dbvis.structured.Sentence;

import java.text.ParseException;
import java.util.Set;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public interface Parser {

    /**
     * A parser parses a sentence and returns a set of possible trees or throws an exception
     * if a sentence is not parseable.
     * @param sentence the input sentence
     * @return a set of trees referenced by the root-TreeNode
     * @throws ParseException if the sentence is not parseable
     */
    Set<TreeNode> parse(Sentence sentence) throws ParseException;
}
