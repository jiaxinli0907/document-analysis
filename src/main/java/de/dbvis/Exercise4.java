package de.dbvis;

import de.dbvis.tagger.HmmPosTagger;
import de.dbvis.tagger.NGramTag;
import de.dbvis.tagger.POSTagger;
import de.dbvis.tagger.StanfordTagger;
import de.dbvis.utils.LineChartFactory;
import de.dbvis.utils.POSTaggerScorer;
import de.dbvis.utils.Resources;
import de.dbvis.utils.tagset.BrownCorpusTagSet;
import de.dbvis.utils.tagset.PennTreebankTagSet;
import de.dbvis.utils.tagset.Tagset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Wolfgang Jentner.
 */
public class Exercise4 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exercise4.class);

    private static final List<Double> xValues = new ArrayList<>();
    private static final TreeMap<String, List<Double>> precisionValues = new TreeMap<>();
    private static final TreeMap<String, List<Double>> runtimeValues = new TreeMap<>();

    private static final POSTaggerScorer scorer = new POSTaggerScorer();

    public static void main(String[] args) throws URISyntaxException, IOException {
        /*
         * Task 1
         * Implement the missing part in the de.dbvis.tagger.HmmPosTagger
         */
        List<File> corpus = new ArrayList<>(Resources.getResourceFiles("data/brown"));


        for(int i = 1; i < corpus.size(); i++) {
            //the subset of the corpus
            final List<File> subsetCorpus = corpus.subList(0, i);

            /*
             * Task 2
             * Add your other taggers and the Stanford & OpenNLP tagger here and add your score and the runtime to
             * precisionValues and runtimeValues (just see how I did it with the HMMTagger)
             */
            //initialize your tagger with a subset of the corpus
            HmmPosTagger tagger = new HmmPosTagger(subsetCorpus);
            measureTagger(i, tagger, BrownCorpusTagSet.instance, null);
//            measureTagger(i, stanford, PennTreebankTagSet.instance, null);
          
            NGramTag unigramTagger = new NGramTag(1,subsetCorpus);
            NGramTag bigramTagger = new NGramTag(2,subsetCorpus);
            NGramTag trigramTagger = new NGramTag(3,subsetCorpus);

            measureTagger(i, unigramTagger, BrownCorpusTagSet.instance, "n=1");
            measureTagger(i, bigramTagger, BrownCorpusTagSet.instance, "n=2");
            measureTagger(i, trigramTagger, BrownCorpusTagSet.instance, "n=3");

            //add the x-values (NOTE: YOU DON'T HAVE TO DO THIS AGAIN)
            if(xValues.size() <= i) {
                xValues.add(i-1, (double) tagger.getNumberOfTrainedWords());
            }


            LOGGER.info("{} of {}", i, corpus.size());
        }


        //this will create two nice little charts
        LineChartFactory.createTaggerPerformance(xValues, precisionValues);
        LineChartFactory.createTaggerRuntime(xValues, runtimeValues);
    }

    private static void measureTagger(int corpusSize, POSTagger tagger, Tagset tagsetMapper, String suffix) throws IOException, URISyntaxException {
        //the name of your tagger
        final String taggerName = tagger.getClass().getName() + (suffix == null ? "" : "-"+suffix);

        //store the current time in the runtimeValue
        runtimeValues.putIfAbsent(taggerName, new ArrayList<>());
        runtimeValues.get(taggerName).add(corpusSize-1, (double) System.currentTimeMillis());

        //start training your tagger
        tagger.trainTagger();

        //add the score from the scorer
        precisionValues.putIfAbsent(taggerName, new ArrayList<>());

        double score = tagsetMapper == null ? scorer.score(tagger) : scorer.scoreMapped(tagger, tagsetMapper);
        precisionValues.get(taggerName).add(corpusSize-1, score);

        //calculate the difference in time what we have stored in the array
        double diff = System.currentTimeMillis() - runtimeValues.get(taggerName).get(corpusSize-1);
        runtimeValues.get(taggerName).set(corpusSize-1, diff); //replace it to store the runtime only
    }
}
