package de.dbvis.structured;


/**
 * Created by Wolfgang Jentner.
 */
public class WordImpl implements Word {

    private String token;
    private String stem;
    private String tag;

    public WordImpl(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Word copy() {
        Word word = new WordImpl(this.token);
        word.setTag(tag);
        word.setStem(stem);
        return word;
    }

    @Override
    public String toString() {
        return token+"/"+tag;
    }
}
