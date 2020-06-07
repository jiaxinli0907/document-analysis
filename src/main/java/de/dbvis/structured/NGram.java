package de.dbvis.structured;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public interface NGram {
    /**
     * @return the number of Words in the NGram
     */
    int getN();

    /**
     * @return the words in the ngram
     */
    Word[] getWords();
}
