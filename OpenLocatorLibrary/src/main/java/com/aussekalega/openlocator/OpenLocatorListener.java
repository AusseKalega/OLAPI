package com.aussekalega.openlocator;

import android.location.Location;

interface OpenLocatorListener {
    void onLocationPermissionGranted();
    void onLocationPermissionDenied();
    void onLocationReceived(Location location);
    void onLocationProviderEnabled();
    void onLocationProviderDisabled();
}