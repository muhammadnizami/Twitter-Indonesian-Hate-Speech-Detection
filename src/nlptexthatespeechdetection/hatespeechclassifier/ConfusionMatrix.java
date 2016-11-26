/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlptexthatespeechdetection.hatespeechclassifier;

/**
 *
 * @author nim_13512501
 */
public class ConfusionMatrix {
    int TP=0;
    int FP=0;
    int TN=0;
    int FN=0;
    
    public void add(boolean predicted, boolean actual){
        if (predicted){
            if (actual){
                TP++;
            }else{
                FP++;
            }
        }else{
            if (actual){
                FN++;
            }else{
                TN++;
            }
        }
    }
    
    public String toString(){
        String retval="";
        retval+="TN: " + TN + "\tFN: " + FN + "\n";
        retval+="FP: " + FP + "\tTP: " + TP + "\n";
        retval+="precision: "+precision()+"\n";
        retval+="recall: "+recall()+"\n";
        return retval;
    }
    
    public double precision(){
        return (double)TP/(TP+FP);
    }
    
    public double recall(){
        return (double)TP/(TP+FN);
    }
}
