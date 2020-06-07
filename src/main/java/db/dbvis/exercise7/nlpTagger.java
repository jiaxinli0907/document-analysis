/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.dbvis.exercise7;

import de.dbvis.structured.Sentence;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 *
 * @author thech
 */
public class nlpTagger {
    POSTaggerME tagger = null;
    POSModel model = null;

    public void initialize(String lexiconFileName) {
        try {
            InputStream modelStream =  getClass().getResourceAsStream(lexiconFileName);
            model = new POSModel(modelStream);
            tagger = new POSTaggerME(model);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String[] toArray(Sentence sent){
        String[] tmp = new String[sent.size()];
        for(int i = 0;i<tmp.length;i++){
            tmp[i] = sent.get(i).getToken();
        }
        return tmp;
    }

    public void tag(ArrayList<Sentence> text){
        initialize("/en-pos-maxent.bin");
        try {
            if (model != null) {
                POSTaggerME tagger = new POSTaggerME(model);
                if (tagger != null) {
                    for (Sentence sentence : text) {
                        String[] sent = toArray(sentence);
                        String[] tags = tagger.tag(sent);
                        for (int i = 0; i < sent.length; i++) {
                            String word = sent[i].trim();
                            String tag = tags[i].trim();
                            sentence.get(i).setTag(tag);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
