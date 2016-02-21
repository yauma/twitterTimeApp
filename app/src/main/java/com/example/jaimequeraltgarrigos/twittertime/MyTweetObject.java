package com.example.jaimequeraltgarrigos.twittertime;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jaimequeraltgarrigos on 21/2/16.
 */
public class MyTweetObject implements Parcelable {

    private String name, nick, imageProfileURL, text, creationDate;
    private int retweets;
    private byte[] imageByteArray;

    public MyTweetObject(String name, String nick, String imageProfileURL, String text, String creationDate, int retweets) {
        this.name = name;
        this.nick = nick;
        this.imageProfileURL = imageProfileURL;
        this.text = text;
        this.creationDate = creationDate;
        this.retweets = retweets;
    }

    public String getName() {
        return name;
    }

    public String getNick() {
        return nick;
    }

    public String getImageProfileURL() {
        return imageProfileURL;
    }

    public String getText() {
        return text;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public int getRetweets() {
        return retweets;
    }

    protected MyTweetObject(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(nick);
        parcel.writeString(imageProfileURL);
        parcel.writeString(text);
        parcel.writeString(creationDate);
        parcel.writeInt(retweets);
    }

    private void readFromParcel(Parcel in) {
        name = in.readString();
        nick = in.readString();
        imageProfileURL = in.readString();
        text = in.readString();
        creationDate = in.readString();
        retweets = in.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Creator<MyTweetObject>() {
        @Override
        public MyTweetObject createFromParcel(Parcel in) {
            return new MyTweetObject(in);
        }

        @Override
        public MyTweetObject[] newArray(int size) {
            return new MyTweetObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
