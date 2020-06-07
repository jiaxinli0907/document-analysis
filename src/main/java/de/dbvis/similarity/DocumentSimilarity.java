package de.dbvis.similarity;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public interface DocumentSimilarity {

    /**
     * Calculates the similarity of two documents based on the implemented similarity measure.
     * @param doc1 a string representing doc1
     * @param doc2 a string representing doc2
     * @return a similarity measure
     */
    double similarity(String doc1, String doc2);
}
