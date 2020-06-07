/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author thech
 */
public class ShiftReduceParser extends AbstractShiftReduceParser{

    public ShiftReduceParser(Grammar grammar) {
        super(grammar);
    }

    @Override
    protected void shiftReduceAlgorithm() throws ParseException {
        while(true){
        
            boolean re = reduce();
            if(!re){boolean shi = shift();
                    
                    if(!shi){break;}
                    
                    
                    }
        
        }
    }

    @Override
    protected boolean shift() {
        if(queue.size() > 0){
            parseList.add(queue.poll());
            return true;
        }
        else{return false;}
    }

    @Override
    protected boolean reduce() {
        boolean done = false;
        TreeNode tmp;
        if(parseList.size() > 1){
            
            tmp = grammar.findRuleByChildren(Arrays.asList(parseList.get(parseList.size()-2),parseList.get(parseList.size()-1)));
            done = (tmp != null);
            if(done){
            parseList.remove(parseList.size()-1);
            parseList.remove(parseList.size()-1);
            parseList.add(tmp);
            return true;
            }
            else{
            
                        tmp = grammar.findRuleByChildren(Arrays.asList(parseList.get(parseList.size()-1)));
                        done = (tmp != null);
                        if(done){
                            parseList.remove(parseList.size()-1);
                            parseList.add(tmp);
                            return true;
                        }
                        else{return false;}
            
            
            }
        
                }
        else if(parseList.size() > 0){
            tmp = grammar.findRuleByChildren(parseList);
            done = (tmp != null);
            if(done){
                parseList.clear();
                parseList.add(tmp);
                return true;
            }
            else{return false;}
        }
        else{return false;}
    }
    
}
