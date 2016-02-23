package com.example.jaimequeraltgarrigos.twittertime.controllers;

import android.content.Context;

import com.example.jaimequeraltgarrigos.twittertime.models.AsyncTasks.TweetAPISearch;
import com.example.jaimequeraltgarrigos.twittertime.models.contentProviderPackages.TweetsProvider;
import com.example.jaimequeraltgarrigos.twittertime.models.dbPackagge.DbTweets;
import com.example.jaimequeraltgarrigos.twittertime.models.dataClasses.MyTweetObject;
import com.example.jaimequeraltgarrigos.twittertime.views.AsyncResponse;

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

    public ArrayList<MyTweetObject> getTweetListFromContentProvider(Context context){
        tweetsList = TweetsProvider.getListTweetsFromContentProvider(context);
        return tweetsList;
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
