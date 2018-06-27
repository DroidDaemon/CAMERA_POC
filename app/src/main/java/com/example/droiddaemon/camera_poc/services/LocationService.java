package com.example.droiddaemon.camera_poc.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.example.droiddaemon.camera_poc.utils.LogHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sandeep.singh on 27-06-2018.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOG = LocationService.class.getSimpleName();
    private final IBinder binder = new LocalBinder();
    private static final int INTERVAL = 60 * 1000;
    private static final int FAST_INTERVAL = 30 * 1000;
    private static final int MIN_DISTANCE = 25;
    private double pre_lattitude;
    private double pre_longitude;


    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        LogHelper.i(LOG, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogHelper.i(LOG, "onStartCommand");

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        return START_STICKY;
    }


    @Override
    public void onConnected(Bundle bundle) {
        LogHelper.i(LOG, "onConnected" + bundle);

        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (l != null) {
            LogHelper.i(LOG, "lat 1 " + l.getLatitude());
            LogHelper.i(LOG, "lng 2 " + l.getLongitude());
            ((CommonApplication) getApplication()).getCommonController().saveLatLng(l);
        }
        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        LogHelper.i(LOG, "onConnectionSuspended " + i);

    }

    @Override
    public void onLocationChanged(Location location) {
        LogHelper.i(LOG, "lat " + location.getLatitude());
        LogHelper.i(LOG, "lng " + location.getLongitude());
        LatLng latLng = ((CommonApplication) getApplication()).getCommonController().getLatLng();
        if (latLng == null)
            return;
        pre_lattitude = latLng.latitude;
        pre_longitude = latLng.longitude;
        ((CommonApplication) getApplication()).getCommonController().saveLatLng(location);
        ((CommonApplication) getApplication()).getCommonController().saveAccuracy(location.getAccuracy() + "");
        /*if(javelinServiceUtil.haversineDistance(pre_lattitude,pre_longitude,location.getLatitude(),location.getLongitude()) > MIN_DISTANCE){
            checkDistance(locationCheckCallBackListner);
        }*/ // Condition : Update device lat long on server if device is in moving state
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  stopLocationUpdate();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdate();
            mGoogleApiClient.disconnect();

        }

        LogHelper.i(LOG, "onDestroy");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        LogHelper.i(LOG, "onConnectionFailed ");
    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FAST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    public class LocalBinder extends Binder {
        public JavelinLocationService getService() {
            // Return this instance of MyService so clients can call public methods
            return JavelinLocationService.this;
        }
    }

    private void startLocationUpdate() {
        initLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }


}
