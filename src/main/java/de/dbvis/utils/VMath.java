/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.utils;

import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author JiaxinLi
 */
public class VMath implements VectorMath{

    @Override
    public double euclideanNorm(RealVector v) {
        double[] a1 = v.toArray();
        double en=0.0;
        for(int i=0;i<a1.length;i++){
            en+=Math.pow(a1[i], 2);
        }
        return Math.sqrt(en);          
    }

    @Override
    public double dotProduct(RealVector v1, RealVector v2) {  
        double[] tmp1 = v1.toArray();
        double[] tmp2 = v2.toArray();
        double sum=0.0;
        for(int i=0; i<tmp1.length;i++ ){
            sum+=tmp1[i]*tmp2[i];
        }
        return sum;
    }

    @Override
    public double cosineSimilarity(RealVector v1, RealVector v2) {
 
        double dotpro = dotProduct(v1,v2);
        double en1 = euclideanNorm(v1);
        double en2 = euclideanNorm(v2);
        return dotpro/(en1*en2);
        
    }
    
    
}
