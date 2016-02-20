package com.example.jaimequeraltgarrigos.twittertime;

import android.content.Context;

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
import java.util.List;

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

    public void SearchTweets(Context context, final String query) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {

                TwitterApiClient twitterApiClient = Twitter.getInstance().getApiClient();
                twitterApiClient.getSearchService().tweets("\"" + query + "\"", null, null, null, null, 50, null, null, null, false, new GuestCallback<>(new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        // use result tweets
                        tweetArrayList = new ArrayList<Tweet>(result.data.tweets);
                        tweetArrayList.size();
                        TweetController.getInstance().setTweetArrayList(tweetArrayList);
                    }
                    @Override
                    public void failure(TwitterException exception) {
                        // handle exceptions
                    }
                }));
            }

            @Override
            public void failure(TwitterException e) {
                int i = 0;
            }
        });


    }


}
