package com.aussekalega.openlocator.demo;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aussekalega.openlocator.demo.R;
import com.google.android.gms.location.LocationRequest;
import com.aussekalega.openlocator.OpenLocatorAppCompatActivity;
import com.aussekalega.openlocator.OpenLocatorRequest;
import com.aussekalega.openlocator.OpenLocatorRequestBuilder;

public class MainActivity extends OpenLocatorAppCompatActivity implements View.OnClickListener {

    private Button requestSingleLocationButton;
    private Button requestLocationUpdatesButton;
    private Button stopLocationUpdatesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews() {
        requestSingleLocationButton = (Button) findViewById(R.id.requestSingleLocationButton);
        requestLocationUpdatesButton = (Button) findViewById(R.id.requestLocationUpdatesButton);
        stopLocationUpdatesButton = (Button) findViewById(R.id.stopLocationUpdatesButton);

        requestSingleLocationButton.setOnClickListener(this);
        requestLocationUpdatesButton.setOnClickListener(this);
        stopLocationUpdatesButton.setOnClickListener(this);
    }


    @Override
    public void onLocationPermissionGranted() {
        showToast("Location permission granted");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationPermissionDenied() {
        showToast("Location permission denied");
    }

    @Override
    public void onLocationReceived(Location location) {
        showToast(location.getProvider() + "," + location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    public void onLocationProviderEnabled() {
        showToast("Location services are now ON");
    }

    @Override
    public void onLocationProviderDisabled() {
        showToast("Location services are still Off");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.requestSingleLocationButton: {
                LocationRequest locationRequest = new LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                        .setInterval(5000)
                        .setFastestInterval(5000);
                OpenLocatorRequest openLocatorRequest = new OpenLocatorRequestBuilder()
                        .setLocationRequest(locationRequest)
                        .setFallBackToLastLocationTime(3000)
                        .build();
                requestSingleLocationFix(openLocatorRequest);

            }
            break;
            case R.id.requestLocationUpdatesButton: {
                LocationRequest locationRequest = new LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                        .setInterval(5000)
                        .setFastestInterval(5000);
                OpenLocatorRequest openLocatorRequest = new OpenLocatorRequestBuilder()
                        .setLocationRequest(locationRequest)
                        .setFallBackToLastLocationTime(3000)
                        .build();
                requestLocationUpdates(openLocatorRequest);
            }
            break;
            case R.id.stopLocationUpdatesButton:
                stopLocationUpdates();
                break;
        }
    }
}
