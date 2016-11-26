/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlptexthatespeechdetection.hatespeechclassifier;

import com.ansj.vec.Learn;
import com.ansj.vec.LearnDocVec;
import com.ansj.vec.Word2VEC;
import com.ansj.vec.domain.Neuron;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author nim_13512501
 */
public class TrainDoc2Vec {
    
    public static LearnDocVec getTrainedDoc2Vec(String filepath, boolean isCBow) throws IOException{
        File result = new File(filepath);
        
        File modelDir = new File("model");
        if (!modelDir.exists())
            modelDir.mkdir();

        Learn learn = new Learn();

        // ini apa artinya ngga tahu. soalnya copas
        // 训练词向量

        learn.learnFile(result);

        learn.saveModel(new File("model/word2vec.mod"));
        
        Word2VEC word2vec = new Word2VEC();
        word2vec.loadJavaModel("model/word2vec.mod");
        
        Map<String, Neuron> word2vec_model = learn.getWord2VecModel();
        
        LearnDocVec learn_doc = new LearnDocVec(word2vec_model);
        learn_doc.setIsCbow(isCBow);

        learn_doc.learnFile(result);
        
        return learn_doc;
    }
  
}
