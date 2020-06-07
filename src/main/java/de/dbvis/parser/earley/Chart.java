package de.dbvis.parser.earley;

import java.util.Vector;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class Chart
{
    private Vector<State> chart;

    public Chart ()
    {
        chart = new Vector<>();
    }

    void addState(State s) {
        if(!chart.contains(s)) {
            chart.add (s);
        }
    }

    State getState(int i)
    {
        if(i < 0 || i >= chart.size ())
            return null;
        return chart.get (i);
    }

    public int size() {
        return chart.size();
    }

    @Override
    public String toString() {
        return "Chart{" +
                "chart=" + chart +
                '}';
    }
}
