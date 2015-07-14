package com.gojavas.tempola.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gojavas.tempola.entity.UserEntity;

/**
 * Created by gjs331 on 7/9/2015.
 */

public class UserHelper  {

    private static UserHelper instance = null;

    public static UserHelper getInstance() {

        if(instance == null) {
            instance = new UserHelper();

        }
        return instance;

    }


    /**
     * Update userdetail if exist otherwise insert new delivery
     * @param userEntity
     */
    public void insertOrUpdate(UserEntity userEntity) {

        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from  " + DatabaseHelper.USER_TABLE_NAME + " " +
                "where " + DatabaseHelper.USERID + " = ? ", new String[] {userEntity.getUserId()});

        if(cursor.getCount() > 0) {
            // Update delivery
            updateDelivery(userEntity);
        } else {
            // Insert delivery
            insertDelivery(userEntity);
        }
    }


    /**
     * Insert new user
     * @param userEntity
     * @return
     */
    private long insertDelivery(UserEntity userEntity){
        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues contentValues = getContentValues(userEntity);
        long rowid = db.insert(DatabaseHelper.USER_TABLE_NAME, null, contentValues);
        return rowid;
    }

    /**
     * Update user
     * @param userEntity
     */
    private void updateDelivery(UserEntity userEntity) {
        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues contentValues = getContentValuespdate(userEntity);
        db.update(DatabaseHelper.USER_TABLE_NAME, contentValues, DatabaseHelper.USERID + " = ? ", new String[]{userEntity.getUserId()});
    }



    private ContentValues getContentValues(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.USERID, userEntity.getUserid());
        contentValues.put(DatabaseHelper.FNAME, userEntity.getFname());
        contentValues.put(DatabaseHelper.LNAME, userEntity.getLname());
        contentValues.put(DatabaseHelper.PHONE, userEntity.getPhoneno());
        contentValues.put(DatabaseHelper.PICTURE, userEntity.getPicture());
        contentValues.put(DatabaseHelper.BIO, userEntity.getBio());
        contentValues.put(DatabaseHelper.ADDRESS, userEntity.getAddress());
        contentValues.put(DatabaseHelper.STATE, userEntity.getState());
        contentValues.put(DatabaseHelper.COUNTRY, userEntity.getCountry());
        contentValues.put(DatabaseHelper.ZIPCODE,userEntity.getZipcode());
        contentValues.put(DatabaseHelper.VECHILETYPE, userEntity.getType());
        contentValues.put(DatabaseHelper.TOKEN, userEntity.getToken());


        return contentValues;
    }


    private ContentValues getContentValuespdate(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.FNAME, userEntity.getFname());
        contentValues.put(DatabaseHelper.LNAME, userEntity.getLname());
        contentValues.put(DatabaseHelper.PHONE, userEntity.getPhoneno());
        contentValues.put(DatabaseHelper.PICTURE, userEntity.getPicture());
        contentValues.put(DatabaseHelper.BIO, userEntity.getBio());
        contentValues.put(DatabaseHelper.ADDRESS, userEntity.getAddress());
        contentValues.put(DatabaseHelper.STATE, userEntity.getState());
        contentValues.put(DatabaseHelper.COUNTRY, userEntity.getCountry());
        contentValues.put(DatabaseHelper.ZIPCODE,userEntity.getZipcode());
        contentValues.put(DatabaseHelper.VECHILETYPE, userEntity.getType());
        contentValues.put(DatabaseHelper.TOKEN, userEntity.getToken());


        return contentValues;
    }

}
