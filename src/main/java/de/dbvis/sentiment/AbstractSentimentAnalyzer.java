package de.dbvis.sentiment;

import de.dbvis.structured.Sentence;

import java.util.Collection;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public abstract class AbstractSentimentAnalyzer implements SentimentAnalyzer {

    @Override
    public double sentiment(Collection<Sentence> sentences) {
        return sentences
                .stream()
                .mapToDouble(this::sentiment)
                .average()
                .getAsDouble();
    }


    protected double normalize(double actualValue, double minimum, double maximum) {
        if(maximum <= minimum) {
            throw new IllegalArgumentException("The maximum must be greater than the minimum");
        }
        return (actualValue - minimum) / (maximum - minimum);
    }
}
