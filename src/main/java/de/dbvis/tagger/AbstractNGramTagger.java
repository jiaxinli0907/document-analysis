package de.dbvis.tagger;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public abstract class AbstractNGramTagger implements POSTagger {

    /*
     * the N in NGram
     */
    protected int n;

    /**
     * Constructs a new NGramTagger with variable n
     * @param n the N in NGram
     */
    public AbstractNGramTagger(int n) {
        this.n = n;
    }
}
