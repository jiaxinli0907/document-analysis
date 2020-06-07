/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.tagger;

import de.dbvis.structured.Sentence;
import de.dbvis.structured.SentenceImpl;
import de.dbvis.structured.Word;
import de.dbvis.utils.BrownCorpusHelper;
import de.dbvis.utils.Resources;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thech
 */
public class NGramTag extends AbstractNGramTagger {

    //creating a Hashmap for Unigram, Bigram and Trigram respectively.
    HashMap<String, HashMap<String, Integer>> tagHive = new HashMap<>();
    HashMap<String, HashMap<String, HashMap<String, Integer>>> bi = new HashMap<>();
    HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> tri = new HashMap<>();
    List<File> corpus = null;

    public NGramTag(int n) {
        super(n);
    }
     

    public NGramTag(int n, List<File> corpus) {
        super(n);
        this.corpus = corpus;
    }

    //training the tagger according the N gram. If its unigram it would just put each unique word into the hash map and set the value to be another hashmap. That hasmap has the tags
    //and the frequncy of that tag as the keys and values respectively. The Bigram would have a extra hasmap in between to store the tag of the precceding word. The Trigram would have
    //two extra hashmaps in between to store the two precceding words.
    @Override
    public void trainTagger() {
        
        if(this.corpus == null){
            try {
                this.corpus = Resources.getResourceFiles("data/brown");
            } catch (IOException ex) {
                Logger.getLogger(NGramTag.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(NGramTag.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.corpus.forEach(file -> {
            try {
                List<Sentence> sentences = BrownCorpusHelper.readFileFromCorpus(file.toPath());
                for (Sentence x : sentences) {
                    int indx = 0;
                    for (Word y : x) {
                        
                        if (!tagHive.containsKey(y.getToken().toLowerCase())) {
                            tagHive.put(y.getToken().toLowerCase(), new HashMap<>());
                        }
                        
                        HashMap<String, Integer> tmpHash = tagHive.get(y.getToken().toLowerCase());
                        if (tmpHash.containsKey(y.getTag())) {
                            tmpHash.put(y.getTag(), tmpHash.get(y.getTag()) + 1);
                        } else {
                            tmpHash.put(y.getTag(), 1);
                        }
                        
                        if (n > 1) {
                            if (!tri.containsKey(y.getToken().toLowerCase())) {
                                tri.put(y.getToken().toLowerCase(), new HashMap<>());
                            }
                            if (!bi.containsKey(y.getToken().toLowerCase())) {
                                bi.put(y.getToken().toLowerCase(), new HashMap<>());
                            }
                            if (indx > 0) {
                                if (n > 2) {
                                    if (!tri.get(y.getToken().toLowerCase()).containsKey(x.get(indx - 1).getTag())) {
                                        tri.get(y.getToken().toLowerCase()).put(x.get(indx - 1).getTag(), new HashMap<>());
                                    }
                                }
                                
                                if (!bi.get(y.getToken().toLowerCase()).containsKey(x.get(indx - 1).getTag())) {
                                    bi.get(y.getToken().toLowerCase()).put(x.get(indx - 1).getTag(), new HashMap<>());
                                    bi.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).put(y.getTag(), 1);
                                } else {
                                    if (bi.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).containsKey(y.getTag())) {
                                        bi.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).put(y.getTag(), bi.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).get(y.getTag()) + 1);
                                        
                                    } else {
                                        bi.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).put(y.getTag(), 1);
                                    }
                                }
                            }
                            if (n > 2) {
                                if (indx > 1) {
                                    if (!tri.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).containsKey(x.get(indx - 2).getTag())) {
                                        tri.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).put(x.get(indx - 2).getTag(), new HashMap<>());
                                        tri.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).get(x.get(indx - 2).getTag()).put(y.getTag(), 1);
                                    } else {
                                        if (tri.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).get(x.get(indx - 2).getTag()).containsKey(y.getTag())) {
                                            tri.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).get(x.get(indx - 2).getTag()).put(y.getTag(), tri.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).get(x.get(indx - 2).getTag()).get(y.getTag()) + 1);
                                            
                                        } else {
                                            tri.get(y.getToken().toLowerCase()).get(x.get(indx - 1).getTag()).get(x.get(indx - 2).getTag()).put(y.getTag(), 1);
                                        }
                                    }

                                }
                            }

                        }
                        indx++;
                    }
                    
                }
                int p = 0;
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //assigning the tag of a word by looking at either one, two or none of the precceding words
    public Word assignTag(Word word, Sentence o, int dex) {
        if (!tagHive.containsKey(word.getToken().toLowerCase())) {
            word.setTag("np");
            return word;
        } else {
            if (n == 1 || dex == 0) {

                Double count = 0.0;
                HashMap<String, Double> uniProb = new HashMap<>();
                Map.Entry<String, Double> Max = null;

                for (Map.Entry<String, Integer> entry : tagHive.get(word.getToken().toLowerCase()).entrySet()) {
                    count += entry.getValue();
                }
                for (Map.Entry<String, Integer> entry : tagHive.get(word.getToken().toLowerCase()).entrySet()) {
                    uniProb.put(entry.getKey(), entry.getValue() / count);
                }
                for (Map.Entry<String, Double> entry : uniProb.entrySet()) {
                    if (Max == null || entry.getValue().compareTo(Max.getValue()) > 0) {
                        Max = entry;
                    }

                }
                word.setTag(Max.getKey());
                return word;

            }
            if (n == 2 || (dex == 1 && n != 1)) {

                Double count = 0.0;
                HashMap<String, Double> uniProb = new HashMap<>();
                Map.Entry<String, Double> Max = null;

                for (Map.Entry<String, Integer> entry : tagHive.get(word.getToken().toLowerCase()).entrySet()) {
                    count += entry.getValue();
                }
                for (Map.Entry<String, Integer> entry : tagHive.get(word.getToken().toLowerCase()).entrySet()) {
                    Double tot = 0.0;
                    Double tagC = 0.0;
                    if (bi.get(word.getToken().toLowerCase()).containsKey(o.get(dex - 1).getTag())) {
                        for (Map.Entry<String, Integer> bientry : bi.get(word.getToken().toLowerCase()).get(o.get(dex - 1).getTag()).entrySet()) {
                            tot += bientry.getValue();
                        }
                        if (bi.get(word.getToken().toLowerCase()).get(o.get(dex - 1).getTag()).containsKey(entry.getKey())) {
                            tagC = Double.valueOf(bi.get(word.getToken().toLowerCase()).get(o.get(dex - 1).getTag()).get(entry.getKey()));
                        }
                    }
                    uniProb.put(entry.getKey(), (entry.getValue() / count) * (tagC / tot));
                }
                for (Map.Entry<String, Double> entry : uniProb.entrySet()) {
                    if (Max == null || entry.getValue().compareTo(Max.getValue()) > 0) {
                        Max = entry;
                    }

                }
                word.setTag(Max.getKey());
                return word;

            }
            if (n == 3 && dex > 1) {

                Double count = 0.0;
                HashMap<String, Double> uniProb = new HashMap<>();
                Map.Entry<String, Double> Max = null;

                for (Map.Entry<String, Integer> entry : tagHive.get(word.getToken().toLowerCase()).entrySet()) {
                    count += entry.getValue();
                }
                for (Map.Entry<String, Integer> entry : tagHive.get(word.getToken().toLowerCase()).entrySet()) {
                    Double ttot = 0.0;
                    Double ttagC = 0.0;
                    String s = "";

                    if (tri.get(word.getToken().toLowerCase()).containsKey(o.get(dex - 1).getTag())) {
                        if (tri.get(word.getToken().toLowerCase()).get(o.get(dex - 1).getTag()).containsKey(o.get(dex - 2).getTag())) {
                            for (Map.Entry<String, Integer> trientry : tri.get(word.getToken().toLowerCase()).get(o.get(dex - 1).getTag()).get(o.get(dex - 2).getTag()).entrySet()) {
                                ttot += trientry.getValue();
                            }
                            if (tri.get(word.getToken().toLowerCase()).get(o.get(dex - 1).getTag()).get(o.get(dex - 2).getTag()).containsKey(entry.getKey())) {
                                ttagC = Double.valueOf(tri.get(word.getToken().toLowerCase()).get(o.get(dex - 1).getTag()).get(o.get(dex - 2).getTag()).get(entry.getKey()));
                            }
                        }
                    }
                    uniProb.put(entry.getKey(), (entry.getValue() / count) * (ttagC / ttot));

                }
                for (Map.Entry<String, Double> entry : uniProb.entrySet()) {
                    if (Max == null || entry.getValue().compareTo(Max.getValue()) > 0) {
                        Max = entry;
                    }

                }
                word.setTag(Max.getKey());
                return word;

            }
        }
        return word;
    }

    @Override
    public Sentence assignTags(Sentence sentence) {
        int indx = 0;
        for (Word w : sentence) {

            sentence.set(indx, assignTag(w, sentence, indx));
            indx++;

        }
        return sentence;
    }

}
