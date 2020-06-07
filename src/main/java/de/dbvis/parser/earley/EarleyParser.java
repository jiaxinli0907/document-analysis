/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis.parser.earley;

/**
 *
 * @author jiaxinli
 */
public class EarleyParser extends AbstractEarleyParser{

    @Override
    void predict(State s) {
        
       for(RHS rhs : find(s.getAfterDot())){
       addPredictState(s.getAfterDot(), rhs , s.getJ());
       }
    }

    @Override
    void scan(State s) {
       for(RHS rhs : find(s.getAfterDot())){
           if(scanPossible(rhs, s.getJ())){
               addScanState(s.getAfterDot(), rhs, s.getJ(), s.getJ()+1);
           }        
       }  
    }

    @Override
    void complete(State s) {
        for(int i=0; i<charts[s.getI()].size();i++){
            if(s.getLHS().equals(charts[s.getI()].getState(i).getAfterDot())){
                addCompleteState(charts[s.getI()].getState(i).getLHS(), charts[s.getI()].getState(i).getRHS(), charts[s.getI()].getState(i).getI(), s.getJ());
            }      
        }

    }
    
}
