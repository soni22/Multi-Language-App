package com.gojavas.tempola.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gojavas.tempola.application.TempolaApplication;

/**
 * Created by gjs331 on 7/9/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static DatabaseHelper instance = null;

    public static final String DATABASE_NAME = "Tempola.db";
    public static final String USER_TABLE_NAME = "user";
    public static final String DRS_TABLE_NAME = "drs";


    // Common table fields
    public static final String ID = "id";
    public static final String FNAME = "fname";
    public static final String LNAME = "lname";
    public static final String PHONE = "phone";
    public static final String PICTURE = "picture";
    public static final String BIO = "bio";
    public static final String ADDRESS = "address";
    public static final String STATE = "state";
    public static final String COUNTRY = "country";
    public static final String ZIPCODE = "zipcode";
    public static final String TOKEN = "token";
    public static final String TYPE = "type";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static DatabaseHelper getInstance() {
        if(instance == null) {
            instance = new DatabaseHelper(TempolaApplication.getInstance());
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL( "create table " + USER_TABLE_NAME + " (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        FNAME + " TEXT, " +
                        LNAME + " TEXT, " +
                        PHONE + " TEXT, " +
                        PICTURE + " TEXT, " +
                        BIO + " TEXT, " +
                        ADDRESS + " TEXT, " +
                        STATE + " TEXT, " +
                        COUNTRY + " TEXT, " +
                        ZIPCODE + " TEXT, " +
                        TYPE + " TEXT, " +
                        TOKEN + " TEXT" +
                        ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
