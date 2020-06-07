package de.dbvis.utils;

import de.dbvis.structured.Sentence;
import de.dbvis.structured.SentenceImpl;
import de.dbvis.structured.WordImpl;
import de.dbvis.tagger.POSTagger;
import de.dbvis.utils.tagset.BrownCorpusTagSet;
import de.dbvis.utils.tagset.StandardTag;
import de.dbvis.utils.tagset.Tagset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.List;

/**
 * This class checks a tagger and gives it a score from 0 - 100%.
 * Created by Wolfgang Jentner on 11/10/2017.
 */
public class POSTaggerScorer {
    private static final Logger LOGGER = LoggerFactory.getLogger(POSTaggerScorer.class);

    private static List<Sentence> cachedCheckTags;

    /**
     * This method scores a given tagger
     * @param tagger the tagger to score
     * @return the score (0-1)
     * @throws URISyntaxException in case the URI to the resource file is messed up
     * @throws IOException in case the resource file cannot be read
     */
    public double score(POSTagger tagger) throws URISyntaxException, IOException {
        if(cachedCheckTags == null) {
            cachedCheckTags = BrownCorpusHelper.readFileFromCorpus("data/checktags");
        }

        final double[] correct = {0};
        final double[] incorrect = {0};

        cachedCheckTags
                .forEach(controlSentence -> {
                    //create a new sentence that the tagger will use:
                    final Sentence sentenceForTagger = createCopyWithoutTags(controlSentence);


                    tagger.assignTags(sentenceForTagger);

                    //LOGGER.debug(sentenceForTagger.toString());

                    if(controlSentence.size() != sentenceForTagger.size()) {
                        LOGGER.warn("Your tagged sentence differs from the original one:");
                        LOGGER.warn("Original: {}", controlSentence);
                        LOGGER.warn("Tagged:   {}", sentenceForTagger);
                        incorrect[0] += controlSentence.size();
                    } else {
                        for(int i = 0; i < controlSentence.size(); i++) {
                            String goldTag = controlSentence.get(i).getTag();
                            String realTag = sentenceForTagger.get(i).getTag();

                            if(goldTag == null) {
                                continue;
                            }
                            if(goldTag.equals(realTag)) {
                                correct[0]++;
                            } else {
                                incorrect[0]++;
                            }
                        }
                    }


                });

        double score = correct[0] / (correct[0]+incorrect[0]);

        LOGGER.info("Your tagger {} got a score of {}%",
                tagger.getClass(),
                Math.round(score * 10000.d) / 100.d);

        return score;
    }


    /**
     * This method scores a given tagger
     * @param tagger the tagger to score
     * @return the score (0-1)
     * @throws URISyntaxException in case the URI to the resource file is messed up
     * @throws IOException in case the resource file cannot be read
     */
    public double scoreMapped(POSTagger tagger, final Tagset tagsetMapper) throws URISyntaxException, IOException {
        final Tagset brownCorpusMapper = BrownCorpusTagSet.instance;

        if(cachedCheckTags == null) {
            cachedCheckTags = BrownCorpusHelper.readFileFromCorpus("data/checktags");
        }

        final double[] correct = {0};
        final double[] incorrect = {0};

        cachedCheckTags
                .forEach(controlSentence -> {
                    //create a new sentence that the tagger will use:
                    final Sentence sentenceForTagger = createCopyWithoutTags(controlSentence);


                    final Sentence taggedSentence = tagger.assignTags(sentenceForTagger);
                    //LOGGER.debug(sentenceForTagger.toString());

                    if(controlSentence.size() != taggedSentence.size()) {
                        LOGGER.warn("Your tagged sentence differs from the original one:");
                        LOGGER.warn("Original: {}", controlSentence);
                        LOGGER.warn("Tagged:   {}", taggedSentence);
                        incorrect[0] += controlSentence.size();
                    } else {
                        for(int i = 0; i < controlSentence.size(); i++) {
                            String goldTag = controlSentence.get(i).getTag();
                            String realTag = taggedSentence.get(i).getTag();

                            EnumSet<StandardTag> goldStandardTag = brownCorpusMapper.getTag(goldTag);
                            EnumSet<StandardTag> realStandardTag = tagsetMapper.getTag(realTag);

                            if(goldTag == null || goldStandardTag.isEmpty()) {
                                continue;
                            }
                            if(goldStandardTag.equals(realStandardTag)) {
                                correct[0]++;
                            } else {
                                incorrect[0]++;
                            }
                        }
                    }


                });

        double score = correct[0] / (correct[0]+incorrect[0]);

        LOGGER.info("Your tagger {} got a score of {}% (mapped with {})",
                tagger.getClass(),
                Math.round(score * 10000.d) / 100.d,
                tagsetMapper.getClass());

        return score;
    }

    public static Sentence createCopyWithoutTags(Sentence sentence) {
        final Sentence copy = new SentenceImpl();

        sentence
                .forEach(word -> copy.add(new WordImpl(word.getToken())));

        return copy;
    }
}
