package com.example.jaimequeraltgarrigos.twittertime.models.dbPackagge;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.jaimequeraltgarrigos.twittertime.models.dataClasses.MyTweetObject;

/**
 * Created by jaimequeraltgarrigos on 21/2/16.
 */
public class DbTweets {

    private static DbTweets dbTweets;
    private DbHelper dbHelper;
    SQLiteDatabase db;

    public DbTweets (){}

    public static DbTweets getInstance (){
        if(dbTweets == null){
            dbTweets = new DbTweets();
            return dbTweets;
        }else{
            return dbTweets;
        }
    }

    public boolean insertTweet (Context context, MyTweetObject myTweetObject, byte[] byteArrayBitMap ){

        dbHelper = new DbHelper(context, "TwitterTimeDb", null, 1);
        db = dbHelper.getWritableDatabase();
        ContentValues newTweet = new ContentValues();

        newTweet.put("tweetIdStr", myTweetObject.getidStr());
        newTweet.put("userName", myTweetObject.getName());
        newTweet.put("userNick", myTweetObject.getNick());
        newTweet.put("userImageUrl", myTweetObject.getImageProfileURL());
        newTweet.put("text", myTweetObject.getText());
        newTweet.put("creationDate", myTweetObject.getCreationDate());
        newTweet.put("retweets", myTweetObject.getRetweets());
        newTweet.put("image_data", byteArrayBitMap);

        try {
            long i = db.insertOrThrow("tweets", null, newTweet);
            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }

    }
}
