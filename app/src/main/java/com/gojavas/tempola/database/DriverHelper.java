package com.gojavas.tempola.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gojavas.tempola.entity.DriverEntity;

/**
 * Created by gjs331 on 7/9/2015.
 */

public class DriverHelper {

    private static DriverHelper instance = null;

    public static DriverHelper getInstance() {

        if(instance == null) {
            instance = new DriverHelper();

        }
        return instance;

    }


    /**
     * Update userdetail if exist otherwise insert new delivery
     * @param driverEntity
     */
    public void insertOrUpdate(DriverEntity driverEntity) {

        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from  " + DatabaseHelper.DRIVER_TABLE_NAME + " " +
                "where " + DatabaseHelper.USERID + " = ? ", new String[] {driverEntity.getUserId()});

        if(cursor.getCount() > 0) {
            // Update delivery
            updateDelivery(driverEntity);
        } else {
            // Insert delivery
            insertDelivery(driverEntity);
        }
    }


    /**
     * Insert new user
     * @param driverEntity
     * @return
     */
    private long insertDelivery(DriverEntity driverEntity){
        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues contentValues = getContentValues(driverEntity);
        long rowid = db.insert(DatabaseHelper.DRIVER_TABLE_NAME, null, contentValues);
        return rowid;
    }

    /**
     * Update user
     * @param driverEntity
     */
    private void updateDelivery(DriverEntity driverEntity) {
        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        ContentValues contentValues = getContentValuespdate(driverEntity);
        db.update(DatabaseHelper.DRIVER_TABLE_NAME, contentValues, DatabaseHelper.USERID + " = ? ", new
                String[]{driverEntity.getUserId()});
    }



    private ContentValues getContentValues(DriverEntity driverEntity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.USERID, driverEntity.getUserid());
        contentValues.put(DatabaseHelper.FNAME, driverEntity.getFname());
        contentValues.put(DatabaseHelper.LNAME, driverEntity.getLname());
        contentValues.put(DatabaseHelper.PHONE, driverEntity.getPhoneno());
        contentValues.put(DatabaseHelper.PICTURE, driverEntity.getPicture());
        contentValues.put(DatabaseHelper.BIO, driverEntity.getBio());
        contentValues.put(DatabaseHelper.ADDRESS, driverEntity.getAddress());
        contentValues.put(DatabaseHelper.STATE, driverEntity.getState());
        contentValues.put(DatabaseHelper.COUNTRY, driverEntity.getCountry());
        contentValues.put(DatabaseHelper.ZIPCODE, driverEntity.getZipcode());
        contentValues.put(DatabaseHelper.VECHILETYPE, driverEntity.getType());
        contentValues.put(DatabaseHelper.TOKEN, driverEntity.getToken());


        return contentValues;
    }


    private ContentValues getContentValuespdate(DriverEntity driverEntity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.FNAME, driverEntity.getFname());
        contentValues.put(DatabaseHelper.LNAME, driverEntity.getLname());
        contentValues.put(DatabaseHelper.PHONE, driverEntity.getPhoneno());
        contentValues.put(DatabaseHelper.PICTURE, driverEntity.getPicture());
        contentValues.put(DatabaseHelper.BIO, driverEntity.getBio());
        contentValues.put(DatabaseHelper.ADDRESS, driverEntity.getAddress());
        contentValues.put(DatabaseHelper.STATE, driverEntity.getState());
        contentValues.put(DatabaseHelper.COUNTRY, driverEntity.getCountry());
        contentValues.put(DatabaseHelper.ZIPCODE, driverEntity.getZipcode());
        contentValues.put(DatabaseHelper.VECHILETYPE, driverEntity.getType());
        contentValues.put(DatabaseHelper.TOKEN, driverEntity.getToken());


        return contentValues;
    }


    /**
     * Get delivery detail
     * @return
     */
    public DriverEntity getDriverDetail(String driverid) {
        DriverEntity driverEntity = new DriverEntity();
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from  " + DatabaseHelper.DRIVER_TABLE_NAME + " where " +
                DatabaseHelper.USERID + " = ? ", new String[] {driverid});
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false) {
                driverEntity = getDriverDetailEntity(cursor);
                cursor.moveToNext();
                break;
            }
        }
        cursor.close();
        return driverEntity;
    }
    /**
     * Get the delivery object
     * @param cursor
     * @return
     */
    private DriverEntity getDriverDetailEntity(Cursor cursor) {


        DriverEntity driverEntity = new DriverEntity();

        String fname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FNAME));
        String lname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LNAME));
        String phone_no = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE));
        String picture = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PICTURE));
        String address = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS));
        String bio = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BIO));
        String state = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATE));
        String country = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COUNTRY));
        String zipcode = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ZIPCODE));
        String token = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TOKEN));

        driverEntity.setFname(fname);
        driverEntity.setLname(lname);
        driverEntity.setPhoneno(phone_no);
        driverEntity.setPicture(picture);
        driverEntity.setAddress(address);
        driverEntity.setBio(bio);
        driverEntity.setState(state);
        driverEntity.setCountry(country);
        driverEntity.setZipcode(zipcode);
        driverEntity.setToken(token);

        return driverEntity;

    }


}
