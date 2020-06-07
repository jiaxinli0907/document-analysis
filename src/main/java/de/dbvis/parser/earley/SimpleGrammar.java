package de.dbvis.parser.earley;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class SimpleGrammar extends Grammar {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleGrammar.class);

    public SimpleGrammar() {
        super();
        initialize();
    }

    private void initialize() {
        initRules();
        initPOS();
    }

    private void initRules() {
        addRule("S", new String[] {"NP", "VP"});
        addRule("NP", new String[] {"DT", "N"}, new String[] {"NP", "PP"});
        addRule("VP", new String[] {"VP", "PP"}, new String[] {"V", "NP"});
        addRule("PP", new String[] {"P", "NP"});
        addRule("DT", new String[] {"the"});
        addRule("N", new String[] {"child"}, new String[] {"fork"}, new String[] {"cake"});
        addRule("V", new String[] {"ate"});
        addRule("P", new String[] {"with"});
        Rules.forEach((s, rhs) -> LOGGER.debug("{} --> {}", s, Arrays.toString(rhs)));
    }

    private void addRule(String lhs, String[]... rhs) {
        Rules.put(lhs, Arrays.stream(rhs).map(RHS::new).toArray(RHS[]::new));
    }

    private void initPOS() {
        POS.add ("N");
        POS.add ("V");
        POS.add ("P");
        POS.add ("DT");
    }
}
