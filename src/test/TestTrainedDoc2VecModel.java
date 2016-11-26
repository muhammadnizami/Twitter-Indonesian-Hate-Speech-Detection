/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import nlptexthatespeechdetection.hatespeechclassifier.TrainDoc2Vec;
import com.ansj.vec.LearnDocVec;
import java.io.IOException;
import java.util.List;
import nlptexthatespeechdetection.hatespeechclassifier.AnnotatedDataFolder;

/**
 *
 * @author nim_13512501
 */
public class TestTrainedDoc2VecModel {
    public static final String doc2vectrainingdatapath="doc2vec_data/doc2vec_train_data";
    public static void main(String [] args) throws IOException{
        LearnDocVec learnDocVec = TrainDoc2Vec.getTrainedDoc2Vec(doc2vectrainingdatapath,Boolean.TRUE);
        String str1 = "RT @eiynamie: kalau dia dh tak sayang. memang tiada wish2 segala dah.. https://t.co/JCwSej3kh";
        String str2 = "I'm at Restaurant Yen Yen A One in Kuala Lumpur, WP Kuala Lumpur https://t.co/kyTSJtSDlo";
        String str3 = "I'm in Restaurant Yen Yen A One in Kuala Lumpur, WP Kuala Lumpur https://t.co/kyTSJtSDlo";
        System.out.println("should be very small: " + 
                euclideanDistance(learnDocVec.getUnseenDocVector(str2),learnDocVec.getUnseenDocVector(str2))
                );
        System.out.println("should be  small: " + 
                euclideanDistance(learnDocVec.getUnseenDocVector(str3),learnDocVec.getUnseenDocVector(str2))
                );
        System.out.println("should be a bit high: " + 
            euclideanDistance(learnDocVec.getUnseenDocVector(str1),learnDocVec.getUnseenDocVector(str2))
        );
        
        System.out.println("===testing from the classification dataset===");
        AnnotatedDataFolder annotatedDataFolder = new AnnotatedDataFolder("data");
        
        List<String> hateSpeeches = annotatedDataFolder.loadHateSpeechStrings();
        List<String> notHateSpeeches = annotatedDataFolder.loadNotHateSpeechStrings();
        
        String hateSpeech1 = hateSpeeches.get(1);
        String notHateSpeech1 = notHateSpeeches.get(1);
        String notHateSpeech2 = notHateSpeeches.get(2);
        System.out.println("should be  small: " + 
                euclideanDistance(learnDocVec.getUnseenDocVector(notHateSpeech1),learnDocVec.getUnseenDocVector(notHateSpeech2))
                );
        System.out.println("should be  high: " + 
                euclideanDistance(learnDocVec.getUnseenDocVector(notHateSpeech1),learnDocVec.getUnseenDocVector(hateSpeech1))
                );
        
                
    }
        
    public static double euclideanDistance(float [] v1, float [] v2){
        if (v1.length!=v2.length)
            return Float.MAX_VALUE;
        int n = v1.length;
        double sumSquaredError = 0;
        for (int i=0;i<n;i++){
            float elementDiff = v1[i]-v2[i];
            sumSquaredError += elementDiff*elementDiff;
        }
        return Math.sqrt(sumSquaredError);
    }
    
}
