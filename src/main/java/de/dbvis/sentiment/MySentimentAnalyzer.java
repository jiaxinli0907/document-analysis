/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.sentiment;

import de.dbvis.structured.Sentence;
import de.dbvis.structured.Word;
import de.dbvis.utils.SentimentLexicon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author JiaxinLi
 */
public class MySentimentAnalyzer extends AbstractSentimentAnalyzer{

    @Override
    public double sentiment(Sentence sentence) {
        Set<String> positive = SentimentLexicon.positiveWords();
        Set<String> negative = SentimentLexicon.negativeWords();
        int count = 0;
        double sentVal = 0;
        int Max = 1;
        int Min = -1;
        int negate = 1;
        String[] neg = {"no", "not", "rather", "couldn't", "wasn't", "didn't", "wouldn't", "shouldn't", "weren't", "don't", "doesn't", "haven't", "hasn't", "won't", "wont", "hadn't", "never", "none", "nobody", "nothing", "neither", "nor", "nowhere", "isn't", "can't", "cannot", "mustn't", "mightn't", "shan't", "without", "needn't"};
        String[] Dim = {"hardly", "less", "little", "rarely", "scarcely", "seldom"};
        Word lwort = null; 
        for (Word word : sentence){
            String tmp = word.getToken().toLowerCase();
            if(positive.contains(tmp)){
                int addVal = 1;
                if(lwort!=null){if(cont(Dim,lwort.getToken().toLowerCase())){addVal *= 0.5;}}
                sentVal += addVal*negate;
                count++;
            }
            else if(negative.contains(tmp)){
                int addVal = -1;
                if(lwort!=null){if(cont(Dim,lwort.getToken().toLowerCase())){addVal *= 0.5;}}
                sentVal += addVal*negate;
                count++;
            }
            else if(cont(neg,tmp)){negate *= -1;}
            lwort = word;
            
        
        }
        double neu = 0.0;
        if(count > 0) neu = sentVal/count;
        neu = normalize(neu, Min, Max);
        return neu;
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
    
    public Boolean cont(String[] a, String b){
    
        for(String tmp : a){
        
            if(tmp.equals(b)){return true;}
        
        }
    return false;
    
    }
    
}
