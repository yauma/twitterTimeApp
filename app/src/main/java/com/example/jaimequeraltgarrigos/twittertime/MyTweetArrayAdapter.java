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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jaimequeraltgarrigos on 19/2/16.
 */
public class MyTweetArrayAdapter extends ArrayAdapter {

    private Context context;
    private int resource;
    private ArrayList<MyTweetObject> tweetArrayList;

    public MyTweetArrayAdapter(Context context, int resource, ArrayList<MyTweetObject> tweetsList) {
        super(context, resource, tweetsList);

        this.context = context;
        this.resource = resource;
        this.tweetArrayList = tweetsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder holder;
        if (rowView != null) {
            holder = (ViewHolder) rowView.getTag();

        }else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(rowView);
            rowView.setTag(holder);
        }

        Picasso.with(context).load(tweetArrayList.get(position).getImageProfileURL()).resize(200, 200)
                .centerCrop().into(holder.imageViewPhotoProfile);
        holder.textViewName.setText(tweetArrayList.get(position).getName());
        holder.textViewNick.setText(tweetArrayList.get(position).getNick());
        holder.textViewText.setText(tweetArrayList.get(position).getText());
        holder.textViewTime.setText(tweetArrayList.get(position).getCreationDate());
        holder.textViewRetweet.setText(tweetArrayList.get(position).getRetweets() + " RETWEET");

        return rowView;
    }

    static class ViewHolder {
        @Bind(R.id.textViewName)
        TextView textViewName;
        @Bind(R.id.textViewNick)
        TextView textViewNick;
        @Bind(R.id.imageView)
        ImageView imageViewPhotoProfile;
        @Bind(R.id.textViewText)
        TextView textViewText;
        @Bind(R.id.textViewTime)
        TextView textViewTime;
        @Bind(R.id.textViewRetweet)
        TextView textViewRetweet;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
