package de.dbvis.hmm;

import io.github.adrianulbona.hmm.Observation;

/**
 * Created by Wolfgang Jentner.
 */
public class HmmWord implements Observation {
    public static final HmmWord STOP = new HmmWord("$STOP$");
    private final String word;

    public HmmWord(String word) {
        assert word != null;
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HmmWord hmmWord = (HmmWord) o;

        return getWord().equals(hmmWord.getWord());
    }

    @Override
    public int hashCode() {
        return getWord().hashCode();
    }

    @Override
    public String toString() {
        return "HmmWord{" +
                "word='" + word + '\'' +
                '}';
    }
}
