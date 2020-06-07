package de.dbvis.distance;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public abstract class AbstractLevenshteinDistance implements StringDistance {
    private int insert = 1;
    private int delete = 1;
    private int replace = 1;

    protected int[][] table;

    public int distance(String s1, String s2) {
        table = new int[s1.length()+1][s2.length()+1];
        return distanceImpl(s1, s2);
    }

    abstract int distanceImpl(String s1, String s2);
}
