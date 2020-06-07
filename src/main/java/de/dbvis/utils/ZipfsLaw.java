package de.dbvis.utils;

/**
 * Created by Wolfgang Jentner.
 */
public class ZipfsLaw {

    /**
     * http://www.languagemonitor.com/number-of-words/number-of-words-in-the-english-language-1008879/
     */
    private static final long numberOfWords = 1025109;

    private static double denom;

    static {
        denom = 0;
        for(double n = 1; n <= numberOfWords; n++) {
            denom += 1.d/n;
        }
    }

    private static double getDenom() {
        return denom;
    }

    private static double getFrequency(int rank) {
        return (1.d/(double) rank) / getDenom();
    }

    /**
     * Simply an array with values from 1 - numberOfWords
     * @param numberOfWords the number of words
     * @return an array of values from 1 to numberOfWords
     */
    public static double[] getXValues(int numberOfWords) {
        double[] xvalues = new double[numberOfWords];

        for(int i = 1; i <= numberOfWords; i++) {
            xvalues[i-1] = i;
        }

        return xvalues;
    }

    /**
     * An array with the frequencies predicted by Zipf's Law
     * @param numberOfWords the number of words
     * @return an array with the frequencies predicted by Zipf's Law
     */
    public static double[] getValues(int numberOfWords) {
        double[] frequencies = new double[numberOfWords];

        for(int i = 1; i <= numberOfWords; i++) {
            frequencies[i-1] = getFrequency(i);
        }

        return frequencies;
    }

    /**
     * Used in the example (calculate your frequencies!)
     * @param numberOfWords the number of words
     * @return an array with random values between 0 and 1
     */
    public static double[] getRandomValues(int numberOfWords) {
        double[] frequencies = new double[numberOfWords];

        for(int i = 1; i <= numberOfWords; i++) {
            frequencies[i-1] = Math.random();
        }

        return frequencies;
    }
}
