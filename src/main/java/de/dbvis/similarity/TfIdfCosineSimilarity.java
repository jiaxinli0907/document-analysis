/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.similarity;

import de.dbvis.utils.VMath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author JiaxinLi
 */
public class TfIdfCosineSimilarity extends AbstractCosineSimilarity{

    public TfIdfCosineSimilarity(Map<String, Map<String, Integer>> dataStructure) {
        super(dataStructure);
    }

    @Override
    public double similarity(String doc1, String doc2) {
        ArrayList<Double> tf1 = new ArrayList<>();
        ArrayList<Double> tf2 = new ArrayList<>();
        
        for(Map.Entry<String, Map<String, Integer>> set : dataStructure.entrySet()){
            if(set.getValue().containsKey(doc1)){tf1.add(set.getValue().get(doc1)*1.0);}
            else{tf1.add(0.0);}
            if(set.getValue().containsKey(doc2)){tf2.add(set.getValue().get(doc2)*1.0);}
            else{tf2.add(0.0);}
        }
        
         ArrayList<Double> df = new ArrayList<>();
         HashMap<String, Integer> ddf = new HashMap<>();
         for(Map.Entry<String, Map<String, Integer>> out : dataStructure.entrySet()){
       
             for(Map.Entry<String, Integer> in : out.getValue().entrySet()){
                 if(in.getValue()!=0){
                    ddf.putIfAbsent(out.getKey(), 0);
                    ddf.put(out.getKey(), ddf.get(out.getKey())+1);
                 }
             }
             
         }
         
         for(Map.Entry<String,Integer> tmp : ddf.entrySet()){
         
             df.add(tmp.getValue() * 1.0);
         
         }
         int N=2;
         double[] idf = new double[df.size()];
         RealVector w1 = new ArrayRealVector();
         RealVector w2 = new ArrayRealVector();
         for(int i=0;i<df.size();i++){
             idf[i] = Math.log10(N/df.get(i));
         }
         for(int j=0;j<df.size();j++){
             if(tf1.get(j)!=0){
                w1 = w1.append((1+Math.log10(tf1.get(j)))*idf[j]);
             }
             else{
                 w1 = w1.append(0);
             }
            if(tf2.get(j)!=0){
                w2 = w2.append((1+Math.log10(tf2.get(j)))*idf[j]);
             }
             else{
                 w2 = w2.append(0);
             }
         }       
        VMath mat = new VMath();       
        return mat.cosineSimilarity(w1,w2);
    }
    
}
