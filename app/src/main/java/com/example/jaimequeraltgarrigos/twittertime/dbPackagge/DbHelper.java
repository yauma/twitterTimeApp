package com.example.jaimequeraltgarrigos.twittertime.dbPackagge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jaimequeraltgarrigos on 21/2/16.
 */
public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE tweets (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tweetIdStr INTEGER UNIQUE, "+
                "userName TEXT, " +
                "userNick TEXT, " +
                "userImageUrl TEXT, " +
                "text TEXT, " +
                "creationDate TEXT, " +
                "retweets INTEGER," +
                "image_data BLOB);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
