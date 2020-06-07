package de.dbvis.structured;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of a Sentence using the ArrayList.
 * Created by Wolfgang Jentner.
 */
public class SentenceImpl extends ArrayList<Word> implements Sentence {
    public static Sentence createFromTokens(String... tokens) {
        final Sentence sentence = new SentenceImpl();
        Arrays.stream(tokens)
        .map(WordImpl::new)
        .forEach(sentence::add);
        return sentence;
    }

    @Override
    public Sentence copy() {
        final Sentence sentenceCopy = new SentenceImpl();
        this.forEach(word -> sentenceCopy.add(word.copy()));
        return sentenceCopy;
    }
}
