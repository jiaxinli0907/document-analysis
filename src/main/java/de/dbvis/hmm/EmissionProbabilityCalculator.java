package de.dbvis.hmm;

import io.github.adrianulbona.hmm.Emission;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wolfgang Jentner.
 */
public class EmissionProbabilityCalculator implements io.github.adrianulbona.hmm.probability.EmissionProbabilityCalculator<HmmPosTag, HmmWord> {

    private Map<Emission<HmmPosTag, HmmWord>, Double> data = new HashMap<>();

    public EmissionProbabilityCalculator(Map<Emission<HmmPosTag, HmmWord>, Double> emissionProbabilities) {
        this.data = emissionProbabilities;
        this.check();
    }

    private void check() {
        final Map<HmmPosTag, Double> sums = new HashMap<>();
        data.entrySet().forEach(entry -> {
            HmmPosTag tag = entry.getKey().getState();
            sums.put(tag, sums.getOrDefault(tag, 0d) + entry.getValue());
        });
        sums.entrySet().forEach(entry -> {
            if(Math.abs(entry.getValue() - 1d) > 1e-4)
                throw new RuntimeException("The probabilities of the emission state "+entry.getKey()+" must add up to 1. Instead its: "+entry.getValue());
        });
    }

    @Override
    public Double probability(Emission<HmmPosTag, HmmWord> emission) {
        return data.getOrDefault(emission, 0d);
    }
}
