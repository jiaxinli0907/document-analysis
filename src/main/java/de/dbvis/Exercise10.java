package de.dbvis;

import de.dbvis.sentiment.MySentimentAnalyzer;
import de.dbvis.sentiment.openNLP;
import de.dbvis.sentiment.sNLP;
import de.dbvis.structured.Sentence;
import de.dbvis.utils.LineChartFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class Exercise10 {

    public static void main(String[] args) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        /*
         * Task 1
         * Write your own Sentiment Analyzer and extend it from AbstractSentimentAnalyzer.
         * Use utils.SentimentLexicon.
         * Create your own algorithm that is capable of detecting negations in sentences (check the lecture).
         * You can also use a heuristic, you are not required to parse sentences.
         * Use the provided normalize method.
         */
        
        debateUt debra = new debateUt();
        MySentimentAnalyzer ann = new MySentimentAnalyzer();
        List<List<Double>> method1 = new ArrayList<>();
        List<Double> tp = new ArrayList<>();
        for(List<Sentence> po: debra.corpus1 ){
            tp.add(ann.sentiment(po));
        //System.out.println(k);
        }
        method1.add(tp);
        tp = new ArrayList<>();
        for(List<Sentence> po: debra.corpus2 ){
            tp.add(ann.sentiment(po));
        //System.out.println(k);
        }
        method1.add(tp);
        tp = new ArrayList<>();
        for(List<Sentence> po: debra.corpus2 ){
            tp.add(ann.sentiment(po));
        //System.out.println(k);
        }
        method1.add(tp);
        

        /*
         * Task 2
         * Use OpenNLP and StanfordNLP to analyze the sentiment.
         * Create one class for each and extend it from the AbstractSentimentAnalyzer.
         *
         * StanfordNLP features an analyzer with a trained model.
         * You'll find examples on how to do that online.
         *
         * OpenNLP does not feature a direct sentiment analyzer.
         * Instead use the DocumentCategorizerME class.
         * You'll find an example with tweets online. Use this model provided.
         * If you like you can extend this model from sentences of the debate data-set.
         * Manually annotate 10-20 sentences and see if it improves your categorizer. (Task 3)
         */
        
        sNLP stanNLP = new sNLP();
        stanNLP.init();
        List<List<Double>> method2 = new ArrayList<>();
        tp = new ArrayList<>();
        for(List<Sentence> po: debra.corpus1 ){
            tp.add(stanNLP.sentiment(po));
        //System.out.println(k);
        }
        method2.add(tp);
        tp = new ArrayList<>();
        for(List<Sentence> po: debra.corpus2 ){
            tp.add(stanNLP.sentiment(po));
        //System.out.println(k);
        }
        method2.add(tp);
        tp = new ArrayList<>();
        for(List<Sentence> po: debra.corpus2 ){
            tp.add(stanNLP.sentiment(po));
        //System.out.println(k);
        }
        method2.add(tp);
        

        openNLP opennlp = new openNLP();
        opennlp.trainModel();
        List<List<Double>> method3 = new ArrayList<>();
        tp = new ArrayList<>();
        for(List<Sentence> po: debra.corpus1 ){
            tp.add(opennlp.sentiment(po));
        //System.out.println(k);
        }
        method3.add(tp);
        tp = new ArrayList<>();
        for(List<Sentence> po: debra.corpus2 ){
            tp.add(opennlp.sentiment(po));
        //System.out.println(k);
        }
        method3.add(tp);
        tp = new ArrayList<>();
        for(List<Sentence> po: debra.corpus2 ){
            tp.add(opennlp.sentiment(po));
        //System.out.println(k);
        }
        method3.add(tp);

       
        /*
         * Task 3
         * Use the Debate Dataset (XML).
         * Calculate the average sentiment (over all sentences) of each utterance (== Collection<Sentence>) with the 3 sentiment analyzers from Task 1 & 2.
         * Put the results into a line chart, create a line chart for each debate (see method below).
         * One line chart should have three lines, one for each sentiment analyzer.
         * Discuss the results.
         */
        

        
        
    LineChartFactory.createSentimentLineChart("Debate 1", method1.get(0), method2.get(0), method3.get(0));
    LineChartFactory.createSentimentLineChart("Debate 2", method1.get(1), method2.get(1), method3.get(1));
    LineChartFactory.createSentimentLineChart("Debate 3", method1.get(2), method2.get(2), method3.get(2));
    }
}


