/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlptexthatespeechdetection.dataCollection;

import nlptexthatespeechdetection.hatespeechclassifier.AnnotatedDataFolder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 *
 * @author nim_13512501
 */
public class TwitterStreamingAnnotator {
    static final String dataFolderName = "data";
    
    static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    
    
    public static void main(String[] args) throws NotDirectoryException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Nama Anda (sebagai anotator): ");
        String namaAnotator = sc.nextLine();
        AnnotatedDataFolder annotatedDataFolder = new AnnotatedDataFolder(dataFolderName);
        
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                if (status.getLang().equals("in")){
                    System.out.println();
                    System.out.println();
                    System.out.println("=======ANOTASI=======");
                    System.out.println("status: " + status.getText());
                    System.out.println();
                    System.out.println("is this a hate speech?(y/n. any other if you do not know)");
                    String annotatorResponse = sc.nextLine().trim().toLowerCase();
                    
                    
                    
                    Date date = new Date();
                    String dateString = dateFormat.format(date);

                    try{
                        if (annotatorResponse.equals("y")){
                            String filePath = annotatedDataFolder.saveHateSpeechString(namaAnotator, dateString, status.getText());
                            System.out.println("Saved data to: " + filePath);
                        }else if (annotatorResponse.equals("n")){
                            String filePath = annotatedDataFolder.saveNotHateSpeechString(namaAnotator, dateString, status.getText());
                            System.out.println("Saved data to: " + filePath);
                        }
                        System.out.println("thank you!");
                    }catch(FileNotFoundException ex){
                        ex.printStackTrace();
                    }   catch (IOException ex) {
                            Logger.getLogger(TwitterStreamingAnnotator.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }else{
                        System.out.println("ignoring non-indonesian tweet");
                    }
//                if (status.getGeoLocation() != null) {
//                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + " * " + status.getId() + " $ " + status.getGeoLocation().toString());
//                }
//                if (status.getLang().equals("id")) {
//                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + " * " + status.getId() + " # " + status.getLang() + " $ " + (status.getGeoLocation() == null ? "NULLGEO" : status.getGeoLocation().toString()));
//                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
//                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        
        twitterStream.addListener(listener);
        
        
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(new String[]{"a","i","u","e","o"});
        filterQuery.language("in");
        twitterStream.filter(filterQuery);
    }
}
