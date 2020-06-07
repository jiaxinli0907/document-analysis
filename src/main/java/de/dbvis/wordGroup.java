/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis;

import java.util.ArrayList;

/**
 *
 * @author thech
 */
public class wordGroup {
    
    ArrayList<String> Sentence;
    String Speaker = "";
    
    public wordGroup(ArrayList<String> x, String y){
        Sentence = new ArrayList<>(x);
        Speaker = y;
        
    }
    
}
