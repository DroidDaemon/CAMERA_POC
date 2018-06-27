package com.example.droiddaemon.camera_poc.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sandeep.singh on 27-06-2018.
 */

public class NetworkUtil {

        public static final int TYPE_WIFI = 1;
        public static final int TYPE_MOBILE = 2;
        public static final int TYPE_NOT_CONNECTED = 0;

        private NetworkUtil() {
            throw new IllegalAccessError("Illegal access: Utility Class");
        }

        private static int getConnectivityStatus(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI;

                if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
            return TYPE_NOT_CONNECTED;
        }

        public static boolean isConnected(Context context) {
            return getConnectivityStatus(context) != TYPE_NOT_CONNECTED;
        }
}
