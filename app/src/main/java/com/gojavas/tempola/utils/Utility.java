package com.gojavas.tempola.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

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
}
