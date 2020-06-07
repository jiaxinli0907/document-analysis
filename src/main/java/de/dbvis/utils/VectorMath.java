package de.dbvis.utils;

import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public interface VectorMath {

    /**
     * Calculates the euclideanNorm for an input vector
     * @param v the input vector
     * @return the euclideanNorm
     */
    double euclideanNorm(RealVector v);

    /**
     * Calculates the dot product for two given vectors.
     * @param v1 vector 1
     * @param v2 vector 2
     * @return the dot product of the two vectors
     */
    double dotProduct(RealVector v1, RealVector v2);


    /**
     * Calculates the cosine similarity of two given vectors
     * @param v1 vector 1
     * @param v2 vector 2
     * @return the cosine similarity of the two vectors
     */
    double cosineSimilarity(RealVector v1, RealVector v2);
}
