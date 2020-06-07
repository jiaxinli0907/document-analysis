package de.dbvis.parser;

import de.dbvis.structured.Sentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public abstract class AbstractCYKParser implements Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCYKParser.class);

    AbstractCYKParser.CYKDataStructure matrix;

    private Grammar grammar;

    /**
     * Initializes the parser with a given grammar
     * @param grammar the grammar for the parser
     */
    public AbstractCYKParser(Grammar grammar) {
        assert grammar != null;
        this.grammar = grammar;
    }

    @Override
    public final Set<TreeNode> parse(Sentence sentence) throws ParseException {
        int n = sentence.size();
        matrix = new AbstractCYKParser.CYKDataStructure(n);

        //add the POS tags to the diagonal
        for(int i = 0; i < n; i++) {
            addRulesToCell(i,i,grammar.findRulesByChildren(new TreeNode(sentence.get(i).getToken().toLowerCase())));
        }

        LOGGER.debug("Matrix after initialization\n{}", matrix);

        this.cykAlgorithm();

        LOGGER.debug("Matrix after algorithm\n{}", matrix);

        if(getRulesFromCell(0, n-1).isEmpty()) {
            throw new ParseException("Could not parse sentence", 0);
        }

        return getRulesFromCell(0, n-1);
    }

    /**
     * This method should iterate over the matrix cells as provided in the lecture.
     * On the diagonal of the matrix the POS tags are placed already (cells with a [P] below) (see output from debug)
     * Given a 4-dim matrix:
     * [[P] [1]  [4]  [6]]
     * [[]  [P]  [2]  [5]]
     * [[]  []   [P]  [3]]
     * [[]  []   []   [P]]
     * 1. You should iterate over the cells as indicated by the numbers.
     * 2. In each cell try to generate rules by looking at cells to the left and to the bottom.
     *    The way you learned it in the lecture. To combine the rules please use the getCombinedRulesMethod.
     *    You receive the rules for each cell by getRulesFromCell(i,j) method.
     *    Add your combined rules with the addRulesToCell(...) method.
     *
     * HINT: The above described parser algorithm should be implementable with the help of
     * 3 for loops. Think of a way to iterate over the cells and to combine rules for each (left/down) cells.
     */
    protected abstract void cykAlgorithm();

    /**
     * Returns the size of the matrix which is equal to the length of the input sentence.
     * @return the size of the matrix
     */
    protected int getMatrixSize() {
        return matrix.size();
    }

    /**
     * Returns the rules from a matrix cell given row index i and column index j
     * @param i the row index i
     * @param j the column index j
     * @return a set of rules, or an empty set
     */
    protected Set<TreeNode> getRulesFromCell(int i, int j) {
        return matrix.get(i, j);
    }

    /**
     * Adds a set of rules to the matrix.
     * @param i the row index i
     * @param j the column index j
     * @param rules a set of rules which must not be null
     */
    protected void addRulesToCell(int i, int j, Set<TreeNode> rules) {
        assert rules != null;
        matrix.add(i, j, rules);
    }

    /**
     * This method tries to find rules that match from
     * rulesLeft (cell to the left) and rulesDown (cell to the bottom).
     * If there are rules they are combined in another set which shall be
     * placed in the current cell.
     *
     * @param rulesLeft the cell to the left but no further than the diagonal
     * @param rulesDown the cell to the bottom but no further down than the diagonal
     * @return a combined set of rules or an empty set
     */
    protected Set<TreeNode> getCombinedRules(Set<TreeNode> rulesLeft, Set<TreeNode> rulesDown) {
        final Set<TreeNode> combinedRules = new HashSet<>();
        rulesLeft.forEach(ruleLeft -> {
            rulesDown.forEach(ruleDown -> {
                combinedRules.addAll(grammar.findRulesByChildren(ruleLeft, ruleDown));
            });
        });
        return combinedRules;
    }

    /**
     * An inner class representing the data structure which
     * is a two-dim matrix of size nxn (n = length of sentence)
     * that contains a set of rules in each cell.
     */
    protected class CYKDataStructure {
        private List<List<Set<TreeNode>>> table;

        /**
         * Initializes the data structure with empty hash sets in each cell.
         * @param lengthOfSentence the length of the sentence aka the dimensions of the matrix
         */
        CYKDataStructure(int lengthOfSentence) {
            table = new ArrayList<>(lengthOfSentence);
            for(int i = 0; i < lengthOfSentence; i++) {
                List<Set<TreeNode>> row = new ArrayList<>(lengthOfSentence);

                for(int j = 0; j < lengthOfSentence; j++) {
                    row.add(new HashSet<>());
                }

                table.add(row);
            }
        }

        /**
         * Returns the cell content (Set) of the cell with the indices i and j.
         * @param i - the row index
         * @param j - the column index
         * @return a Set which may be empty but never null
         */
        Set<TreeNode> get(int i, int j) {
            if(i > j) {
                throw new RuntimeException("i <= j must be fulfilled but i="+i+" and j="+j);
            }
            Set<TreeNode> ret = table.get(i).get(j);
            return ret != null ? ret : Collections.emptySet();
        }

        /**
         * Adds a new set to a cell.
         * @param i - the row index
         * @param j - the column index
         * @param nodes a set of nodes which must not be null
         */
        void add(int i, int j, Set<TreeNode> nodes) {
            if(i > j) {
                throw new RuntimeException("i <= j must be fulfilled but i="+i+" and j="+j);
            }
            assert nodes != null;
            Set<TreeNode> set = this.get(i, j);
            set.addAll(nodes);
        }

        /**
         * Returns the number of dimensions of the matrix aka the length of the sentence
         * @return the number of dimensions
         */
        int size() {
            return table.size();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            table.forEach(row -> {
                sb.append(row);
                sb.append("\n");
            });
            return sb.toString();
        }
    }
}
