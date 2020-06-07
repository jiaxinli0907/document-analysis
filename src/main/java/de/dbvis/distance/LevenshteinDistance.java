/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.distance;

/**
 *
 * @author thech
 */
public class LevenshteinDistance extends AbstractLevenshteinDistance{
    
    
    int distanceImpl(String s1, String s2) {
        String tmp1 = s1.toLowerCase();
        String tmp2 = s2.toLowerCase();
   
        int [] dist = new int [tmp1.length() + 1];
        for (int j = 0; j < dist.length; j++)
            dist[j] = j;
        for (int i = 1; i <= tmp2.length(); i++) {
     
            dist[0] = i;
            int ww = i - 1;
            for (int j = 1; j <= tmp1.length(); j++) {
                int jj = Math.min(1 + Math.min(dist[j], dist[j - 1]), tmp2.charAt(i - 1) == tmp1.charAt(j - 1) ? ww : ww + 1);
                ww = dist[j];
                dist[j] = jj;
            }
        }
        return dist[tmp1.length()];
    }
    
    
}
