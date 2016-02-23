package com.example.jaimequeraltgarrigos.twittertime.views;

import com.example.jaimequeraltgarrigos.twittertime.models.dataClasses.MyTweetObject;

import java.util.ArrayList;

/**
 * Created by jaimequeraltgarrigos on 19/2/16.
 */
public interface AsyncResponse {
    public void TweetsDownloaded(ArrayList<MyTweetObject> tweetArrayList);
}
