package de.dbvis.tagger;

import de.dbvis.structured.Sentence;
import de.dbvis.structured.Word;
import de.dbvis.utils.Resources;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class OpenNLPTagger implements POSTagger {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenNLPTagger.class);

    private POSTaggerME tagger;
    private POSModel model;

    @Override
    public void trainTagger() {
        try {
            Path modelFile = Resources.getResource("models/en-pos-maxent.bin");
            InputStream inputStream = new FileInputStream(modelFile.toFile());
            model = new POSModel(inputStream);
            tagger = new POSTaggerME(model);
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Something went wrong", e);
        }
    }

    @Override
    public Sentence assignTags(Sentence sentence) {
        String[] sentenceArray = sentence.stream().map(Word::getToken).toArray(String[]::new);
        String[] taggedSentence = tagger.tag(sentenceArray);
        for(int i = 0; i < sentence.size(); i++) {
            sentence.get(i).setTag(taggedSentence[i]);
        }
        return sentence;
    }
}
