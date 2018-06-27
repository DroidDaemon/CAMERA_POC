package com.example.droiddaemon.camera_poc.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.example.droiddaemon.camera_poc.Interfaces.SharedPrefInterface;
import com.example.droiddaemon.camera_poc.utils.Constants;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sandeep.singh on 27-06-2018.
 */

public class SharedPrefService implements SharedPrefInterface{

    private static final String TAG = SharedPrefService.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    Context context;

    public SharedPrefService(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void saveLatLng(Location location) {
        SharedPreferences.Editor editor = SharedPrefService.edit();
        editor.putString(Constants.SHARED_PREF_LAT, String.valueOf(location.getLatitude()));
        editor.putString(Constants.SHARED_PREF_LNG, String.valueOf(location.getLongitude()));
        editor.commit();
    }

    @Override
    public LatLng getLatLng() {
        return null;
    }
}
