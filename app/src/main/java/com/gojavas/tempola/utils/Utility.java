package com.gojavas.tempola.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import com.gojavas.tempola.application.TempolaApplication;

/**
 * Created by gjs331 on 7/3/2015.
 */
public class Utility {

    /**
     * Get device id
     * @return
     */
    public static String getDeviceId() {
        Context context = TempolaApplication.getInstance();
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }


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
}
