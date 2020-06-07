/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.wordnet;

import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import java.io.IOException;
import static java.lang.Integer.max;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thech
 */
public class Wordnet extends AbstractWordnet{
    
    public Wordnet() throws URISyntaxException, IOException{
        super();
    
    }

    
    @Override
    public List<String> findCommonHypernyms(String str1, String str2) {
        List<ISynsetID> hyp1 = getHypernyms(str1);
        List<ISynsetID> hyp2 = getHypernyms(str2);
        HashMap<List<IWord>,Integer> ha = new HashMap<>();
        List<String> ret = new ArrayList<>();
        
        for(ISynsetID syn : hyp1){
            
            List<IWord> wort = dictionary.getSynset(syn).getWords();
            ha.putIfAbsent(wort, 0);
            ha.put(wort, ha.get(wort) + 1);
       
        }
        
        for(ISynsetID syn : hyp2){
            
            List<IWord> wort = dictionary.getSynset(syn).getWords();
            ha.putIfAbsent(wort, 0);
            ha.put(wort, ha.get(wort) + 1);
       
        }
        
        for(Map.Entry<List<IWord>,Integer> ent : ha.entrySet()){
        
            if(ent.getValue() > 1){
            
                for(IWord dort : ent.getKey()){
                
                    ret.add(dort.getLemma());
                
                
                }
            
            
            }
        
        
        }
        return ret;
    }

    
     /**
     * Find all hypernyms for a given word.
     * Use this: dictionary.getIndexWord(word, POS.NOUN);
     * And go on from there.
     * @param word the word
     * @return a list of all hypernyms
     */
    @Override
    protected List<ISynsetID> getHypernyms(String word) {
        IIndexWord wort = dictionary.getIndexWord(word,POS.NOUN);   
        List<ISynsetID> Val = new ArrayList<>();
        for (IWordID wortID : wort.getWordIDs()){
            Val.add(wortID.getSynsetID());
        } 
        List<ISynsetID> hyp = new ArrayList<>();
        findAllHypernymsRecursively(Val,hyp);
        return hyp;
    }
    
}
