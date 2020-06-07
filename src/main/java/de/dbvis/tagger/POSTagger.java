package de.dbvis.tagger;

import de.dbvis.structured.Sentence;

/**
 * Created by Wolfgang Jentner.
 */
public interface POSTagger {

    /**
     * Reads the corpus in data/brown and learns the token/tag combinations
     */
    void trainTagger();

    /**
     * Similar to assignTag, will handle a whole sentence and return it.
     * @param sentence The sentence consisting of words
     * @return Returns the same sentence.
     */
    Sentence assignTags(Sentence sentence);
}
