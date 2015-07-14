package com.gojavas.tempola.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.gojavas.tempola.activity.MainActivity;
import com.gojavas.tempola.application.TempolaApplication;
import com.gojavas.tempola.constants.Constants;
import com.gojavas.tempola.database.UserHelper;
import com.gojavas.tempola.entity.UserEntity;
import com.gojavas.tempola.utils.LocationUtils;
import com.gojavas.tempola.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gjs331 on 7/13/2015.
 */
public class SendLocation extends IntentService implements LocationUtils.OnLocationReceivedIn{

    Context context;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public SendLocation(String name) {

        super(name);

    }

    public SendLocation(){
        super("SendLocation");


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocationUtils locationUtils=new LocationUtils(this,this);
        locationUtils.googleApiConnect();

    }


    @Override
    public void onLocationReceived(Location location) {

        System.out.print("SendLocation = "+ location);

        if (location!=null)
        sendUpdatedLocation(location.getLatitude(),location.getLongitude());

    }


    private void sendUpdatedLocation(final double lat, final double longt){


        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                Constants.UPDATE_PROVIDER_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.print("SendLocation = "+ response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();


            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put(Constants.USERID,Utility.getFromSharedPrefs(getApplicationContext(),Constants.USERID));
                params.put(Constants.TOKEN, Utility.getFromSharedPrefs(getApplicationContext(),
                        Constants.TOKEN));
                params.put(Constants.LATITUDE, String.valueOf(lat));
                params.put(Constants.LONGITUDE,String.valueOf(longt));


                return params;
            }

        };

        TempolaApplication.getInstance().addToRequestQueue(jsonObjRequest);



    }


}
