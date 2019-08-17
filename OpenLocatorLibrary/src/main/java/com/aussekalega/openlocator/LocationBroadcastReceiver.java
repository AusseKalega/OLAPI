package com.aussekalega.openlocator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

class LocationBroadcastReceiver extends BroadcastReceiver {
    private final OpenLocatorListener openLocatorListener;

    public LocationBroadcastReceiver(OpenLocatorListener openLocatorListener) {
        this.openLocatorListener = openLocatorListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AppConstants.INTENT_LOCATION_RECEIVED)) {
            Location location = intent.getParcelableExtra(IntentKey.LOCATION);
            openLocatorListener.onLocationReceived(location);
        }
    }
}