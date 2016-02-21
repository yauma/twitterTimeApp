package com.example.jaimequeraltgarrigos.twittertime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.ByteArrayOutputStream;
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
    private MyTweetObject myTweetObject;

    public MyTweetArrayAdapter(Context context, int resource, ArrayList<MyTweetObject> tweetsList) {
        super(context, resource, tweetsList);

        this.context = context;
        this.resource = resource;
        this.tweetArrayList = tweetsList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        final ViewHolder holder;
        if (rowView != null) {
            holder = (ViewHolder) rowView.getTag();

        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(rowView);
            rowView.setTag(holder);
        }
        myTweetObject = tweetArrayList.get(position);
        Picasso.with(context).load(myTweetObject.getImageProfileURL()).resize(200, 200)
                .centerCrop().into(holder.imageViewPhotoProfile);
        holder.textViewName.setText(myTweetObject.getName());
        holder.textViewNick.setText(myTweetObject.getNick());
        holder.textViewText.setText(myTweetObject.getText());
        holder.textViewTime.setText(myTweetObject.getCreationDate());
        holder.textViewRetweet.setText(myTweetObject.getRetweets() + " RETWEET");

        holder.imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(context)
                        .load(tweetArrayList.get(position).getImageProfileURL())
                        .into(target);
            }
        });

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
        @Bind(R.id.imageViewFavorite)
        ImageView imageViewFavorite;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            byte[] byteArrayBitmap = DbBitmapUtility.getBytes(bitmap);
            boolean tweetSavedOnDb = TweetController.getInstance().saveTweetOnDb(context, myTweetObject, byteArrayBitmap);
            if (tweetSavedOnDb == true){
                Toast.makeText(context,"Tweet Saved",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"Tweet Already Saved",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

}

class DbBitmapUtility {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}