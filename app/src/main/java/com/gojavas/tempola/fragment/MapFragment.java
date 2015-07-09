package com.gojavas.tempola.fragment;

import android.graphics.Color;
import android.location.Location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gojavas.tempola.R;
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

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by gjs331 on 7/6/2015.
 */
public class MapFragment extends Fragment implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {


    static boolean lastlocation=true;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    Button btnFusedLocation;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    GoogleMap googleMap;
    LatLng lastlatLng=null;
    LinearLayout linearLayout_notification;
    GridLayout gridLayout_trip;
    Button btn_accept,btn_rejected,btn_tripcompleted_driverwalkstarted,btn_call,btn_time,btn_distance;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }
        createLocationRequest();


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        View rootView = inflater.inflate(R.layout.activity_location_google_map, container, false);

        linearLayout_notification=(LinearLayout)rootView.findViewById(R.id.notification_layout);
        gridLayout_trip=(GridLayout)rootView.findViewById(R.id.gridlayout_trip);

        btn_accept=(Button)rootView.findViewById(R.id.map_accepted);
        btn_rejected=(Button)rootView.findViewById(R.id.map_rejected);

        btn_tripcompleted_driverwalkstarted=(Button)rootView.findViewById(R.id.map_trip_completed_driverwalkstarted);
        btn_call=(Button)rootView.findViewById(R.id.map_call);
        btn_distance=(Button)rootView.findViewById(R.id.map_distace);
        btn_time=(Button)rootView.findViewById(R.id.map_time);


        btn_accept.setOnClickListener(this);
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
        mGoogleApiClient.connect();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }
    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        addMarker();


    }


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

        if (lastlocation==true){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                    13));
            lastlocation=false;
        }

        Log.d(TAG, "Zoom done.............................");
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onClick(View v) {

    switch (v.getId()){

        case R.id.map_accepted:
                linearLayout_notification.setVisibility(View.GONE);
                gridLayout_trip.setVisibility(View.VISIBLE);

            if (googleMap!=null)
                googleMap.clear();

            break;

        case R.id.map_rejected:
            break;

        case R.id.map_trip_completed_driverwalkstarted:
            break;


        case R.id.map_call:
            break;


        case R.id.map_time:
            break;


        case R.id.map_distace:
            break;
    }

    }
}
