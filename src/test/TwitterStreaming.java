/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import twitter4j.*;

/**
 *
 * @author Erick Chandra
 */
public class TwitterStreaming {
    public static void main(String[] args) throws TwitterException {
    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                if (status.getLang().equals("in")){
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + " * " + status.getId() + " # " + status.getLang() + " $ " + (status.getGeoLocation() == null ? "NULLGEO" : status.getGeoLocation().toString()));
                System.out.println();
                System.out.println("lang: " + status.getLang());
                System.out.println();
                System.out.println();
                    
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
        double [][]location ={{-6.1745,106.8227},{-6.9175,107.6191}};
        //filterQuery.locations(location);
        filterQuery.language("in");
        twitterStream.filter(filterQuery);
        
        twitterStream.sample();
    }
}
