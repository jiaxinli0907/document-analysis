package de.dbvis.sentiment;

import de.dbvis.structured.Sentence;

import java.util.Collection;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public interface SentimentAnalyzer {

    /**
     * Calculates the sentiment of one given sentence.
     * IMPORTANT: The range has to be normalized --> 0 negative, 0.5 neutral, 1 positive
     * @param sentence - The input sentence
     * @return a score ranging from 0 - 1.
     */
    double sentiment(Sentence sentence);

    /**
     * Calculates the average sentiment for a given document (collection of sentences).
     * @param sentences a collection of sentences
     * @return the average sentiment of a document
     */
    double sentiment(Collection<Sentence> sentences);
}
