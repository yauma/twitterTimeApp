package com.example.jaimequeraltgarrigos.twittertime;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AsyncResponse {


    public static final String MY_TIMEZONE = "America/Montevideo";
    private static boolean INTERNET_CONNECTION = false;
    private String query = "";
    private ArrayList<Tweet> tweetArrayList;
    private ListView listView;
    private ProgressBar progressBar;
    private TextView textViewTweetNoFound;
    private MyTweetArrayAdapter myTweetArrayAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.listViewTweet);
        textViewTweetNoFound = (TextView) rootView.findViewById(R.id.textViewTweetNoFound);
        textViewTweetNoFound.setText("Sorry, no tweets found for this time. Try later please");
        textViewTweetNoFound.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        TweetController.getInstance().setAsyncResponse(this);
        createQuery();

        INTERNET_CONNECTION = isNetworkAvailable();
        if (INTERNET_CONNECTION == false) {
            Toast.makeText(getContext(), "No internet connection avalaible", Toast.LENGTH_LONG).show();
            TweetController.getInstance().getTweetListFromDB(getContext());
        } else {
            TweetController.getInstance().getTweetListFromAPI(getContext(), query);
        }

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Searching Tweets", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                progressBar.setVisibility(View.VISIBLE);
                removeAllViews();
                createQuery();
                TweetController.getInstance().getTweetListFromAPI(getContext(), query);
            }
        });

        return rootView;
    }

    private void removeAllViews() {
        listView.removeAllViewsInLayout();
        if(textViewTweetNoFound.getVisibility() == View.VISIBLE){
            textViewTweetNoFound.setVisibility(View.INVISIBLE);
        }
    }

    private void createQuery() {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm ");
        TimeZone timeZone = TimeZone.getTimeZone(MY_TIMEZONE);
        dateFormat.setTimeZone(timeZone);
        this.query =  "It´s " + dateFormat.format(cal.getTime()) + " and";
        System.out.println(query);
    }

    //checking for Internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Async return from Twiiter API or from db
    @Override
    public void TweetsDownloaded(ArrayList<Tweet> tweetArrayList) {
        progressBar.setVisibility(View.INVISIBLE);
        if(tweetArrayList.size() == 0){
            textViewTweetNoFound.setVisibility(View.VISIBLE);
        }
        this.tweetArrayList = tweetArrayList;
        myTweetArrayAdapter = new MyTweetArrayAdapter(getContext(),R.layout.tweet_adapter,tweetArrayList);
        listView.setAdapter(myTweetArrayAdapter);
    }
}
