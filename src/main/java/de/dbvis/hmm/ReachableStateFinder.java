package de.dbvis.hmm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wolfgang Jentner.
 */
public class ReachableStateFinder implements io.github.adrianulbona.hmm.ReachableStateFinder<HmmPosTag, HmmWord> {

    private static final List<HmmPosTag> UNKNOWN = new LinkedList<>();
    static {
        UNKNOWN.add(new HmmPosTag("unknown"));
    }

    private Map<HmmWord, List<HmmPosTag>> data = new HashMap<>();

    public ReachableStateFinder(Map<HmmWord, List<HmmPosTag>> reachableStates) {
        this.data = reachableStates;
    }

    @Override
    public List<HmmPosTag> reachableFor(HmmWord observation) {
        return data.getOrDefault(observation, UNKNOWN);
    }
}
