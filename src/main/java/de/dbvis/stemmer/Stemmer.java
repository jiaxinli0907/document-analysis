package de.dbvis.stemmer;

/**
 * Created by Wolfgang Jentner.
 */
public interface Stemmer {

    /**
     * Implement this method, think of 10 rules to stem english words.
     * @param token The input token
     * @return the stemmed token
     */

 
    String stem(String token);
    
  
    
        
    
    
}
