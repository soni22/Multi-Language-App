package com.gojavas.tempola.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gjs331 on 7/13/2015.
 */
public class LocationUtils implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    public OnLocationReceivedIn onLocationReceivedIn;

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    /**
     * Interface for getting location on Changed Location
     */

    public interface OnLocationReceivedIn {
        public void onLocationReceived(Location location);
    }


    public LocationUtils(Context context, OnLocationReceivedIn listener){

        createLocationRequest();
        googleApiClient(context);
        onLocationReceivedIn = listener;


    }



    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setInterval(INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


protected void googleApiClient(Context context){

    mGoogleApiClient = new GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();

}

    @Override
    public void onLocationChanged(Location location) {

       if (location != null ) {
           System.out.print("location utils ="+location);
            onLocationReceivedIn.onLocationReceived(location);
        }

    }
    public void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d("Location Stopped", "Location update stopped .......................");

    }

    public void startLocationUpdates() {

        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d("Location Started", "Location update started ..............: ");

    }

    public  void googleApiConnect(){
        mGoogleApiClient.connect();
    }

    public  boolean isGoogleApiConnect(){
      return  mGoogleApiClient.isConnected();
    }


}
