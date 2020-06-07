package de.dbvis.structured;

/**
 * A word consists of a token, the stem, and the POS-tag.
 * Created by Wolfgang Jentner.
 */
public interface Word {

    String getToken();

    void setToken(String token);

    String getStem();

    void setStem(String stem);

    String getTag();

    void setTag(String tag);

    Word copy();
}
