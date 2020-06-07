package de.dbvis.hmm;

import io.github.adrianulbona.hmm.Transition;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wolfgang Jentner.
 */
public class TransitionProbabilityCalculator implements io.github.adrianulbona.hmm.probability.TransitionProbabilityCalculator<HmmPosTag> {

    private Map<Transition<HmmPosTag>, Double> data = new HashMap<>();

    public TransitionProbabilityCalculator(Map<Transition<HmmPosTag>, Double> transitionProbabilities) {
        this.data = transitionProbabilities;
        this.check();
    }

    private void check() {
        final Map<HmmPosTag, Double> sums = new HashMap<>();
        data.forEach((key, value) -> {
            HmmPosTag tag = key.getFrom();
            sums.put(tag, sums.getOrDefault(tag, 0d) + value);
        });
        sums.forEach((key, value) -> {
            if (Math.abs(value - 1d) > 1e-4)
                throw new RuntimeException("The probabilities of the transition from state " + key + " must add up to 1. Instead its: " + value);
        });
    }

    @Override
    public Double probability(Transition<HmmPosTag> transition) {
        return data.getOrDefault(transition, 0d);
    }
}
