package com.example.musicplayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;


public class MySQLHelper extends SQLiteOpenHelper {

    private static final String createTable = "create table music_tb("
            + "_id integer primary key autoincrement,title,artist,album,album_id,time,url)";

    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("TAG","版本变化："+ oldVersion +"---->"+newVersion);
    }
}
