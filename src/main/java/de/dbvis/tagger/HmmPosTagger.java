package de.dbvis.tagger;

import de.dbvis.hmm.*;
import de.dbvis.structured.Sentence;
import de.dbvis.utils.BrownCorpusHelper;
import io.github.adrianulbona.hmm.Emission;
import io.github.adrianulbona.hmm.Model;
import io.github.adrianulbona.hmm.Transition;
import io.github.adrianulbona.hmm.probability.ProbabilityCalculator;
import io.github.adrianulbona.hmm.solver.MostProbableStateSequenceFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Wolfgang Jentner.
 */
public class HmmPosTagger implements POSTagger {

    private static final Logger LOGGER = LoggerFactory.getLogger(HmmPosTagger.class);

    private int numberOfTrainedWords;

    /**
     * The corpus that the tagger will use to train itself
     */
    private List<File> corpus;

    /**
     * This is the hidden-markov model (library)
     */
    private Model<HmmPosTag, HmmWord> model;

    /**
     * Denotes all possible states reachable from a word (performance gain for
     * the algorithm)
     */
    private Map<HmmWord, List<HmmPosTag>> reachableStates = new HashMap<>();

    /**
     * Stores the emission probabilities
     */
    private Map<Emission<HmmPosTag, HmmWord>, Double> emissionProbabilities = new HashMap<>();

    /**
     * Stores the start probabilities
     */
    private Map<HmmPosTag, Double> startProbabilities = new HashMap<>();

    /**
     * Stores the transition probabilities
     */
    private Map<Transition<HmmPosTag>, Double> transitionProbabilities = new HashMap<>();

    /**
     * The HmmPosTagger is constructed with a given list of files which will be
     * used for training
     *
     * @param corpus - the files the tagger will be trained with
     */
    public HmmPosTagger(List<File> corpus) {
        this.corpus = corpus;
    }

    @Override
    public void trainTagger() {

        final Integer[] sumOfWords = {0};

        /*
         * We need those temporary structures to normalize our values
         */
        final Map<HmmPosTag, Double> emissionSum = new HashMap<>();
        final Map<HmmPosTag, Double> transitionSum = new HashMap<>();
        final Double[] totalSum = {0d}; //just a little hack since the variable must be final. We use an array of length 1 to satisfy this condition

        this.corpus.forEach(file -> {
            try {
                List<Sentence> sentences = BrownCorpusHelper.readFileFromCorpus(file.toPath());

                sentences.forEach(sentence -> {
                    sumOfWords[0] += sentence.size();

                    List<HmmPosTag> tags = sentence.stream().map(w -> new HmmPosTag(w.getTag())).collect(Collectors.toList());
                    List<HmmWord> tokens = sentence.stream().map(w -> new HmmWord(w.getToken())).collect(Collectors.toList());

                    tags.add(HmmPosTag.STOP);
                    tokens.add(HmmWord.STOP);
                    totalSum[0]++;

                    for (int i = 0; i < tags.size(); i++) {
                        HmmWord hmmWord = tokens.get(i);
                        HmmPosTag hmmPosTag = tags.get(i);
//
                        addReachableState(hmmWord, hmmPosTag);
                        incrementOne(emissionSum, hmmPosTag);
                        
                        incrementOne(emissionProbabilities, new Emission(hmmPosTag, hmmWord));
                        if (i == 0) {
                            incrementOne(transitionProbabilities, new Transition(hmmPosTag, hmmPosTag));
                            incrementOne(startProbabilities, hmmPosTag);
                            incrementOne(transitionSum, hmmPosTag);

                        } else {
                            incrementOne(transitionProbabilities, new Transition(tags.get(i - 1), hmmPosTag));
                            incrementOne(transitionSum, tags.get(i - 1));
                        }
//
                        /*
                         * IMPLEMENT ME!
                         * Your task is to populate 4 data structures:
                         * - reachableState (use the addReachableState method here)
                         * - startProbabilities
                         * - emissionProbabilities
                         * - transitionProbabilities
                         * you may use the incrementOne method here
                         *
                         * For the probabilities also make sure that you populate the temporary sum-data structures
                         * So that the numbers will be normalized correctly.
                         */
                    }
                });
            } catch (IOException e) {
                LOGGER.error("Something went wrong training the tagger", e);
                throw new RuntimeException(e);
            }
        });

        this.numberOfTrainedWords = sumOfWords[0];

        //normalize start probabilities
        startProbabilities
                .forEach((key, value) -> startProbabilities.put(key, value / totalSum[0]));

        //normalize emission probabilities
        emissionProbabilities
                .forEach((key, value) -> emissionProbabilities.put(key, emissionProbabilities.get(key) / emissionSum.get(key.getState())));

        //normalize transition probabilities
        transitionProbabilities
                .forEach((key, value) -> transitionProbabilities.put(key, transitionProbabilities.get(key) / transitionSum.get(key.getFrom())));



            //We initialize our HMM with the trained probabilities and the reachable states data structure
            this.model = new Model<>(
                    new ProbabilityCalculator<>(
                            new StartProbabilityCalculator(this.startProbabilities),
                            new EmissionProbabilityCalculator(this.emissionProbabilities),
                            new TransitionProbabilityCalculator(this.transitionProbabilities)
                    ),
                    new ReachableStateFinder(this.reachableStates)
            );
        }

    

    private static <T> void incrementOne(Map<T, Double> map, T key) {

        map.put(key, map.getOrDefault(key, 0d) + 1);
    }

    /**
     * A convenience method to easily add states to the reachable state data
     * structure.
     *
     * @param word the word that is the predecessor
     * @param tag the tag that is the successor
     */
    private void addReachableState(HmmWord word, HmmPosTag tag) {
        List<HmmPosTag> list = reachableStates.getOrDefault(word, new ArrayList<>());
        if (!list.contains(tag)) {
            list.add(tag);
        }
        reachableStates.put(word, list);
    }

    @Override
    public Sentence assignTags(Sentence sentence) {
        List<HmmWord> words = sentence
                .stream()
                .map(w -> new HmmWord(w.getToken()))
                .collect(Collectors.toList());

        //add artificial stop add the end of sentence:
        words.add(HmmWord.STOP);

        List<HmmPosTag> tags = new MostProbableStateSequenceFinder<>(this.model).basedOn(words);

        for (int i = 0; i < sentence.size(); i++) {
            sentence.get(i).setTag(tags.get(i).getTag());
        }

        return sentence;
    }

    public int getNumberOfTrainedWords() {
        return this.numberOfTrainedWords;
    }
}
