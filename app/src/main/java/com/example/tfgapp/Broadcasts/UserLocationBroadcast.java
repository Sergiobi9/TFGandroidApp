package com.example.tfgapp.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

public class UserLocationBroadcast extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.example.googlemaps_background.UPDATE_LOCATION";
    private final String TAG = "UserLocationBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);

                if (result != null) {
                    Location location = result.getLastLocation();
                    Log.d(TAG, location.toString());
                }
            }
        }
    }
}
