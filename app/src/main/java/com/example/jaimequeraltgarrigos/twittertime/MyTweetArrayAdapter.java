package com.example.jaimequeraltgarrigos.twittertime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaimequeraltgarrigos on 19/2/16.
 */
public class MyTweetArrayAdapter extends ArrayAdapter {

    private Context context;
    private int resource;
    private ArrayList<Tweet> tweetArrayList;

    public MyTweetArrayAdapter(Context context, int resource, ArrayList<Tweet> tweetsList) {
        super(context, resource, tweetsList);

        this.context = context;
        this.resource = resource;
        this.tweetArrayList = tweetsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(resource, parent, false);
        }

        TextView textViewName = (TextView) rowView.findViewById(R.id.textViewName);
        TextView textViewNick = (TextView) rowView.findViewById(R.id.textViewNick);
        ImageView imageViewPhotoProfile = (ImageView) rowView.findViewById(R.id.imageView);
        TextView textViewText = (TextView) rowView.findViewById(R.id.textViewText);
        TextView textViewTime = (TextView) rowView.findViewById(R.id.textViewTime);
        TextView textViewRetweet = (TextView) rowView.findViewById(R.id.textViewRetweet);

        Picasso.with(context).load(tweetArrayList.get(position).user.profileImageUrl).resize(200, 200)
                .centerCrop().into(imageViewPhotoProfile);
        textViewName.setText(tweetArrayList.get(position).user.name);
        textViewNick.setText(tweetArrayList.get(position).user.screenName);
        textViewText.setText(tweetArrayList.get(position).text);
        textViewTime.setText(tweetArrayList.get(position).createdAt);
        textViewRetweet.setText(tweetArrayList.get(position).retweetCount + " RETWEET");

        return rowView;
    }
}
