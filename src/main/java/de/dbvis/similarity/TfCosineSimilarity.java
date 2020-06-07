/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.similarity;

import java.util.Map;
import org.apache.commons.math3.linear.RealVector;
import de.dbvis.utils.VMath;
import org.apache.commons.math3.linear.ArrayRealVector;

/**
 *
 * @author JiaxinLi
 */
public class TfCosineSimilarity extends AbstractCosineSimilarity{

    public TfCosineSimilarity(Map<String, Map<String, Integer>> dataStructure) {
        super(dataStructure);
    }

    @Override
    public double similarity(String doc1, String doc2) {
        RealVector d1 = new ArrayRealVector();
        RealVector d2 = new ArrayRealVector();

        for(Map.Entry<String, Map<String, Integer>> set : dataStructure.entrySet()){
            if(set.getValue().containsKey(doc1)){d1=d1.append(set.getValue().get(doc1));}
            else{d1=d1.append(0);}
            if(set.getValue().containsKey(doc2)){d2=d2.append(set.getValue().get(doc2));}
            else{d2=d2.append(0);}
        }
        VMath mat = new VMath();       
        return mat.cosineSimilarity(d1,d2);
    }


    
    
}
