package de.dbvis.tagger;

import de.dbvis.structured.Sentence;
import de.dbvis.utils.BrownCorpusHelper;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Created by Wolfgang Jentner (University of Konstanz) [wolfgang.jentner@uni.kn]..
 */
public class StanfordTagger implements POSTagger {

    private MaxentTagger tagger;

    @Override
    public void trainTagger() {
        this.tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
    }

    @Override
    public Sentence assignTags(Sentence sentence) {
        String taggedString = tagger.tagTokenizedString(sentence.getTokenString());
        return BrownCorpusHelper.parseLineToSentence(taggedString, "_");
    }
}
