/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlptexthatespeechdetection.annotation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author nim_13512501
 */
public class AnnotatedDataFolder {
    public String annotatedDataFolderPath;
    public static final String hateSpeechFolderName = "hateSpeech";
    public static final String notHateSpeechFolderName = "notHateSpeech";
    public AnnotatedDataFolder(String annotatedDataFolderPath) throws NotDirectoryException{
        this.annotatedDataFolderPath = annotatedDataFolderPath;
        File file = new File(annotatedDataFolderPath);
        if (!file.exists())
            file.mkdir();
        if (!file.isDirectory())
            throw new java.nio.file.NotDirectoryException(annotatedDataFolderPath + "is not a directory");
        
        File hateSpeechFolder = new File(annotatedDataFolderPath + "/" + hateSpeechFolderName);
        if (hateSpeechFolder.exists()){
            if (!hateSpeechFolder.isDirectory())
                throw new java.nio.file.NotDirectoryException(annotatedDataFolderPath + "/" + hateSpeechFolderName + " already exists and is not a directory");
        }else{
            hateSpeechFolder.mkdir();
        }
        File notHateSpeechFolder = new File(annotatedDataFolderPath + "/" + notHateSpeechFolderName);
        if (notHateSpeechFolder.exists()){
            if (!hateSpeechFolder.isDirectory())
                throw new java.nio.file.NotDirectoryException(annotatedDataFolderPath + "/" + hateSpeechFolderName + " already exists and is not a directory");
        }else{
            notHateSpeechFolder.mkdir();
        }
    }
    public List<String> loadHateSpeechStrings() throws FileNotFoundException{
        return loadDataFromFolder(hateSpeechFolderName);
    }
    public List<String> loadNotHateSpeechStrings() throws FileNotFoundException{
        return loadDataFromFolder(notHateSpeechFolderName);
    }
    public String saveHateSpeechString(String annotatorName, String dateTimeString, String text) throws FileNotFoundException, IOException{
        return saveTextData(annotatorName,dateTimeString,text,hateSpeechFolderName);
    }
    public String saveNotHateSpeechString(String annotatorName, String dateTimeString, String text) throws FileNotFoundException, IOException{
        return saveTextData(annotatorName,dateTimeString,text,notHateSpeechFolderName);
    }
    public String saveTextData(String annotatorName, String dateTimeString, String text, String relativeFolderPath) throws FileNotFoundException, IOException{
        String folderPath = annotatedDataFolderPath + "/" + relativeFolderPath;
        String textHash = String.format("%x",text.hashCode());
        String fileName = dateTimeString + "__" + annotatorName + "__" + textHash;
        String filePath = folderPath + "/" + fileName;
        
        File file = new File(filePath);
        file.createNewFile();
        
        PrintWriter writer = new PrintWriter(filePath);
        writer.print(text);
        writer.close();
        
        return filePath;
    }
    
    /**
     * 
     * @param relativeFolderPath the path relative to the annotatedDataFolder
     * @return 
     */
    private List<String> loadDataFromFolder(String relativeFolderPath) throws FileNotFoundException{
        String folderPath = annotatedDataFolderPath + "/" + relativeFolderPath;
        File folder = new File(folderPath);
        
        File[] listOfFiles = folder.listFiles();
        List<String> listOfStrings = new ArrayList<String>(listOfFiles.length);
        for (File file : listOfFiles){
            String content = new Scanner(file).useDelimiter("\\Z").next();
            listOfStrings.add(content);
        }
        return listOfStrings;
    }
    
    /**
     * untuk testing
     */
    public static void main (String [] args) throws NotDirectoryException, FileNotFoundException{
        AnnotatedDataFolder dataFolder = new AnnotatedDataFolder("data");
        List<String> notHateSpeech = dataFolder.loadNotHateSpeechStrings();
        List<String> hateSpeech = dataFolder.loadHateSpeechStrings();
        System.out.println("======NOT HATE SPEECH=======");
        for (String s : notHateSpeech){
            System.out.println();
            System.out.println(s);
        }
        System.out.println();
        System.out.println();
        System.out.println("======HATE SPEECH=======");
        for (String s : hateSpeech){
            System.out.println();
            System.out.println(s);
        }
        System.out.println();
        System.out.println();
    }
}
