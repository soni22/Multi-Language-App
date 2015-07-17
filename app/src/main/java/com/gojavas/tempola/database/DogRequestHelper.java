package com.gojavas.tempola.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.gojavas.tempola.entity.DogRequestEntity;


/**
 * Created by gjs331 on 7/15/2015.
 */
public class DogRequestHelper {

    private static DogRequestHelper instance = null;

    public static DogRequestHelper getInstance() {

        if(instance == null) {
            instance = new DogRequestHelper();

        }
        return instance;

    }


    /**
     * Update userdetail if exist otherwise insert new delivery
     * @param dogRequestEntity
     */
    public void insertOrUpdate(DogRequestEntity dogRequestEntity) {

        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor =  db.rawQuery("select * from  " + DatabaseHelper.DOG_REQUEST_TABLE_NAME + " " +
                "where " + DatabaseHelper.REQUEST_ID + " = ? ", new String[]{dogRequestEntity.getRequestid()});

        if(cursor.getCount() > 0) {
            // Update delivery
            updateDelivery(dogRequestEntity);
        } else {
            // Insert delivery
            insertDelivery(dogRequestEntity);
        }
    }

    public DogRequestEntity getDogRequest(String requestid) {

        DogRequestEntity dogRequestEntity=new DogRequestEntity();
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor =  db.rawQuery("select * from  " + DatabaseHelper.DOG_REQUEST_TABLE_NAME + " " +
                "where " + DatabaseHelper.REQUEST_ID + " = ? ", new String[]{requestid});

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false) {
                dogRequestEntity = getContentValues(cursor);
                cursor.moveToNext();
                break;
            }
        }
        cursor.close();
        return dogRequestEntity;

    }

    /**
     * Insert new user
     * @param dogRequestEntity
     * @return
     */
    private long insertDelivery(DogRequestEntity dogRequestEntity){
        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues contentValues = getContentValues(dogRequestEntity);
        long rowid = db.insert(DatabaseHelper.DOG_REQUEST_TABLE_NAME, null, contentValues);
        return rowid;
    }

    /**
     * Update user
     * @param dogRequestEntity
     */
    private void updateDelivery(DogRequestEntity dogRequestEntity) {
        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues contentValues = getContentValuesUpdate(dogRequestEntity);
        db.update(DatabaseHelper.DOG_REQUEST_TABLE_NAME, contentValues, DatabaseHelper.REQUEST_ID + " = ?" +
                " ", new
                String[]{dogRequestEntity.getRequestid()});
    }



    private ContentValues getContentValues(DogRequestEntity dogRequestEntity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.REQUEST_ID, dogRequestEntity.getRequestid());
        contentValues.put(DatabaseHelper.TIME_LEFT, dogRequestEntity.getTime_left_to_respond());
        contentValues.put(DatabaseHelper.NAME, dogRequestEntity.getName());
        contentValues.put(DatabaseHelper.CURR_ADDRESS, dogRequestEntity.getCurr_address());
        contentValues.put(DatabaseHelper.PICTURE, dogRequestEntity.getPicture());
        contentValues.put(DatabaseHelper.PHONE, dogRequestEntity.getPhone());
        contentValues.put(DatabaseHelper.ADDRESS, dogRequestEntity.getAddress());
        contentValues.put(DatabaseHelper.LATITUDE, dogRequestEntity.getLatitude());
        contentValues.put(DatabaseHelper.LONGITUDE, dogRequestEntity.getLongitude());
        contentValues.put(DatabaseHelper.RATING, dogRequestEntity.getRating());
        contentValues.put(DatabaseHelper.NUM_RATING, dogRequestEntity.getNum_rating());


        return contentValues;
    }

    private DogRequestEntity getContentValues(Cursor cursor) {

        DogRequestEntity dogRequestEntity=new DogRequestEntity();
        dogRequestEntity.setTime_left_to_respond(cursor.getInt(cursor.getColumnIndex
                (DatabaseHelper.TIME_LEFT)));
        dogRequestEntity.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)));

        dogRequestEntity.setCurr_address(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURR_ADDRESS)));
        dogRequestEntity.setPicture(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PICTURE)));
        dogRequestEntity.setPhone(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE)));
        dogRequestEntity.setAddress(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS)));
        dogRequestEntity.setLatitude(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper
                .LATITUDE)));
        dogRequestEntity.setLongitude(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper
                .LONGITUDE)));
        dogRequestEntity.setRating(cursor.getString(cursor.getColumnIndex(DatabaseHelper.RATING)));
        dogRequestEntity.setNum_rating(cursor.getInt(cursor.getColumnIndex(DatabaseHelper
                .NUM_RATING)));


        return dogRequestEntity;
    }

    //Dog Request Helper
//
//    public static final String REQUEST_ID = "requestid";
//    public static final String TIME_LEFT = "time_left_to_respond";
//    public static final String NAME = "name";
//    public static final String CURR_ADDRESS = "curr_address";
//    //    public static final String PICTURE = "picture";
////    public static final String PHONE = "phone";
////    public static final String ADDRESS = "address";
//    public static final String LATITUDE = "latitude";
//    public static final String LONGITUDE = "longitude";
//    public static final String RATING = "rating";
//    public static final String NUM_RATING = "num_rating";



    private ContentValues getContentValuesUpdate(DogRequestEntity dogRequestEntity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.TIME_LEFT, dogRequestEntity.getTime_left_to_respond());
        contentValues.put(DatabaseHelper.NAME, dogRequestEntity.getName());
        contentValues.put(DatabaseHelper.CURR_ADDRESS, dogRequestEntity.getCurr_address());
        contentValues.put(DatabaseHelper.PICTURE, dogRequestEntity.getPicture());
        contentValues.put(DatabaseHelper.PHONE, dogRequestEntity.getPhone());
        contentValues.put(DatabaseHelper.ADDRESS, dogRequestEntity.getAddress());
        contentValues.put(DatabaseHelper.LATITUDE, dogRequestEntity.getLatitude());
        contentValues.put(DatabaseHelper.LONGITUDE, dogRequestEntity.getLongitude());
        contentValues.put(DatabaseHelper.RATING, dogRequestEntity.getRating());
        contentValues.put(DatabaseHelper.NUM_RATING, dogRequestEntity.getNum_rating());


        return contentValues;
    }

}
