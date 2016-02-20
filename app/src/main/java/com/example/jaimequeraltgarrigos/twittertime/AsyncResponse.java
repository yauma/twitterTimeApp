package com.example.jaimequeraltgarrigos.twittertime;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;

/**
 * Created by jaimequeraltgarrigos on 19/2/16.
 */
public interface AsyncResponse {
    public void TweetsDownloaded(ArrayList<Tweet> tweetArrayList);
}
