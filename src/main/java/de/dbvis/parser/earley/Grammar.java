package de.dbvis.parser.earley;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class Grammar
{
    // S -> A | B
    HashMap<String, RHS[]> Rules;
    Vector<String> POS;
    public Grammar ()
    {
        Rules = new HashMap<String, RHS[]>();
        POS = new Vector<String>();
    }


    public RHS[] getRHS (String lhs)
    {
        RHS[] rhs = null;
        if(Rules.containsKey (lhs))
        {
            rhs = Rules.get (lhs);
        }
        return rhs;
    }
    public boolean isPartOfSpeech (String s)
    {
        return POS.contains (s);
    }
}
