package de.dbvis.parser.earley;

import de.dbvis.parser.Parser;
import de.dbvis.parser.TreeNode;
import de.dbvis.structured.Sentence;
import de.dbvis.structured.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

public abstract class AbstractEarleyParser implements Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEarleyParser.class);

    protected Grammar grammar;
    protected String[] sentence;
    protected Chart[] charts;

    public AbstractEarleyParser() {
        grammar = new SimpleGrammar();
    }

    @Override
    public Set<TreeNode> parse(Sentence s) throws ParseException {
        this.sentence = s.stream().map(Word::getToken).toArray(String[]::new);
        charts = new Chart[sentence.length + 1];
        for (int i = 0; i < charts.length; i++)
            charts[i] = new Chart();

        String[] start1 = {"@", "S"};
        RHS startRHS = new RHS(start1);
        State start = new State("$", startRHS, 0, 0);
        charts[0].addState(start);
        for (Chart chart : charts) {
            for (int j = 0; j < chart.size(); j++) {
                State st = chart.getState(j);
                String next_term = st.getAfterDot();
                if (st.isDotLast()) {
                    complete(st);
                    
                } else if (grammar.isPartOfSpeech(next_term)) {
                    scan(st);
                } else {
                    predict(st);
                }
            }
        }
        String[] fin = {"S", "@"};
        RHS finRHS = new RHS(fin);
        State finish = new State("$", finRHS, 0, sentence.length);
        State last = charts[sentence.length].getState(charts[sentence.length].size() - 1);

        if (!finish.equals(last)) {
            throw new ParseException("Could not parse sentence", 0);
        }

        List<List<List<State>>> newTable = new ArrayList<>();


        for (int i = 0; i < charts.length - 1; i++) {
            newTable.add(new ArrayList<>());
            for (int j = 1; j < charts.length; j++) {
                newTable.get(i).add(new ArrayList<>());
                for (Chart c : charts) {
                    for (int k = 0; k < c.size(); k++) {
                        State st = c.getState(k);

                        if (st.getI() == i && st.getJ() == j && st.isDotLast() && !st.getLHS().equals("$")) {
                            newTable.get(i).get(j - 1).add(st);
                        }
                    }
                }
            }
        }

        int n = newTable.size();
        final List<List<Set<TreeNode>>> tree = new ArrayList<>();
        //init empty data struc:
        for (int i = 0; i < n; i++) {
            tree.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                tree.get(i).add(new HashSet<>());
            }
        }


        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n - i; k++) {
                int x = k;
                int y = k + i;

                if (newTable.get(x).get(y).isEmpty()) {
                    continue;
                }

                //merge single cell:
                if (x == y) {
                    TreeNode cur = null;
                    for (State state : newTable.get(x).get(y)) {
                        List<TreeNode> children = new ArrayList<>();
                        for (String child : state.getRHS().getTerms()) {
                            if ("@".equals(child)) {
                                continue;
                            }
                            children.add(new TreeNode(child));
                        }

                        if (cur == null) {
                            cur = new TreeNode(state.getLHS(), children);
                        } else {
                            cur = new TreeNode(state.getLHS(), cur);
                        }
                    }
                    if (cur != null) {
                        tree.get(x).get(y).add(cur);
                    }
                }


                for (int j = 0; j < y - x; j++) {
                    int x1 = x;
                    int y1 = j + x;

                    int x2 = j + x + 1;
                    int y2 = y;

                    Set<TreeNode> left = tree.get(x1).get(y1);
                    Set<TreeNode> right = tree.get(x2).get(y2);

                    if (!left.isEmpty() && !right.isEmpty()) {
                        for (Iterator<TreeNode> it = left.iterator(); it.hasNext(); ) {
                            TreeNode leftNode = it.next();
                            for (Iterator<TreeNode> itR = right.iterator(); itR.hasNext(); ) {
                                TreeNode rightNode = itR.next();
                                if(!(leftNode.getName().equals("S") && newTable.get(x).get(y).get(0).getLHS().equals("S"))) { // don't accept partially completed sentences
                                    tree.get(x).get(y).add(new TreeNode(newTable.get(x).get(y).get(0).getLHS(), leftNode, rightNode));
                                }
                            }
                        }
                    }
                }
            }
        }

        return tree.get(0).get(tree.size() - 1);
    }

    /**
     * Get the left hand side of the production rule of the state (getAfterDot()) and find the corresponding RHS.
     * Add new predict states in the same cell (getJ()).
     *
     * @param s the current state to predict from
     */
    abstract void predict(State s);

    /**
     * Executed when we are at a terminal. Do as in predict but check for each RHS whether a scan is possible (scanPossible).
     * If it is: addScanState where the new cell should be at j+1.
     *
     * @param s the current state to scan from
     */
    abstract void scan(State s);

    /**
     * Get the left hand side of the rule (s.getLHS) and iterate through all states of charts[i].
     * Check whether this state (getAfterDot()) is equal to s.lhs and add a new completeState
     * with the index i of your found state and j of your state "s".
     *
     * @param s the current state to complete from
     */
    abstract void complete(State s);

    /**
     * Returns all possible right-hand sides of a production identified by the leftSide:
     * leftSide -> NP VP | NP
     *
     * @param leftSide the left side of the production
     * @return an array of the possible extensions
     */
    protected RHS[] find(String leftSide) {
        return grammar.getRHS(leftSide);
    }

    /**
     * Adds a new prediction state to the charts
     *
     * @param lhs the left hand side of the production rule
     * @param rhs the right hand side of the production rule
     * @param j   the index j
     */
    void addPredictState(String lhs, RHS rhs, int j) {
        charts[j].addState(new State(lhs, rhs.addDot(), j, j));
        LOGGER.debug("predict {}", new State(lhs, rhs.addDot(), j, j));
    }

    /**
     * Adds a new scan state to the charts
     *
     * @param lhs the left hand side of the production rule
     * @param rhs the right hand side of the production rule
     * @param j1  the index j
     * @param j2  the index j+1
     */
    void addScanState(String lhs, RHS rhs, int j1, int j2) {
        charts[j2].addState(new State(lhs, rhs.addDotLast(), j1, j2));
        LOGGER.debug("scan {}", new State(lhs, rhs.addDotLast(), j1, j2));
    }

    /**
     * Adds a new complete state to the charts
     *
     * @param lhs the left hand side of the production rule
     * @param rhs the right hand side of the production rule
     * @param i   the index i
     * @param j   the index j
     */
    void addCompleteState(String lhs, RHS rhs, int i, int j) {
        charts[j].addState(new State(lhs, rhs.moveDot(), i, j));
        LOGGER.debug("complete {}", new State(lhs, rhs.moveDot(), i, j));
    }

    /**
     * Checks whether a scan step is possible with a given right hand side of a production and the index j.
     *
     * @param rhs the right hand side of the production rule
     * @param j   the index j
     * @return true iff a scan step is possible, false otherwise
     */
    boolean scanPossible(RHS rhs, int j) {
        String[] terms = rhs.getTerms();
        return terms.length == 1 &&
                j < sentence.length &&
                terms[0].compareToIgnoreCase(sentence[j]) == 0;
    }
}