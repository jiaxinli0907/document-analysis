package de.dbvis.hmm;

import io.github.adrianulbona.hmm.State;

/**
 * Created by Wolfgang Jentner.
 */
public class HmmPosTag implements State {
    public static final HmmPosTag STOP = new HmmPosTag("$STOP$");
    private final String tag;

    public HmmPosTag(String tag) {
        assert tag != null;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HmmPosTag hmmPosTag = (HmmPosTag) o;

        return getTag().equals(hmmPosTag.getTag());
    }

    @Override
    public int hashCode() {
        return getTag().hashCode();
    }

    @Override
    public String toString() {
        return "HmmPosTag{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
