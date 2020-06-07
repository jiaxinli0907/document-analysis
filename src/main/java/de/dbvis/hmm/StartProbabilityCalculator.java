package de.dbvis.hmm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wolfgang Jentner.
 */
public class StartProbabilityCalculator implements io.github.adrianulbona.hmm.probability.StartProbabilityCalculator<HmmPosTag> {

    private Map<HmmPosTag, Double> data = new HashMap<>();

    public StartProbabilityCalculator(Map<HmmPosTag, Double> startProbabilities) {
        this.data = startProbabilities;
        this.check();
    }

    private void check() {
        if(Math.abs(data.values().stream().mapToDouble(Double::doubleValue).sum() - 1) > 1e-4)
            throw new RuntimeException("Your data array should sum up to one");
    }

    @Override
    public Double probability(HmmPosTag state) {
        return data.getOrDefault(state, 0d);
    }
}
