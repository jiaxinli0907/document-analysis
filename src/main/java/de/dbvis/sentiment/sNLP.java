/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.sentiment;

import de.dbvis.structured.Sentence;
import de.dbvis.structured.Word;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author JiaxinLi
 */
public class sNLP extends AbstractSentimentAnalyzer {
       
    StanfordCoreNLP pipeline;
	public void init() {
                Properties props = new Properties();
                props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}

    @Override
    public double sentiment(Sentence sentence) {
   
        String s;
        s = toS(sentence);
        double mainSentiment = 0;
        if (s != null && s.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(s);
            for (CoreMap sen : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sen.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sen.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
       return normalize(mainSentiment,0,4);
}
    
    @Override
        public double sentiment(Collection<Sentence> sentences) {
        int count = 0;
        double ret = 0.0;
        for(Sentence sent : sentences){
            ret += sentiment(sent);
            count++;
        
        }
    
        return ret/count;
    }
    public String toS(Sentence sentence){
        String tmp="";
        for(Word word : sentence){
            
            tmp += word.getToken() + " ";
        }
        return tmp;
    }
}
