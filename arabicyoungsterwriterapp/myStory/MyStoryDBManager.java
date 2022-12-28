package com.example.arabicyoungsterwriterapp.myStory;


import static com.example.arabicyoungsterwriterapp.myStory.MyStoryDatabaseHelper.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class MyStoryDBManager {

    private MyStoryDatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public MyStoryDBManager(Context c) {
        context = c;
    }

    public MyStoryDBManager open() throws SQLException {
        dbHelper = new MyStoryDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc, String imgUrl, String tvColor, String tvBack) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(MyStoryDatabaseHelper.TITLE, name);
        contentValue.put(MyStoryDatabaseHelper.PARAGRAPH, desc);
        contentValue.put(MyStoryDatabaseHelper.IMGURL, imgUrl);
        contentValue.put(MyStoryDatabaseHelper.TVCOLOR, tvColor);
        contentValue.put(MyStoryDatabaseHelper.TVBACK, tvBack);
        database.insert(TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { MyStoryDatabaseHelper._ID, MyStoryDatabaseHelper.TITLE, MyStoryDatabaseHelper.PARAGRAPH,  MyStoryDatabaseHelper.IMGURL,  MyStoryDatabaseHelper.TVCOLOR,  MyStoryDatabaseHelper.TVBACK};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void delete(long _id) {
        database.delete(TABLE_NAME, MyStoryDatabaseHelper._ID + "=" + _id, null);
    }

}
