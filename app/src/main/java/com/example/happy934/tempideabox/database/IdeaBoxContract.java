package com.example.happy934.tempideabox.database;

import android.provider.BaseColumns;

/**
 * Created by happy934 on 16/10/17.
 */

public final class IdeaBoxContract {

    // This is to prevent someone from accidentally instantiating the class
    private IdeaBoxContract(){

    }

    // This class contains the constants
    public static class IdeaBoxDB implements BaseColumns{
        public static final String TABLENAME = " IdeaBox ";
        public static final String TITLE = "Title";
        public static final String DESCRIPTION = "Description";
        public static final String TAGS = "Tag";
        public static final String TIMESTAMP = "Date";
    }

    // This is a query string to create a Table
    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE"+ IdeaBoxDB.TABLENAME + "("+
                    IdeaBoxDB._ID+" INTEGER PRIMARY KEY,"+
                    IdeaBoxDB.TITLE+" TEXT,"+
                    IdeaBoxDB.DESCRIPTION+" TEXT,"+
                    IdeaBoxDB.TAGS+" TEXT,"+
                    IdeaBoxDB.TIMESTAMP+" INTEGER)";

    // This is another query string which will check whether the specified databse exists
    // if there is an upgrade request then the database will be dropped.
    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS "+IdeaBoxDB.TABLENAME;

}
