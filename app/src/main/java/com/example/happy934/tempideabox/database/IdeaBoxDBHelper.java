package com.example.happy934.tempideabox.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by happy934 on 16/10/17.
 */

public class IdeaBoxDBHelper extends SQLiteOpenHelper{
    public static final String DatabaseName = "IdeaBox2.db";
    public static final int DatabaseVersion = 2;

    public IdeaBoxDBHelper(Context context){
        super(context, DatabaseName,null,DatabaseVersion);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(IdeaBoxContract.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL(IdeaBoxContract.SQL_DELETE_ENTRIES);
    }
}
