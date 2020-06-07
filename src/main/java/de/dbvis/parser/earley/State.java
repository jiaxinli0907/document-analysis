package de.dbvis.parser.earley;

import de.dbvis.parser.TreeNode;

import java.util.Arrays;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class State
{
    private String lhs;
    private RHS rhs;
    private int i,j;

    private TreeNode node;

    public State (String lhs, RHS rhs, int i, int j)
    {
        this.lhs = lhs;
        this.rhs = rhs;
        this.i = i;
        this.j = j;
    }

    public String print() {
        return this.lhs+" -> "+Arrays.toString(this.rhs.getTerms());
    }

    public String getLHS ()
    {
        return lhs;
    }
    public RHS getRHS ()
    {
        return rhs;
    }
    public int getI ()
    {
        return i;
    }
    public int getJ ()
    {
        return j;
    }
    public String getPriorToDot ()
    {
        return rhs.getPriorToDot ();
    }
    public String getAfterDot ()
    {
        return rhs.getAfterDot ();
    }
    public boolean isDotLast ()
    {
        return rhs.isDotLast ();
    }

    @Override
    public String toString() {
        return "State{" +
                "lhs='" + lhs + '\'' +
                ", rhs=" + rhs +
                ", i=" + i +
                ", j=" + j +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (getI() != state.getI()) return false;
        if (getJ() != state.getJ()) return false;
        if (lhs != null ? !lhs.equals(state.lhs) : state.lhs != null) return false;
        return rhs != null ? rhs.equals(state.rhs) : state.rhs == null;
    }

    @Override
    public int hashCode() {
        int result = lhs != null ? lhs.hashCode() : 0;
        result = 31 * result + (rhs != null ? rhs.hashCode() : 0);
        result = 31 * result + getI();
        result = 31 * result + getJ();
        return result;
    }
}
