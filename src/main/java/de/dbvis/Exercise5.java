package de.dbvis;

import de.dbvis.parser.*;
import de.dbvis.structured.Sentence;
import de.dbvis.structured.SentenceImpl;

import java.text.ParseException;
import java.util.Set;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class Exercise5 {

    public static void main(String[] args) throws ParseException {
        final Grammar grammar = Grammar.build()
                .addRule("S", "NP", "VP")
                .addRule("NP", "DT", "Nom")
                .addRule("NP", "DT", "N")
                .addRule("NP", "NP", "PP")
                .addRule("Nom", "Adj", "Nom")
                .addRule("Nom", "Adj", "N")
                .addRule("VP", "V", "Adj")
                .addRule("VP", "V", "NP")
                .addRule("VP", "V", "S")
                .addRule("PP", "P", "NP")
                .addRule("DT", "a")
                .addRule("DT", "the")
                .addRule("N", "bear")
                .addRule("N", "squirrel")
                .addRule("N", "dog")
                .addRule("N", "park")
                .addRule("N", "man")
                .addRule("Adj", "angry")
                .addRule("Adj", "frightened")
                .addRule("Adj", "little")
                .addRule("P", "in")
                .addRule("V", "saw")
                .addRule("V", "chased");

        final Sentence sentenceA = SentenceImpl.createFromTokens("the", "angry", "bear", "chased", "the", "frightened", "little", "squirrel");
        final Sentence sentenceB = SentenceImpl.createFromTokens("the", "dog", "saw", "a", "man", "in", "the", "park");

        /*
         * TASK 1: Do it manually on a sheet of paper!
         */

        /*
         * TASK 2: Extend from the AbstractShiftReduceParser and implement the methods.
         * Pay attention to the comments (JavaDoc) at the methods.
         */
         Parser shiftReduceParser = new ShiftReduceParser(grammar);

         Set<TreeNode> treesA1 = shiftReduceParser.parse(sentenceA);
         printTrees(treesA1);

         Set<TreeNode> treesB1 = shiftReduceParser.parse(sentenceB);
         printTrees(treesB1);

        /*
         * TASK 3: Extend from the AbstractCYKParser and implement the methods.
         * Pay attention to the comments (JavaDoc) at the methods.
         */
//        Parser cykParser = new CYKParser(grammar);

//        Set<TreeNode> treesA2 = cykParser.parse(sentenceA);
//        printTrees(treesA2);

//        Set<TreeNode> treesB2 = cykParser.parse(sentenceB);
//        printTrees(treesB2);

    }

    private static void printTrees(Set<TreeNode> trees) {
        trees.forEach(TreeNode::print);
    }
}
