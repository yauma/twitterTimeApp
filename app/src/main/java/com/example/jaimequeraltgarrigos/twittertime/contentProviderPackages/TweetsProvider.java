package com.example.jaimequeraltgarrigos.twittertime.contentProviderPackages;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.example.jaimequeraltgarrigos.twittertime.MyTweetObject;
import com.example.jaimequeraltgarrigos.twittertime.dbPackagge.DbHelper;

import java.util.ArrayList;

/**
 * Created by jaimequeraltgarrigos on 21/2/16.
 */
public class TweetsProvider extends ContentProvider {
    private static final String URI = "content://com.example.jaimequeraltgarrigos.twittertime.contentproviders/tweets";
    public static final Uri CONTENT_URI = Uri.parse(URI);
    //Data Base
    private DbHelper dbHelper;
    private static final String DB_NAME = "TwitterTimeDb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_TWEETS = "tweets";
    //URI Matcher
    private static final int TWEETS = 1;
    private static final int TWEETS_ID = 2;
    private static final UriMatcher uriMatcher;

    private static ArrayList<MyTweetObject> myTweetObjectsList;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.jaimequeraltgarrigos.twittertime.contentproviders", "tweets", TWEETS);
        uriMatcher.addURI("com.example.jaimequeraltgarrigos.twittertime.contentproviders", "tweets/#", TWEETS_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext(), DB_NAME, null, DB_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //if the query is with an ID
        String where = selection;
        if (uriMatcher.match(uri) == TWEETS_ID) {
            where = "tweetIdStr=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_TWEETS, projection, where, selectionArgs, null, null, sortOrder);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case TWEETS:
                return "vnd.android.cursor.dir/vnd.jaimequeralt.tweet";
            case TWEETS_ID:
                return "vnd.android.cursor.item/vnd.jaimequeralt.tweet";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId = 1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        regId = db.insert(TABLE_TWEETS, null, values);

        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cont;

        String where = selection;
        if (uriMatcher.match(uri) == TWEETS_ID) {
            where = "tweetIdStr=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cont = db.delete(TABLE_TWEETS, where, selectionArgs);
        return cont;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cont;

        String where = selection;
        if (uriMatcher.match(uri) == TWEETS_ID) {
            where = "tweetIdStr=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cont = db.update(TABLE_TWEETS, values, where, selectionArgs);
        return cont;
    }

    public static final class Tweets implements BaseColumns {

        //Column names
        public static final String COL_TWEETS_STR_ID = "tweetIdStr";
        public static final String COL_NAME = "userName";
        public static final String COL_NICK = "userNick";
        public static final String COL_IMAGE_URL = "userImageUrl";
        public static final String COL_TEXT = "text";
        public static final String COL_CREATION_DATE = "creationDate";
        public static final String COL_RETWEETS = "retweets";
        public static final String COL_IMAGE_DATA = "image_data";

        private Tweets() {
        }
    }

    public static ArrayList<MyTweetObject> getListTweetsFromContentProvider(Context context) {
        String[] projection = new String[]{
                TweetsProvider.Tweets._ID,
                TweetsProvider.Tweets.COL_TWEETS_STR_ID,
                TweetsProvider.Tweets.COL_NAME,
                TweetsProvider.Tweets.COL_NICK,
                TweetsProvider.Tweets.COL_IMAGE_URL,
                TweetsProvider.Tweets.COL_TEXT,
                TweetsProvider.Tweets.COL_CREATION_DATE,
                TweetsProvider.Tweets.COL_RETWEETS,
                TweetsProvider.Tweets.COL_IMAGE_DATA,
        };

        Uri tweetsUri = TweetsProvider.CONTENT_URI;

        ContentResolver cr = context.getContentResolver();

        //We perform query
        Cursor cur = cr.query(tweetsUri,
                projection, //Columns
                null,       //where clause query
                null,       //Selection Argumentos query
                null);

        if (cur.moveToFirst()) {
            long tweetStrId;
            String name;
            String nick;
            String imageUrl;
            String text;
            String creationDate;
            int retweets;
            byte[] byteArrayBitmap;
            myTweetObjectsList = new ArrayList<>();

            int colTweetStrId = cur.getColumnIndex(Tweets.COL_TWEETS_STR_ID);
            int colName = cur.getColumnIndex(Tweets.COL_NAME);
            int colNick = cur.getColumnIndex(Tweets.COL_NICK);
            int colImageUrl = cur.getColumnIndex(Tweets.COL_IMAGE_URL);
            int colText = cur.getColumnIndex(Tweets.COL_TEXT);
            int colCreationDate = cur.getColumnIndex(Tweets.COL_CREATION_DATE);
            int colRetweets = cur.getColumnIndex(Tweets.COL_RETWEETS);
            int colImageData = cur.getColumnIndex(Tweets.COL_IMAGE_DATA);


            do {
                tweetStrId = cur.getLong(colTweetStrId);
                name = cur.getString(colName);
                nick = cur.getString(colNick);
                imageUrl = cur.getString(colImageUrl);
                text = cur.getString(colText);
                creationDate = cur.getString(colCreationDate);
                retweets = cur.getInt(colRetweets);
                byteArrayBitmap = cur.getBlob(colImageData);

                MyTweetObject myTweetObject = new MyTweetObject(tweetStrId,name,nick,imageUrl,text,creationDate,retweets);
                myTweetObject.setImageByteArray(byteArrayBitmap);
                myTweetObjectsList.add(myTweetObject);
            } while (cur.moveToNext());
        }
        return myTweetObjectsList;
    }
}
