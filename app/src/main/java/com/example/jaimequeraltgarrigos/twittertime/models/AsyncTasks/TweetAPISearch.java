package com.example.jaimequeraltgarrigos.twittertime.models.AsyncTasks;

import android.content.Context;
import android.widget.Toast;

import com.example.jaimequeraltgarrigos.twittertime.controllers.TweetController;
import com.example.jaimequeraltgarrigos.twittertime.models.dataClasses.MyTweetObject;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.GuestCallback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jaimequeraltgarrigos on 18/2/16.
 */
public class TweetAPISearch {
    private static TweetAPISearch tweetAPISearch;
    ArrayList<Tweet> tweetArrayList;
    private static final String TWITTER_KEY = "ED4oFDu9IWxHdM0jg5I6npvTE";
    private static final String TWITTER_SECRET = "Nhx3XJG4cBZrNaXbymuO1LiotfYedbVORB4g9fwHyubij8szh8";
    private String query;

    public TweetAPISearch() {
    }

    public static TweetAPISearch getInstance() {
        if (tweetAPISearch == null) {
            tweetAPISearch = new TweetAPISearch();
            return tweetAPISearch;
        } else {
            return tweetAPISearch;
        }

    }

    public void SearchTweets(final Context context, final String query) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {

                TwitterApiClient twitterApiClient = Twitter.getInstance().getApiClient();
                twitterApiClient.getSearchService().tweets(query, null, null, null, "recent", 50, null, null, null, false, new GuestCallback<>(new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        // use result tweets
                        ArrayList<MyTweetObject> myTweetObjectList;
                        tweetArrayList = new ArrayList<Tweet>(result.data.tweets);
                        myTweetObjectList = cleanAndOrganizeMyTweetObjectList(tweetArrayList);
                        TweetController.getInstance().setTweetArrayList(myTweetObjectList);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        System.out.println(exception);
                    }
                }));
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(context, "Authentication Twitter API Problems", Toast.LENGTH_LONG).show();
            }
        });


    }

    private ArrayList<MyTweetObject> cleanAndOrganizeMyTweetObjectList(ArrayList<Tweet> tweetListFromAPI) {
        ArrayList<MyTweetObject> myTweetObjectList = new ArrayList<MyTweetObject>();
        for (Tweet tweet : tweetArrayList) {
            MyTweetObject myTweetObject = new MyTweetObject(tweet.id,tweet.user.name, tweet.user.screenName, tweet.user.profileImageUrl,
                    tweet.text, tweet.createdAt, tweet.retweetCount);
            myTweetObjectList.add(myTweetObject);
        }

        Collections.sort(myTweetObjectList, new Comparator<MyTweetObject>() {
            @Override
            public int compare(MyTweetObject t1, MyTweetObject t2) {
                return new Integer(t2.getRetweets()).compareTo(new Integer(t1.getRetweets()));
            }
        });
        return myTweetObjectList;
    }

}
