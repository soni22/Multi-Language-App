package com.gojavas.tempola.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.gojavas.tempola.database.UserHelper;
import com.gojavas.tempola.entity.UserEntity;
import com.gojavas.tempola.utils.LocationUtils;
import com.gojavas.tempola.utils.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gjs331 on 7/6/2015.
 */
public class MapFragment extends Fragment implements View.OnClickListener, LocationUtils.OnLocationReceivedIn{


    static boolean lastlocation=true;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    Button btnFusedLocation;
    TextView tvLocation, btn_time, btn_distance;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    GoogleMap googleMap;
    LatLng lastlatLng=null;
    LinearLayout linearLayout_accept_reject;
    RelativeLayout mMapBottomLayout;
    Button btn_accept,btn_rejected,btn_tripcompleted_driverwalkstarted;
    FloatingActionButton btn_call;
    ProgressDialog progressDialog;
    LocationUtils  locationUtils;


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
        IconGenerator iconFactory = new IconGenerator(getActivity());
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        if (lastlocation==true){
            lastlatLng=currentLatLng;

        }
        options.position(currentLatLng);
//googleMap.clear();
        Marker mapMarker = googleMap.addMarker(options);
        long atTime = mCurrentLocation.getTime();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
        mapMarker.setTitle(mLastUpdateTime);

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

                linearLayout_accept_reject.setVisibility(View.GONE);
                mMapBottomLayout.setVisibility(View.VISIBLE);


            if (googleMap!=null)
                googleMap.clear();

            Map<String, String> params = new HashMap<String, String>();

            params.put("token",Utility.getFromSharedPrefs(getActivity(),Constants.TOKEN));
            params.put("id", Utility.getFromSharedPrefs(getActivity(),Constants.USERID));
            params.put("request_id", "150");
            params.put("accepted", Constants.ACCEPTED_STATE);

            SendDatatoServer(Constants.REQUEST_RESPONSE_URL,params,Constants.ACCEPTED_STATE);

//                params.put("device_type", "android");
//                params.put("device_token", Utility.getDeviceId());
//                params.put("login_by", "manual");

            break;

        case R.id.map_rejected:


            params = new HashMap<String, String>();

            params.put("token",Utility.getFromSharedPrefs(getActivity(),Constants.TOKEN));
            params.put("id", Utility.getFromSharedPrefs(getActivity(),Constants.USERID));
            params.put("request_id", "150");
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
////                        params.put("username", etUname.getText().toString().trim());
////                        params.put("password", etPass.getText().toString().trim());
//
//                params.put("email", str_email);
//                params.put("password", str_password);
//                params.put("device_type", "android");
//                params.put("device_token", Utility.getDeviceId());
//                params.put("login_by", "manual");
//
                return params;
            }

        };

        TempolaApplication.getInstance().addToRequestQueue(jsonObjRequest);
    }

    public  void responseActtion(String responseAction){

        switch (responseAction){

            case Constants.REJECTED_STATE:
                break;
            case Constants.ACCEPTED_STATE:
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
}
