/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.sentiment;

import de.dbvis.structured.Sentence;
import de.dbvis.structured.Word;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 *
 * @author JiaxinLi
 */
public class openNLP extends AbstractSentimentAnalyzer{
DoccatModel model;
    @Override
    public double sentiment(Sentence sentence) {
        String  tweet;
        tweet=toS(sentence);
        DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
        double[] outcomes = myCategorizer.categorize(tweet);
        String category = myCategorizer.getBestCategory(outcomes);
       return Double.valueOf(category);

    }
    public void trainModel() {
        InputStream dataIn = null;
        try {
        dataIn = new FileInputStream("src/main/resources/sentiment/tweets.txt");
        ObjectStream lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
	ObjectStream sampleStream = new DocumentSampleStream(lineStream);
	// Specifies the minimum number of times a feature must be seen
	int cutoff = 2;
	int trainingIterations = 30;
	model = DocumentCategorizerME.train("en", sampleStream, cutoff,trainingIterations);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dataIn != null) {
                try {
                    dataIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
