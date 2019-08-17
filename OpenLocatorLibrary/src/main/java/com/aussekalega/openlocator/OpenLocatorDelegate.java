package com.aussekalega.openlocator;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.aussekalega.openlocator.R;

class OpenLocatorDelegate {
    private static final int PERMISSIONS_REQUEST = 100;
    private static final int ENABLE_LOCATION_SERVICES_REQUEST = 101;
    private static final int GOOGLE_PLAY_SERVICES_ERROR_DIALOG = 102;


    private final Activity activity;
    private final OpenLocatorListener openLocatorListener;
    private final LocationBroadcastReceiver locationReceiver;
    private LocationManager mLocationManager;
    private int mLocationFetchMode;
    private LocationRequest mLocationRequest;
    private GoogleApiAvailability googleApiAvailability;
    private OpenLocatorRequest openLocatorRequest;

    OpenLocatorDelegate(Activity activity, OpenLocatorListener openLocatorListener) {
        this.activity = activity;
        this.openLocatorListener = openLocatorListener;
        locationReceiver = new LocationBroadcastReceiver(openLocatorListener);
    }


    private boolean isLocationEnabled() {
        return isGPSLocationEnabled()
                || isNetworkLocationEnabled();
    }

    private boolean isGPSLocationEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isNetworkLocationEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void openLocationSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, ENABLE_LOCATION_SERVICES_REQUEST);
    }

    void stopLocationUpdates() {
        Intent intent = new Intent(activity, LocationBgService.class);
        intent.setAction(AppConstants.ACTION_LOCATION_FETCH_STOP);
        activity.startService(intent);
    }

    private void isProperRequest(OpenLocatorRequest openLocatorRequest) {
        if (openLocatorRequest == null)
            throw new IllegalStateException("openLocatorRequest can't be null");

        if (openLocatorRequest.locationRequest == null)
            throw new IllegalStateException("locationRequest can't be null");
        this.openLocatorRequest = openLocatorRequest;
    }

    private void startLocationBGService(LocationRequest locationRequest, long fallBackToLastLocationTime) {
        if (!isLocationEnabled())
            showLocationServicesRequireDialog();
        else {
            Intent intent = new Intent(activity, LocationBgService.class);
            intent.setAction(AppConstants.ACTION_LOCATION_FETCH_START);
            intent.putExtra(IntentKey.LOCATION_REQUEST, locationRequest);
            intent.putExtra(IntentKey.LOCATION_FETCH_MODE, mLocationFetchMode);
            intent.putExtra(IntentKey.FALLBACK_TO_LAST_LOCATION_TIME, fallBackToLastLocationTime);
            activity.startService(intent);
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void showPermissionRequireDialog() {
        String title = TextUtils.isEmpty(openLocatorRequest.locationPermissionDialogTitle) ? activity.getString(R.string.location_permission_dialog_title) : openLocatorRequest.locationPermissionDialogTitle;
        String message = TextUtils.isEmpty(openLocatorRequest.locationPermissionDialogMessage) ? activity.getString(R.string.location_permission_dialog_message) : openLocatorRequest.locationPermissionDialogMessage;
        String negativeButtonTitle = TextUtils.isEmpty(openLocatorRequest.locationPermissionDialogNegativeButtonText) ? activity.getString(android.R.string.cancel) : openLocatorRequest.locationPermissionDialogNegativeButtonText;
        String positiveButtonTitle = TextUtils.isEmpty(openLocatorRequest.locationPermissionDialogPositiveButtonText) ? activity.getString(android.R.string.ok) : openLocatorRequest.locationPermissionDialogPositiveButtonText;
        new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(negativeButtonTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openLocatorListener.onLocationPermissionDenied();
                    }
                })
                .setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission();
                    }
                }).create().show();
    }

    private void showLocationServicesRequireDialog() {
        String title = TextUtils.isEmpty(openLocatorRequest.locationSettingsDialogTitle) ? activity.getString(R.string.location_services_off) : openLocatorRequest.locationSettingsDialogTitle;
        String message = TextUtils.isEmpty(openLocatorRequest.locationSettingsDialogMessage) ? activity.getString(R.string.open_location_settings) : openLocatorRequest.locationSettingsDialogMessage;
        String negativeButtonText = TextUtils.isEmpty(openLocatorRequest.locationSettingsDialogNegativeButtonText) ? activity.getString(android.R.string.cancel) : openLocatorRequest.locationSettingsDialogNegativeButtonText;
        String positiveButtonText = TextUtils.isEmpty(openLocatorRequest.locationSettingsDialogPositiveButtonText) ? activity.getString(android.R.string.ok) : openLocatorRequest.locationSettingsDialogPositiveButtonText;
        new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openLocatorListener.onLocationProviderDisabled();
                    }
                })
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openLocationSettings();
                    }
                })
                .create().show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
    }


    private void requestLocation(LocationRequest locationRequest, int locationMode) {
        if (isGoogleServiceAvailable()) {
            mLocationFetchMode = locationMode;
            mLocationRequest = locationRequest;
            checkForPermissionAndRequestLocation(locationRequest);
        } else
            showGooglePlayServicesErrorDialog();
    }

    private void checkForPermissionAndRequestLocation(LocationRequest locationRequest) {
        if (!hasLocationPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION))
                showPermissionRequireDialog();
            else
                requestPermission();
        } else
            startLocationBGService(locationRequest, openLocatorRequest.fallBackToLastLocationTime);
    }

    private void unregisterLocationBroadcastReceiver() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(locationReceiver);
    }

    private void registerLocationBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.INTENT_LOCATION_RECEIVED);
        LocalBroadcastManager.getInstance(activity).registerReceiver(locationReceiver, intentFilter);
    }

    private boolean isGoogleServiceAvailable() {
        return googleApiAvailability.isGooglePlayServicesAvailable(activity) == ConnectionResult.SUCCESS;
    }

    private void showGooglePlayServicesErrorDialog() {
        int errorCode = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (googleApiAvailability.isUserResolvableError(errorCode))
            googleApiAvailability.getErrorDialog(activity, errorCode, GOOGLE_PLAY_SERVICES_ERROR_DIALOG).show();
    }


    void onCreate() {
        mLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        googleApiAvailability = GoogleApiAvailability.getInstance();
        registerLocationBroadcastReceiver();
    }

    void onActivityResult(int requestCode) {
        switch (requestCode) {
            case ENABLE_LOCATION_SERVICES_REQUEST:
                if (isLocationEnabled()) {
                    requestLocation(mLocationRequest, mLocationFetchMode);
                    openLocatorListener.onLocationProviderEnabled();
                } else
                    openLocatorListener.onLocationProviderDisabled();
                break;
        }
    }

    void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation(mLocationRequest, mLocationFetchMode);
                    openLocatorListener.onLocationPermissionGranted();
                } else
                    openLocatorListener.onLocationPermissionDenied();
                break;
        }
    }

    void onDestroy() {
        stopLocationUpdates();
        unregisterLocationBroadcastReceiver();
    }

    Location getLastKnownLocation() {
        return PreferenceUtil.getInstance(activity).getLastKnownLocation();
    }

    void requestLocationUpdates(OpenLocatorRequest openLocatorRequest) {
        isProperRequest(openLocatorRequest);
        requestLocation(openLocatorRequest.locationRequest, AppConstants.CONTINUOUS_LOCATION_UPDATES);
    }

    void requestSingleLocationFix(OpenLocatorRequest openLocatorRequest) {
        isProperRequest(openLocatorRequest);
        requestLocation(openLocatorRequest.locationRequest, AppConstants.SINGLE_FIX);
    }
}