package com.example.afek.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Afek on 26/01/2017.
 */

public class DbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gallery.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Constants.Gallery.TABLE_NAME + " (" +
                    Constants.Gallery._ID + " INTEGER PRIMARY KEY," +
                    Constants.Gallery.KEY_DATA + " BLOB," +
                    Constants.Gallery.KEY_LONG + " DOUBLE," +
                    Constants.Gallery.KEY_LATI + " DOUBLE" +
                    ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXIST " + Constants.Gallery.TABLE_NAME;

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
