/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlptexthatespeechdetection.hatespeechclassifier;

import com.ansj.vec.LearnDocVec;
import com.datumbox.opensource.classifiers.NaiveBayes;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nlptexthatespeechdetection.hatespeechclassifier.Logistic.Instance;
import static nlptexthatespeechdetection.hatespeechclassifier.TrainDoc2Vec.getTrainedDoc2Vec;

/**
 *
 * @author nim_13512501
 */
public class HateSpeechClassifier1 {
    public static final String doc2vectrainingdatapath="doc2vec_data/doc2vec_train_data";
    public static final String hateSpeechLabelStr="hateSpeech";
    
    LearnDocVec doc2vec = null;
    Logistic logistic = null;
    
    public void trainDoc2vec() throws IOException{
        doc2vec = getTrainedDoc2Vec(doc2vectrainingdatapath, true);
    }
    
    public void train(String [][] labeledData) throws IOException{
        if (doc2vec==null)
            trainDoc2vec();
        List<Logistic.Instance> trainingInstances = instancesFromLabeledData(labeledData, hateSpeechLabelStr);
        logistic = new Logistic(doc2vec.getLayerSize());
        logistic.train(trainingInstances);
    }
    
    public double classify(String str){
        float [] featureVector = doc2vec.getUnseenDocVector(str);
        return logistic.classify(featureVector);
    }
    
    public boolean isHateSpeech(String str,double boundary){
        return classify(str)>=boundary;
    }
        
    public List<Logistic.Instance> instancesFromLabeledData(String [][] labeledData,
            String trueLabel){
        List<Logistic.Instance> instances = new ArrayList<Instance>(labeledData.length);
        for (int i=0;i<labeledData.length;i++){
             instances.add(instanceFromLabeledDatum(labeledData[i],trueLabel));
        }
        return instances;
    }

    public Logistic.Instance instanceFromLabeledDatum(String [] labeledDatum, String trueLabel){
        int label = labeledDatum[1].equals(trueLabel)?1:0;
        float [] x = doc2vec.getUnseenDocVector(labeledDatum[0]);
        return new Logistic.Instance(label,x);
    }
    
    public ConfusionMatrix test(String[][] testSet, String trueStr){
        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        for (String [] instance : testSet){
            boolean predict = isHateSpeech(instance[0],0.5);
            boolean actual = instance[1].equals(trueStr);
            confusionMatrix.add(predict, actual);
        }
        return confusionMatrix;
    }
    
    public static void main(String [] args) throws NotDirectoryException, FileNotFoundException, IOException{
        AnnotatedDataFolder annotatedDataFolder = new AnnotatedDataFolder("data");
        boolean overSampling = false;
        String [][] sortedLabeledData = annotatedDataFolder.getDateSortedLabeledData(overSampling);
        HateSpeechClassifier1 classifier = new HateSpeechClassifier1();
        classifier.train(sortedLabeledData);
        HateSpeechClassifier1 classifier70 = new HateSpeechClassifier1();
        int splitPoint = (int) (sortedLabeledData.length*0.7);
        String [][] training70 = Arrays.copyOfRange(sortedLabeledData, 0, splitPoint);
        String [][] test70 = Arrays.copyOfRange(sortedLabeledData, splitPoint+1, sortedLabeledData.length);
        classifier70.train(training70);
        
        /*for (String [] ss: sortedLabeledData){
            for (String s : ss){
                System.out.println(s);
            }
            System.out.println("predicted: "+classifier.classify(ss[0]));
            System.out.println();
            System.out.println();
        }*/
        
        System.out.println("100% test set (test==train)");
        System.out.println(classifier.test(sortedLabeledData, hateSpeechLabelStr));
        System.out.println("30% test set (train: 70% first)");
        System.out.println(classifier70.test(test70, hateSpeechLabelStr));
    }
}
