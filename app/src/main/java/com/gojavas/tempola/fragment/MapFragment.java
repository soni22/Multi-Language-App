package com.gojavas.tempola.fragment;

import android.app.ProgressDialog;
import android.location.Location;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.gojavas.tempola.R;
import com.gojavas.tempola.application.TempolaApplication;
import com.gojavas.tempola.constants.Constants;
import com.gojavas.tempola.database.DogRequestHelper;
import com.gojavas.tempola.entity.DogRequestEntity;
import com.gojavas.tempola.utils.LocationUtils;
import com.gojavas.tempola.utils.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gjs331 on 7/6/2015.
 */
public class MapFragment extends Fragment implements View.OnClickListener, LocationUtils.OnLocationReceivedIn{


    static boolean lastlocation=true;
    String msg;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    Button btnFusedLocation;
    TextView tvLocation, btn_time, btn_distance,tv_userName,tv_userAddress;
    ImageView iv_userImage;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    GoogleMap googleMap;
    LatLng lastlatLng=null;
    LinearLayout linearLayout_accept_reject;
    RelativeLayout mMapBottomLayout,userDetailLayout;
    Button btn_accept,btn_rejected,btn_tripcompleted_driverwalkstarted;
    FloatingActionButton btn_call;
    ProgressDialog progressDialog;
    LocationUtils  locationUtils;
    Marker driver_marker,client_marker;

    private final static int DELAY = 10000;
    private final Handler handler = new Handler();

    private final Timer timer = new Timer();
    private final TimerTask requestTimerTask = new TimerTask() {
//        private int counter = 0;
        public void run() {

            getAllRequests();
//            handler.post(new Runnable() {
//                public void run() {
//                    Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
//                }
//            });
//            if(++counter == 4) {
//                timer.cancel();
//            }
        }
    };


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setInterval(INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog=new ProgressDialog(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }
        //createLocationRequest();

        locationUtils=new LocationUtils(getActivity(), this);
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();

        View rootView = inflater.inflate(R.layout.activity_location_google_map, container, false);

        userDetailLayout=(RelativeLayout)rootView.findViewById(R.id.user_detail_layout);
        tv_userName=(TextView)rootView.findViewById(R.id.user_name);
        tv_userAddress=(TextView)rootView.findViewById(R.id.user_address);
        iv_userImage=(ImageView)rootView.findViewById(R.id.user_image);

        linearLayout_accept_reject=(LinearLayout)rootView.findViewById(R.id.notification_layout);
        mMapBottomLayout=(RelativeLayout)rootView.findViewById(R.id.map_bottom_layout);

        btn_accept=(Button)rootView.findViewById(R.id.map_accepted);
        btn_rejected=(Button)rootView.findViewById(R.id.map_rejected);

        btn_tripcompleted_driverwalkstarted=(Button)rootView.findViewById(R.id.map_trip_completed_driverwalkstarted);
        btn_call=(FloatingActionButton)rootView.findViewById(R.id.map_call);
        btn_distance=(TextView)rootView.findViewById(R.id.map_distance);
        btn_time=(TextView)rootView.findViewById(R.id.map_time);


        btn_accept.setOnClickListener(this);
        btn_rejected.setOnClickListener(this);


        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment fm = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        googleMap = fm.getMap();
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        viewVisibility(Utility.getFromSharedPrefs(getActivity(), Constants.CURRENT_STATE));


    }





    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
//        mGoogleApiClient.connect();
locationUtils.googleApiConnect();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
        if (locationUtils.isGoogleApiConnect()){
            locationUtils.startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        System.out.print(msg);
        }

        if (!Utility.getFromSharedPrefs(getActivity(),Constants.REQUEST_ID).equalsIgnoreCase
                (Constants.NO_REQUEST_ID)) {
//                        startquestReTimer();
            viewVisibility(Utility.getFromSharedPrefs(getActivity(),Constants.CURRENT_STATE));
        }



    }



    @Override
    public void onPause() {
        super.onPause();
//        stopLocationUpdates();

        locationUtils.stopLocationUpdates();

    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
//        mGoogleApiClient.disconnect();
//        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }
   /* @Override
    public void onLocationChanged(Location location) {

        if(location !=null){
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        addMarker();
        }



    }*/


    private void addMarker() {
        MarkerOptions options = new MarkerOptions();

        // following four lines requires 'Google Maps Android API Utility Library'
        // https://developers.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info


//        IconGenerator iconFactory = new IconGenerator(getActivity());
//        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
//        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
//        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.jennifer);

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        options.icon(icon);
        if (lastlocation==true){
            lastlatLng=currentLatLng;

        }
        options.position(currentLatLng);
//googleMap.clear();
        driver_marker = googleMap.addMarker(options);
        long atTime = mCurrentLocation.getTime();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
        driver_marker.setTitle(mLastUpdateTime);

//        googleMap	.addPolyline((new PolylineOptions())
//                .add(lastlatLng, currentLatLng).width(5).color(Color.BLUE)
//                .geodesic(true));

        lastlatLng=currentLatLng;
        Log.d(TAG, "Marker added.............................");

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                13));

        if (lastlocation==true){
            lastlocation=false;
        }

        Log.d(TAG, "Zoom done.............................");
    }


/*
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
//        startLocationUpdates();
        locationUtils.startLocationUpdates();
    }
*/

   /* protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }*/


