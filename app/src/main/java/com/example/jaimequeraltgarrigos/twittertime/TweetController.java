package com.example.jaimequeraltgarrigos.twittertime;

import android.content.Context;

import com.example.jaimequeraltgarrigos.twittertime.dbPackagge.DbTweets;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;

/**
 * Created by jaimequeraltgarrigos on 18/2/16.
 */
public class TweetController {

    private static TweetController tweetController ;
    private ArrayList<MyTweetObject> tweetsList;
    private AsyncResponse asyncResponse;

    public TweetController() {
    }

    public static TweetController getInstance(){
        if (tweetController == null){
            tweetController = new TweetController();
            return tweetController;
        }else{
            return tweetController;
        }
    }

    public void getTweetListFromAPI (Context context, String query){
        TweetAPISearch.getInstance().SearchTweets(context,query);
    }

    public void getTweetListFromDB (Context context){

    }

    public void setTweetArrayList(ArrayList<MyTweetObject> tweetArrayList) {
        this.tweetsList = tweetArrayList;
        asyncResponse.TweetsDownloaded(tweetArrayList);
    }

    public void setAsyncResponse(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    public boolean saveTweetOnDb(Context context, MyTweetObject myTweetObject, byte[] byteArrayBitmap){
        boolean tweetSavedOnDb = DbTweets.getInstance().insertTweet(context,myTweetObject,byteArrayBitmap);
        return tweetSavedOnDb;
    }
}
