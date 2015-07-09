package com.gojavas.tempola.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.widget.Toast;

import com.gojavas.tempola.constants.Constants;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gjs331 on 7/3/2015.
 */
public class Utility {


    /**
     * Show toast message
     * @param mContext
     * @param string
     */
    public static void showToast(Context mContext, String string) {
        Toast toast = Toast.makeText(mContext, string, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 100);
        toast.show();
    }


    /**
     * Save data in shared preferences
     * @param mContext
     * @param key
     * @param data
     */
    public static void saveToSharedPrefs(Context mContext, String key, String data) {
        final SharedPreferences taskForceData = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = taskForceData.edit();
        editor.putString(key, data);
        editor.commit();
    }


    /**
     * Get data from shared preferences
     * @param mContext
     * @param key
     * @return saved value
     */
    public static String getFromSharedPrefs(Context mContext, String key) {

        final SharedPreferences taskForceData = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        final String preData = taskForceData.getString(key, "");
        return preData;
    }

    /**
     * Check whether internet is connected or not
     * @param mContext
     * @return
     */
    public static boolean isInternetConnected(Context mContext) {
        final ConnectivityManager connection = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connection != null&& (connection.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)|| (connection.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        }
        return false;
    }

}
