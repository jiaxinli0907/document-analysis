package de.dbvis.structured;

import java.util.Arrays;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class NGramImpl implements NGram {

    protected int n;
    protected Word[] words;

    public NGramImpl(Word... words) {
        this.n = words.length;
        this.words = words;
    }

    @Override
    public int getN() {
        return 0;
    }

    @Override
    public Word[] getWords() {
        return new Word[0];
    }

    public int hashCode() {
        return Arrays.hashCode(words);
    }

    public boolean equals(Object o) {
        return o != null
                && o instanceof NGram
                && this.getN() == ((NGram) o).getN()
                && Arrays.equals(this.getWords(), ((NGram) o).getWords());
    }

    public String toString() {
        return Arrays.toString(words);
    }
}
