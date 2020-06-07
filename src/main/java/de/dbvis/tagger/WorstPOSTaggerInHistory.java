package de.dbvis.tagger;


import de.dbvis.structured.Sentence;
import de.dbvis.structured.Word;
import de.dbvis.utils.BrownCorpusHelper;
import de.dbvis.utils.Resources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Wolfgang Jentner.
 */
public class WorstPOSTaggerInHistory extends AbstractNGramTagger {
    private static String[] tagset = new String[] {"np", "nn", "vb", "at"};

    /**
     * Constructs a new NGramTagger with variable n
     *
     * @param n the N in NGram
     */
    public WorstPOSTaggerInHistory(int n) {
        super(n);
    }

    @Override
    public void trainTagger() {
        //who needs training anyways?

        //hint: make use of the Resources.getResourceFiles in your implementation here.
        //use the following construct to train your tagger
        try {
            Resources.getResourceFiles("data/brown")
                    .forEach(file -> {
                        try {
                            List<Sentence> sentences = BrownCorpusHelper.readFileFromCorpus(file.toPath());
                            //we actually don't train anything here bc who cares
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Word assignTag(Word word) {
        if(!word.getToken().toLowerCase().equals(word.getToken())) {
            word.setTag("np");
        } else {
            word.setTag(tagset[(int) Math.round(Math.random()*(tagset.length-1))]);
        }
        return word;
    }

    @Override
    public Sentence assignTags(Sentence sentence) {
        sentence.forEach(this::assignTag);
        return sentence;
    }
}
