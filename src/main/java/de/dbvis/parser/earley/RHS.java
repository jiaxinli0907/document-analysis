package de.dbvis.parser.earley;

import java.util.Arrays;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class RHS
{
    private String[] terms;
    private boolean hasDot = false;
    private int dot = -1;
    private final static String DOT = "@";


    public RHS (String[] t)
    {
        terms = t;
        for (int i=0; i< terms.length; i++)
        {
            if(terms[i].compareTo (DOT) == 0)
            {
                dot = i;
                hasDot = true;
                break;
            }
        }
    }

    public String[] getTerms ()
    {
        return terms;
    }
    public String getPriorToDot ()
    {
        if(hasDot && dot >0)
            return terms[dot-1];
        return "";
    }
    public String getAfterDot ()
    {
        if(hasDot && dot < terms.length-1)
            return terms[dot+1];
        return "";
    }

    public boolean hasDot ()
    {
        return hasDot;
    }
    public boolean isDotLast ()
    {
        if(hasDot)
            return (dot==terms.length-1);
        return false;
    }
    public boolean isDotFirst ()
    {
        if(hasDot)
            return (dot==0);
        return false;
    }

    public RHS addDot ()
    {
        String[] t = new String[terms.length+1];
        t[0] = DOT;
        for (int i=1; i< t.length; i++)
            t[i] = terms[i-1];
        return new RHS (t);
    }

    public RHS addDotLast ()
    {
        String[] t = new String[terms.length+1];
        for (int i=0; i< t.length-1; i++)
            t[i] = terms[i];
        t[t.length-1] = DOT;
        return new RHS (t);
    }
    public RHS moveDot ()
    {
        String[] t = new String[terms.length];
        for (int i=0; i< t.length; i++)
        {
            if (terms[i].compareTo (DOT)==0)
            {
                t[i] = terms[i+1];
                t[i+1] = DOT;
                i++;
            }
            else
                t[i] = terms[i];
        }
        return new RHS (t);
    }

    @Override
    public String toString() {
        return "RHS{" +
                "terms=" + Arrays.toString(terms) +
                ", hasDot=" + hasDot +
                ", dot=" + dot +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RHS rhs = (RHS) o;

        if (hasDot != rhs.hasDot) return false;
        if (dot != rhs.dot) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getTerms(), rhs.getTerms());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getTerms());
        result = 31 * result + (hasDot ? 1 : 0);
        result = 31 * result + dot;
        return result;
    }
}
