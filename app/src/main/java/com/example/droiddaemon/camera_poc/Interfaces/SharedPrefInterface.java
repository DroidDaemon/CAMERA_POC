package com.example.droiddaemon.camera_poc.Interfaces;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sandeep.singh on 27-06-2018.
 */

public interface SharedPrefInterface {

    void saveLatLng(Location location);

    LatLng getLatLng();
}