/*
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

*/

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }

    /*
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }*/

    @Override
    public void onClick(View v) {

    switch (v.getId()){

        case R.id.map_accepted:

            Map<String, String> params = new HashMap<String, String>();

            params.put("token",Utility.getFromSharedPrefs(getActivity(),Constants.TOKEN));
            params.put("id", Utility.getFromSharedPrefs(getActivity(),Constants.USERID));
            params.put("request_id",Utility.getFromSharedPrefs(getActivity(),Constants.REQUEST_ID));
            params.put("accepted", Constants.ACCEPTED_STATE);

            SendDatatoServer(Constants.REQUEST_RESPONSE_URL,params,Constants.ACCEPTED_STATE);

            break;

        case R.id.map_rejected:


            params = new HashMap<String, String>();
            params.put("token",Utility.getFromSharedPrefs(getActivity(),Constants.TOKEN));
            params.put("id", Utility.getFromSharedPrefs(getActivity(),Constants.USERID));
            params.put("request_id", Utility.getFromSharedPrefs(getActivity(),Constants.REQUEST_ID));
            params.put("accepted", Constants.REJECTED_STATE);

            SendDatatoServer(Constants.REQUEST_RESPONSE_URL,params,Constants.REJECTED_STATE);

            break;

        case R.id.map_trip_completed_driverwalkstarted:
            break;


        case R.id.map_call:
            break;


        case R.id.map_time:
            break;


        case R.id.map_distance:
            break;
    }

    }



    public void showProgrss(String message){
        if (progressDialog!=null && !progressDialog.isShowing()){
            progressDialog.show();
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
        }
    }


    public void hideProgrss(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }


    private void SendDatatoServer(String url, final Map<String,String> map, final String requestAction){


        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.print(response);

                        hideProgrss();

                        responseActtion(requestAction);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgrss();
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();

                Utility.showToast(getActivity(),"Server Error");

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=map;

                return params;
            }

        };

        TempolaApplication.getInstance().addToRequestQueue(jsonObjRequest);
    }

    public  void responseActtion(String responseAction){

        switch (responseAction){

            case Constants.REJECTED_STATE:

                linearLayout_accept_reject.setVisibility(View.GONE);
                userDetailLayout.setVisibility(View.GONE);
                Utility.saveToSharedPrefs(getActivity(), Constants.CURRENT_STATE, Constants.NO_REQUEST_ID);

                break;
            case Constants.ACCEPTED_STATE:

                linearLayout_accept_reject.setVisibility(View.GONE);
                userDetailLayout.setVisibility(View.GONE);
                mMapBottomLayout.setVisibility(View.VISIBLE);

                if (googleMap!=null)
                    googleMap.clear();

                Utility.saveToSharedPrefs(getActivity(),Constants.CURRENT_STATE,Constants.ACCEPTED_STATE);

                break;
            case Constants.DRIVER_ARRIVED_STATE:
                break;
            case Constants.TRIP_STARTRED_STATE:
                break;
            case Constants.TRIP_COMPLETED_STATE:
                break;
            case Constants.PROVIDER_RATING_STATE:
                break;




        }
    }

    @Override
    public void onLocationReceived(Location location) {

        Log.d(TAG, "Firing onLocationChanged..............................................");
  if (location!=null){
      mCurrentLocation = location;
      mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

      addMarker();

  }


    }


    private void startquestReTimer(){

        timer.scheduleAtFixedRate(requestTimerTask, 0, 30*1000);
    }

    private void stopRequestTimer(){
        timer.cancel();
    }


    private void viewVisibility(String responseAction){

        switch (responseAction){

            case Constants.NO_REQUEST_ID:

                userDetailLayout.setVisibility(View.GONE);
                btn_accept.setVisibility(View.GONE);
                btn_rejected.setVisibility(View.GONE);

                break;


            case Constants.REQUEST_ARRIVED:

                userDetailLayout.setVisibility(View.VISIBLE);
                btn_accept.setVisibility(View.VISIBLE);
                btn_rejected.setVisibility(View.VISIBLE);

                DogRequestEntity dogRequestEntity=DogRequestHelper.getInstance().getDogRequest
                        (Utility
                    .getFromSharedPrefs
                        (getActivity(),Constants.REQUEST_ID));
                tv_userAddress.setText(dogRequestEntity.getCurr_address());
                tv_userName.setText(dogRequestEntity.getName());

                ImageLoader.getInstance().displayImage(dogRequestEntity.getPicture(),iv_userImage);



                break;

            case Constants.REJECTED_STATE:
                break;

            case Constants.ACCEPTED_STATE:

                linearLayout_accept_reject.setVisibility(View.GONE);
                userDetailLayout.setVisibility(View.GONE);
                mMapBottomLayout.setVisibility(View.VISIBLE);

                break;

            case Constants.DRIVER_ARRIVED_STATE:
                break;

            case Constants.TRIP_STARTRED_STATE:
                break;

            case Constants.TRIP_COMPLETED_STATE:
                break;

            case Constants.PROVIDER_RATING_STATE:
                break;




        }
    }



    private  void  getAllRequests(){

        String url=Constants.GET_ALL_REQUESTS+Utility.getFromSharedPrefs(getActivity(),Constants
                .USERID)+"&"+Utility.getFromSharedPrefs(getActivity(),Constants
                .TOKEN);

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
                             int   time_left=jsonObject2.getInt("time_left_to_respond");

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

                                Utility.saveToSharedPrefs(getActivity(), Constants.REQUEST_ID, requestid);
                                Utility.saveToSharedPrefs(getActivity(), Constants.CURRENT_STATE, Constants
                                        .REQUEST_ARRIVED);

                                stopRequestTimer();

                                viewVisibility(Utility.getFromSharedPrefs(getActivity(), Constants.CURRENT_STATE));

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

    }

}
