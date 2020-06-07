package de.dbvis;

import de.dbvis.parser.CYKParser;
import de.dbvis.parser.Grammar;
import de.dbvis.parser.Parser;
import de.dbvis.parser.TreeNode;
import de.dbvis.parser.earley.EarleyParser;
import de.dbvis.structured.Sentence;
import de.dbvis.structured.SentenceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Set;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class Exercise6 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exercise6.class);
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


        /*
         * TASK 1:
         * CYK parser
         * Extend from the AbstractCYKParser and implement the methods.
         * Pay attention to the comments (JavaDoc) at the methods.
         */

        final Sentence sentenceA = SentenceImpl.createFromTokens("the", "angry", "bear", "chased", "the", "frightened", "little", "squirrel");
        final Sentence sentenceB = SentenceImpl.createFromTokens("the", "dog", "saw", "a", "man", "in", "the", "park");

        Parser cykParser = new CYKParser(grammar);

        Set<TreeNode> treesA2 = cykParser.parse(sentenceA);
        printTrees(treesA2);

        Set<TreeNode> treesB2 = cykParser.parse(sentenceB);
        printTrees(treesB2);


        /*
         * TASK 2:
         * Earley parser
         *
         * Extend from the class AbstractEarleyParser and implement the missing methods.
         * Pay attention to the comments (JavaDoc) at the methods.
         */

        final Sentence sentence = SentenceImpl.createFromTokens("the", "child", "ate", "the", "cake", "with", "the", "fork");

      Parser p = new EarleyParser();
      printTrees(p.parse(sentence));


        /*
         * TASK 3:
         * Earley parser: parsing by hand
         * Do it manually on a sheet of paper!
         *
         * Grammar:
         * S-> NP VP
         * NP -> NP PP | N
         * VP -> Verb NP | VP PP
         * PP -> Prep NP
         * Noun -> Johannes | Rita | Wolfgang
         * Verb -> visited | called
         * Prep -> from

         *   Sentence:
         *   Wolfgang visited Johannes.
         */

    }

    private static void printTrees(Set<TreeNode> trees) {
        trees.forEach(TreeNode::print);
    }
}
