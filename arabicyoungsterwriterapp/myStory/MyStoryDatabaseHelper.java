package com.example.arabicyoungsterwriterapp.myStory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyStoryDatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "stories";

    // Table columns
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String PARAGRAPH = "paragraph";
    public static final String IMGURL = "img_url";
    public static final String TVCOLOR = "tv_color";
    public static final String TVBACK = "tv_back";

    // Database Information
    static final String DB_NAME = "MYSTORY.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT NOT NULL, " + PARAGRAPH + " TEXT, "+IMGURL+" TEXT, "+ TVCOLOR +" TEXT, "+TVBACK+" TEXT);";

    public MyStoryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
