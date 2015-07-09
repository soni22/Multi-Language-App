package com.gojavas.tempola.application;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by gjs331 on 7/3/2015.
 */
public class TempolaApplication extends Application {

    public static final String TAG = TempolaApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static TempolaApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public TempolaApplication() {
        instance = this;
    }

    public static TempolaApplication getInstance() {
        if (instance == null)
            throw new IllegalStateException();
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }



}
