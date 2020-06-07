/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.parser;

import static java.lang.reflect.Array.set;
import java.util.Set;

/**
 *
 * @author jiaxinli
 */

public class CYKParser extends AbstractCYKParser{

    public CYKParser(Grammar grammar) {
        super(grammar);
    }

    @Override
    protected void cykAlgorithm() {
        int i=0,x=0,j=0;
        while(x<matrix.size()-1){
            i=0;
            j=x+1;
            while(j<matrix.size()){    
           
                for(int k=i;k<j;k++){
                    Set<TreeNode> left;
                    Set<TreeNode> down;
                    Set<TreeNode> com;
                    left = matrix.get(i,k);
                    down = matrix.get(k+1,j);
                    com = getCombinedRules(left,down);
                    if(com!=null){
                        matrix.add(i, j, com);
                    }
                }
                i++;
                j++;
            }
            x++;
        }
    }
    
    
}
