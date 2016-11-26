/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlptexthatespeechdetection.dataCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
public class GetTwitterDoc2VecTrainingData {
    public static final String folderName="doc2vec_data";
    public static final String fileName="doc2vec_train_data";
    static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    
    
    
    public static void main(String [] args) throws FileNotFoundException, IOException{
        File dir = new File(folderName);
        if (!dir.exists())
            dir.mkdir();
        if (!dir.isDirectory()){
            System.out.println(folderName+ " is not a directory");
            return;
        }
        
        System.out.println("number of tweets required: " );
        int numTweetsRequired = (new Scanner(System.in)).nextInt();

        String path = folderName+"/"+fileName;
        File file = new File(path);
        if (!file.exists())file.createNewFile();
        FileWriter writer = new FileWriter(path,true);
        
        
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        StatusListener listener = new StatusListener() {
            int numTweets=0;
            @Override
            public void onStatus(Status status) {
                if (status.getLang().equals("in")){
                    try {
                        String statusText = status.getText();
                        writer.write("\n");
                        writer.write(statusText);
                        numTweets++;
                        System.out.println("numTweets: " + numTweets);
                        
                        if (numTweets>=numTweetsRequired){
                            writer.close();
                            System.exit(0);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(GetTwitterDoc2VecTrainingData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                               
                }
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
