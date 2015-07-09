package com.gojavas.tempola.database;

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


}
