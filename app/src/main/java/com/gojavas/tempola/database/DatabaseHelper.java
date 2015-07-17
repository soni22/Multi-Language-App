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
    public static final String  DRIVER_TABLE_NAME = "driver";
    public static final String DOG_REQUEST_TABLE_NAME = "dogrequests";
    public static final String TRIP_TRACKING = "triptracking";



    public static final String ID = "id";

    // Driver table fields

    public static final String USERID = "userid";
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
    public static final String VECHILETYPE = "type";


    //Dog Request Helper

    public static final String REQUEST_ID = "requestid";
    public static final String TIME_LEFT = "time_left_to_respond";
    public static final String NAME = "name";
    public static final String CURR_ADDRESS = "curr_address";
//    public static final String PICTURE = "picture";
//    public static final String PHONE = "phone";
//    public static final String ADDRESS = "address";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String RATING = "rating";
    public static final String NUM_RATING = "num_rating";


    //Tracking Table



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

        sqLiteDatabase.execSQL( "create table " + DRIVER_TABLE_NAME + " (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        USERID + " TEXT, " +
                        FNAME + " TEXT, " +
                        LNAME + " TEXT, " +
                        PHONE + " TEXT, " +
                        PICTURE + " TEXT, " +
                        BIO + " TEXT, " +
                        ADDRESS + " TEXT, " +
                        STATE + " TEXT, " +
                        COUNTRY + " TEXT, " +
                        ZIPCODE + " TEXT, " +
                        TOKEN + " TEXT, " +
                        VECHILETYPE + " TEXT" +
                        ")"
        );

        sqLiteDatabase.execSQL( "create table " + DOG_REQUEST_TABLE_NAME + " (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        REQUEST_ID + " INTEGER, " +
                        TIME_LEFT + " TEXT, " +
                        NAME + " TEXT, " +
                        CURR_ADDRESS + " TEXT, " +
                        PICTURE + " TEXT, " +
                        PHONE + " TEXT, " +
                        ADDRESS + " TEXT, " +
                        LATITUDE + " REAL, " +
                        LONGITUDE + " REAL, " +
                        RATING + " TEXT, " +
                        NUM_RATING + " INTEGER" +
                        ")"
        );

        sqLiteDatabase.execSQL( "create table " + TRIP_TRACKING + " (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        REQUEST_ID + " INTEGER, " +
                        LATITUDE + " REAL, " +
                        LONGITUDE + " REAL" +
                        ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
