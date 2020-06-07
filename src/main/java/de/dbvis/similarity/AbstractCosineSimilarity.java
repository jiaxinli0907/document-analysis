package de.dbvis.similarity;

import java.util.Map;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public abstract class AbstractCosineSimilarity implements DocumentSimilarity {

    protected Map<String, Map<String, Integer>> dataStructure;

    public AbstractCosineSimilarity(Map<String, Map<String, Integer>> dataStructure) {
        this.dataStructure = dataStructure;
    }
}
