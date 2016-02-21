package com.example.jaimequeraltgarrigos.twittertime;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AsyncResponse {

    @Bind(R.id.listViewTweet)ListView listView;
    @Bind(R.id.textViewTweetNoFound)TextView textViewTweetNoFound;
    @Bind(R.id.progressBar)ProgressBar progressBar;

    public static final String MY_TIMEZONE = "America/Montevideo";
    private static boolean INTERNET_CONNECTION = false;
    private String query = "";
    private MyTweetArrayAdapter myTweetArrayAdapter;
    private Handler handler;
    private String timeRequest;
    private ArrayList<MyTweetObject> tweetArrayList;


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            tweetArrayList = savedInstanceState.getParcelableArrayList("tweetArrayList");
            timeRequest = savedInstanceState.getString("timeRequest");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        handler = new Handler();
        textViewTweetNoFound.setText("Sorry, no tweets found for this time. Try later please");
        textViewTweetNoFound.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        TweetController.getInstance().setAsyncResponse(this);
        //if tweetList ids not null because is load from savedInstance
        if(tweetArrayList != null){
            reloadTweetsFromList();
        }else{
            createQuery();
            getTweets(query);
        }

        return rootView;
    }

    private void reloadTweetsFromList() {
        runnable.run();
        progressBar.setVisibility(View.INVISIBLE);
        loadMyTweeterArrayAdapter();
    }


    private void getTweets(String query) {
        INTERNET_CONNECTION = isNetworkAvailable();
        if (INTERNET_CONNECTION == false) {
            Toast.makeText(getContext(), "No internet connection avalaible", Toast.LENGTH_LONG).show();
            TweetController.getInstance().getTweetListFromDB(getContext());
        } else {
            System.out.println("getTweetFromAPI");
            TweetController.getInstance().getTweetListFromAPI(getContext(), query);
        }
    }



    private void reloadTweets() {
        progressBar.setVisibility(View.VISIBLE);
        listView.removeAllViewsInLayout();
        if (textViewTweetNoFound.getVisibility() == View.VISIBLE) {
            textViewTweetNoFound.setVisibility(View.INVISIBLE);
        }
        createQuery();
        getTweets(query);

    }

    private void createQuery() {

        this.timeRequest = whatTimeIsIt();
        this.query = "It´s " + timeRequest +" and";

    }

    //checking for Internet connection
    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            System.out.println("Problem Checking Connection");
            return INTERNET_CONNECTION;
        }
    }

    //return tweetArrayList from Twiiter API or from db
    @Override
    public void TweetsDownloaded(ArrayList<MyTweetObject> tweetArrayList) {
        runnable.run();
        progressBar.setVisibility(View.INVISIBLE);
        if (tweetArrayList.size() == 0) {
            textViewTweetNoFound.setVisibility(View.VISIBLE);
        }
        this.tweetArrayList = tweetArrayList;
        loadMyTweeterArrayAdapter();
    }

    private void loadMyTweeterArrayAdapter() {
        myTweetArrayAdapter = new MyTweetArrayAdapter(getContext(), R.layout.tweet_adapter, this.tweetArrayList);
        listView.setAdapter(myTweetArrayAdapter);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String exactTime = whatTimeIsIt();
            if(!timeRequest.contains(exactTime)){
                timeRequest = exactTime;
                //Check if fragment is currently added to its activity to avoid errors
                if(isAdded()){
                    System.out.println("reloadTweets");
                    reloadTweets();
                }

            }
            System.out.println(exactTime + " " +timeRequest);
            handler.postDelayed(runnable, 1000);
        }
    };

    private String whatTimeIsIt() {
        String exactTime;
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm ");
        TimeZone timeZone = TimeZone.getTimeZone(MY_TIMEZONE);
        dateFormat.setTimeZone(timeZone);
        exactTime = dateFormat.format(cal.getTime());
        return exactTime;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("tweetArrayList", tweetArrayList);
        outState.putString("timeRequest",timeRequest);
    }
}
