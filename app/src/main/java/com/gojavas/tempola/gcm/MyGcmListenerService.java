/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gojavas.tempola.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.gojavas.tempola.R;
import com.gojavas.tempola.activity.MainActivity;
import com.gojavas.tempola.application.TempolaApplication;
import com.gojavas.tempola.constants.Constants;
import com.gojavas.tempola.database.DogRequestHelper;
import com.gojavas.tempola.entity.DogRequestEntity;
import com.gojavas.tempola.utils.Utility;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    String requestid="";
    int time_left=0;
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        final String ticker=data.getString("tickerText");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "Ticker: " + ticker);


        try {
            JSONObject jsonObject=new JSONObject(ticker);
            final String requestid=jsonObject.getString("request_id");
            final String time=jsonObject.getString("request_time_start");



            String url=Constants.CHECK_REQUEST_STATUS+Constants.USERID+"="+Utility
                    .getFromSharedPrefs(this,Constants.USERID)+"&"+Constants.TOKEN+"="+Utility.getFromSharedPrefs
                    (this, Constants.TOKEN)+"&"+Constants.REQUEST_ID+"="+requestid;

            StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.print(response);

                            try {
                                JSONObject jsonObject1=new JSONObject(response);
                                boolean success=jsonObject1.getBoolean("success");
                                if (success){

/*
                                    "request_id": 477,
                                            "time_left_to_respond": 5597,
                                            "request_data": {
                                        "owner": {
                                            "name": "Anshul Goel",
                                                    "curr_address": "353, Udyog Vihar IV, Phase V, Sector 19, Gurgaon, Haryana 122016, India",
                                                    "picture": "http://tempola.in/api/public/uploads/b377af272362f126e99d045541138c127430f38e.jpg",
                                                    "phone": "7042192820",
                                                    "address": "profile Sector 18, Gurgaon",
                                                    "latitude": 28.5027512,
                                                    "longitude": 77.0809093,
                                                    "rating": "4.0000",
                                                    "num_rating": 1
                                        },
*/

                                    JSONArray jsonArray=jsonObject1.getJSONArray("incoming_requests");
                                    JSONObject jsonObject2=jsonArray.getJSONObject(0);
                                   String requestid=jsonObject2.getInt("request_id")+"";
                                    time_left=jsonObject2.getInt("time_left_to_respond");

                                    JSONObject jsonObject4=jsonObject2.getJSONObject
                                            ("request_data");
                                    JSONObject jsonObject3=jsonObject4.getJSONObject("owner");
                                    String name=jsonObject3.getString("name");
                                    String cuur_address=jsonObject3.getString("curr_address");
                                    String picture=jsonObject3.getString("picture");
                                    String phone=jsonObject3.getString("phone");
                                    String address=jsonObject3.getString("address");
                                    double latitude=jsonObject3.getDouble("latitude");
                                    double logitude =jsonObject3.getDouble("longitude");
                                    String rating =jsonObject3.getString("rating");
                                    int num_rating=jsonObject3.getInt("num_rating");


                                    DogRequestEntity dogRequestEntity=new DogRequestEntity();
                                    dogRequestEntity.setRequestid(requestid);
                                    dogRequestEntity.setTime_left_to_respond(time_left);
                                    dogRequestEntity.setName(name);
                                    dogRequestEntity.setCurr_address(cuur_address);
                                    dogRequestEntity.setPicture(picture);
                                    dogRequestEntity.setPhone(phone);
                                    dogRequestEntity.setAddress(address);
                                    dogRequestEntity.setLatitude(latitude);
                                    dogRequestEntity.setLongitude(logitude);
                                    dogRequestEntity.setRating(rating);
                                    dogRequestEntity.setNum_rating(num_rating);

                                    DogRequestHelper.getInstance().insertOrUpdate(dogRequestEntity);

                                    sendNotification(ticker);


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                   /*     MyFunctions.croutonAlert(LoginActivity.this,
                                MyFunctions.parseVolleyError(error));
                        loading.setVisibility(View.GONE);
                   */ }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
//
                    return params;
                }

            };

            TempolaApplication.getInstance().addToRequestQueue(jsonObjRequest);





        } catch (JSONException e) {
            e.printStackTrace();
        }





        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */


    private void sendNotification(String message) {

        Utility.saveToSharedPrefs(this, Constants.REQUEST_ID, requestid);
        Utility.saveToSharedPrefs(this, Constants.REQUEST_ID_TIME,time_left+"");
        Utility.saveToSharedPrefs(this, Constants.CURRENT_STATE, Constants.REQUEST_ARRIVED);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_cast_dark)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}