/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.stemmer;

/**
 *
 * @author jiaxinli
 */
public class tokenstem implements Stemmer{

    @Override
    public String stem(String token) {

        if(token.length()>3&& token.charAt(token.length()-1)=='d' && token.charAt(token.length()-2)=='e' && !isVowel(token.charAt(token.length()-3)))
            return token.substring(0, token.length()-2);
        if(token.length()>3&&token.charAt(token.length()-1)=='y' && token.charAt(token.length()-2)=='1'&& !isVowel(token.charAt(token.length()-3)))
            return token.substring(0, token.length()-2);
        if(token.length()>4&&token.charAt(token.length()-1)=='g' && token.charAt(token.length()-2)=='n'&& token.charAt(token.length()-3)=='i'&& !isVowel(token.charAt(token.length()-4)))
            return token.substring(0, token.length()-3);
        if(token.length()>4&&token.charAt(token.length()-1)=='n' && token.charAt(token.length()-2)=='o'&& token.charAt(token.length()-3)=='i'&& token.charAt(token.length()-4)=='t')
            return token.substring(0, token.length()-4);
        if(token.length()>4&&token.charAt(token.length()-1)=='s' && token.charAt(token.length()-2)=='s'&& token.charAt(token.length()-3)=='e'&& token.charAt(token.length()-4)=='n')
            return token.substring(0, token.length()-4);
        if(token.length()>5&&token.charAt(token.length()-1)=='e' && token.charAt(token.length()-2)=='v'&& token.charAt(token.length()-3)=='i'&& token.charAt(token.length()-4)=='t'&& token.charAt(token.length()-5)=='a')
            return token.substring(0, token.length()-5);
        if(token.length()>3&&token.charAt(token.length()-1)=='e' && token.charAt(token.length()-2)=='z'&& token.charAt(token.length()-3)=='i')
            return token.substring(0, token.length()-3);
        if(token.length()>3&&token.charAt(token.length()-1)=='c' && token.charAt(token.length()-2)=='i'&& !isVowel(token.charAt(token.length()-3)))
            return token.substring(0, token.length()-2);
        if(token.charAt(token.length()-1)=='r' && token.charAt(token.length()-2)=='e')
            return token.substring(0, token.length()-2);
        if(token.charAt(token.length()-1)=='e' && token.charAt(token.length()-2)=='l'&& token.charAt(token.length()-3)=='b'&& token.charAt(token.length()-4)=='i')
            return token.substring(0, token.length()-4);
        if(token.charAt(token.length()-1)=='t' && token.charAt(token.length()-2)=='n'&& token.charAt(token.length()-3)=='e'&& token.charAt(token.length()-4)=='m')
            return token.substring(0, token.length()-4);
        if(token.charAt(token.length()-1)=='s' && token.charAt(token.length()-2)=='e'&& !isVowel(token.charAt(token.length()-3)))
            return token.substring(0, token.length()-2);
        if(token.charAt(token.length()-1)=='s' && !isVowel(token.charAt(token.length()-2)))
            return token.substring(0, token.length()-1);        
        return token;
     
    }
    
    public tokenstem(){
        
    }
    
    public boolean isVowel(char v){
        if(v=='a' ||v=='e'||v=='i'||v=='o'||v=='u')
            return true;
        return false;
    }
    
}
