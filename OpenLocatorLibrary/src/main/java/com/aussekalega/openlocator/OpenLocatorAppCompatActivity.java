package com.aussekalega.openlocator;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;


public abstract class OpenLocatorAppCompatActivity extends AppCompatActivity implements OpenLocatorListener {
    private OpenLocatorDelegate openLocatorDelegate;

    protected Location getLastKnownLocation() {
        return openLocatorDelegate.getLastKnownLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openLocatorDelegate = new OpenLocatorDelegate(this,this);
        openLocatorDelegate.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        openLocatorDelegate.onActivityResult(requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        openLocatorDelegate.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        openLocatorDelegate.onDestroy();
    }

    protected void requestLocationUpdates(OpenLocatorRequest openLocatorRequest) {
        openLocatorDelegate.requestLocationUpdates(openLocatorRequest);
    }


    protected void requestSingleLocationFix(OpenLocatorRequest openLocatorRequest) {
        openLocatorDelegate.requestSingleLocationFix(openLocatorRequest);
    }

    protected void stopLocationUpdates() {
        openLocatorDelegate.stopLocationUpdates();
    }
}