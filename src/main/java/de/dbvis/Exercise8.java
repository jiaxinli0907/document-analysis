package de.dbvis;

import de.dbvis.distance.LevenshteinDistance;
import de.dbvis.distance.StringDistance;
import de.dbvis.wordnet.AbstractWordnet;
import de.dbvis.wordnet.Wordnet;
import java.io.IOException;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

public class Exercise8 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exercise8.class);
    public static void main(String[] args) throws ParseException, URISyntaxException, IOException {

        /*
         * TASK 1
         * Levenshtein Distance
         *
         * a) Extend the AbstractLevenshteinDistance class and implement the method distance().
         * b) Use your implemented method to calculate the distance between the given strings. What is the Levenshtein distance between:
         *   1. „Tuesday“, „Thursday“
         *   2. „advantage“, „disadvantage“
         *   3. „advantage“, „adventure“?
         */

        final String string1A = "Tuesday";
        final String string1B = "Thursday";

        final String string2A = "advantage";
        final String string2B = "disadvantage";

        final String string3A = "advantage";
        final String string3B = "adventure";

        StringDistance stringDistance = new LevenshteinDistance();
        LOGGER.info("The distance between {} and {} is {}", string1A, string1B, stringDistance.distance(string1A, string1B));
        LOGGER.info("The distance between {} and {} is {}", string2A, string2B, stringDistance.distance(string2A, string2B));
        LOGGER.info("The distance between {} and {} is {}", string3A, string3B, stringDistance.distance(string3A, string3B));


        /*
         * TASK 2
         * Create class Wordnet and extend it from the AbstractWordnetClass.
         * Implement method findCommonHypernyms() to find all common hypernyms from the given input strings:
         *  1) man, woman
         *  2) cat, dog
         *  3) gun, weapon
         * To do so, implement the getHypernyms() method which retrieves all hypernyms from one word.
         * Use the provided method findAllHypernymsRecursive() for that.
         * Include the hypernyms in your PDF file.
         */

        AbstractWordnet wordnet = new Wordnet();
        LOGGER.debug("{}", wordnet.findCommonHypernyms("woman", "man"));
        LOGGER.debug("{}", wordnet.findCommonHypernyms("cat", "dog"));
        LOGGER.debug("{}", wordnet.findCommonHypernyms("gun", "weapon"));


        /*
         * TASK 3
         * Comparison of Text Similarity Methods (Theory):
         * a)	Which type of similarity can be estimated using Levenshtein Distance? Which type of similarity can be estimated using Wordnet ontology?
         * b)	What are the advantages of these methods?
         * c)	What are the disadvantages/limitations of these methods?
         * d)	What are their possible applications? Describe a concrete real world application example for each of these methods.
         */
    }
}
