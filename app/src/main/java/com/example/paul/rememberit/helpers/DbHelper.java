package com.example.paul.rememberit.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Paul on 22.06.2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABESE_VERSION = 1;
    public static final String DATABASE_NAME = "VocUp.db";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABESE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(DbContract.SQL_CREATE_TABLE_WORDS);
        db.execSQL(DbContract.SQL_CREATE_TABLE_DEFINITIONS);
        db.execSQL(DbContract.SQL_CREATE_TABLE_EXAMPLES);
        db.execSQL(DbContract.SQL_CREATE_TABLE_USER_PROGRESS);
        db.execSQL(DbContract.SQL_CREATE_TABLE_USERS);
        db.execSQL(DbContract.SQL_CREATE_TABLE_DEFINITION_EXAMPLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DbContract.SQL_DELETE_TABLE_WORDS);
        db.execSQL(DbContract.SQL_DELETE_TABLE_DEFINITIONS);
        db.execSQL(DbContract.SQL_DELETE_TABLE_EXAMPLES);
        db.execSQL(DbContract.SQL_DELETE_TABLE_USER_PROGRESS);
        db.execSQL(DbContract.SQL_DELETE_TABLE_USERS);
        onCreate(db);
    }

    public void onDowndrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean doesTableExist(String tableName, SQLiteDatabase db){
        if((db == null) || (!db.isOpen())){
            db = getReadableDatabase();
        }
        if(!db.isReadOnly()){
            db.close();
            db = getReadableDatabase();
        }
        boolean b = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                b = true;
            }
            cursor.close();
        }
        return b;
    }

}
