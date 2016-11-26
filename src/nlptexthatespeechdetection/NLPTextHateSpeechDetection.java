/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlptexthatespeechdetection;

import java.io.IOException;
import java.nio.file.NotDirectoryException;
import nlptexthatespeechdetection.hatespeechclassifier.AnnotatedDataFolder;
import nlptexthatespeechdetection.hatespeechclassifier.HateSpeechClassifier1;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 *
 * @author Erick Chandra
 */
public class NLPTextHateSpeechDetection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TwitterException, NotDirectoryException, IOException {
        HateSpeechClassifier1 classifier = new HateSpeechClassifier1();
        AnnotatedDataFolder data = new AnnotatedDataFolder("data");
        classifier.train(data.getDateSortedLabeledData());
        
    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        StatusListener listener = new StatusListener() {
            int numHateSpeech = 0;
            int numTweets = 0;
            @Override
            public void onStatus(Status status) {
                if (status.getLang().equals("in")){
                    numTweets++;
                    if (classifier.isHateSpeech(status.getText(), 0.5)){
                        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + " * " + status.getId() + " # " + status.getLang() + " $ " + (status.getGeoLocation() == null ? "NULLGEO" : status.getGeoLocation().toString()));
                        System.out.println();
                        System.out.println("lang: " + status.getLang());
                        System.out.println("number of detected hate speech: " + numHateSpeech);
                        System.out.println("total number of streamed tweets: " + numTweets);
                        System.out.println();
                        System.out.println();
                        numHateSpeech++;
                    }                    
                }else{
                    System.out.println("ignoring non-Indonesian tweet");
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
        
        twitterStream.sample();
    }
    
}
