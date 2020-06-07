package de.dbvis.parser;

import de.dbvis.structured.Sentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public abstract class AbstractShiftReduceParser implements Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractShiftReduceParser.class);

    protected Grammar grammar;
    protected List<TreeNode> parseList;
    protected Queue<TreeNode> queue;


    public AbstractShiftReduceParser(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public final Set<TreeNode> parse(Sentence sentence) throws ParseException {
        this.parseList = new ArrayList<>();
        this.queue = new LinkedList<>();

        sentence.forEach(word -> {
            this.queue.add(new TreeNode(word.getToken().toLowerCase()));
        });

        this.shiftReduceAlgorithm();

        if(this.parseList.size() > 1) {
            throw new ParseException("Could not parse sentence", 0);
        }

        Set<TreeNode> parses = new HashSet<>();
        parses.add(this.parseList.get(0));
        return parses;
    }

    /**
     * This method is being called.
     * Make use of the data structures.
     * Simply try a reduce, if that's not possible try a shift
     * If both are not possible stop.
     */
    protected abstract void shiftReduceAlgorithm() throws ParseException;

    /**
     * The shift operation: simply poll the queue if possible and add it to the parseList
     * @return returns true if shift was successful, false if not == queue is empty
     */
    protected abstract boolean shift();

    /**
     * The reduce operation look first at the two right items (tail) of the parseList:
     * n-2 & n-1
     * and try to reduce these with grammar.findRuleByChildren(final List<TreeNode> children) method
     * Hint: simply use subList for that
     *
     * If this does not succeed try:
     * n-1
     * to catch grammar rules like DET -> the
     *
     * do not forget to replace the new TreeNode (rules) in the list!
     * @return true if reduce was possible, false if not
     */
    protected abstract boolean reduce();


    /**
     *
     * @param operation either SHIFT or REDUCE
     */
    protected void logState(String operation) {
        LOGGER.debug("{} * {} | {}", parseList, queue, operation.toUpperCase());
    }
}
