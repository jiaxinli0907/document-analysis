package de.dbvis.structured;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A sentence is an ordered list of words.
 * Right now it does not contain any other meta data.
 * Created by Wolfgang Jentner.
 */
public interface Sentence extends List<Word> {

    Sentence copy();

    /**
     * Returns a string of the tokens separated by a space.
     * @return a string containing the tokens
     */
    default String getTokenString() {
        return this.stream().map(Word::getToken).collect(Collectors.joining(" "));
    }
}
