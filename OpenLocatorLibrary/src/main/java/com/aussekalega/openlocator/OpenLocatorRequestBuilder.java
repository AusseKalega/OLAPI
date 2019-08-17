package com.aussekalega.openlocator;

import com.google.android.gms.location.LocationRequest;

public class OpenLocatorRequestBuilder {
    private LocationRequest locationRequest;
    private String locationSettingsDialogTitle;
    private String locationSettingsDialogMessage;
    private String locationSettingsDialogPositiveButtonText;
    private String locationSettingsDialogNegativeButtonText;
    private String locationPermissionDialogTitle;
    private String locationPermissionDialogMessage;
    private String locationPermissionDialogPositiveButtonText;
    private String locationPermissionDialogNegativeButtonText;
    private long fallBackToLastLocationTime;

    public OpenLocatorRequestBuilder setLocationRequest(LocationRequest locationRequest) {
        this.locationRequest = locationRequest;
        return this;
    }

    public OpenLocatorRequestBuilder setLocationSettingsDialogTitle(String locationSettingsDialogTitle) {
        this.locationSettingsDialogTitle = locationSettingsDialogTitle;
        return this;
    }

    public OpenLocatorRequestBuilder setLocationSettingsDialogMessage(String locationSettingsDialogMessage) {
        this.locationSettingsDialogMessage = locationSettingsDialogMessage;
        return this;
    }

    public OpenLocatorRequestBuilder setLocationSettingsDialogPositiveButtonText(String locationSettingsDialogPositiveButtonText) {
        this.locationSettingsDialogPositiveButtonText = locationSettingsDialogPositiveButtonText;
        return this;
    }

    public OpenLocatorRequestBuilder setLocationSettingsDialogNegativeButtonText(String locationSettingsDialogNegativeButtonText) {
        this.locationSettingsDialogNegativeButtonText = locationSettingsDialogNegativeButtonText;
        return this;
    }

    public OpenLocatorRequestBuilder setLocationPermissionDialogTitle(String locationPermissionDialogTitle) {
        this.locationPermissionDialogTitle = locationPermissionDialogTitle;
        return this;
    }

    public OpenLocatorRequestBuilder setLocationPermissionDialogMessage(String locationPermissionDialogMessage) {
        this.locationPermissionDialogMessage = locationPermissionDialogMessage;
        return this;
    }

    public OpenLocatorRequestBuilder setLocationPermissionDialogPositiveButtonText(String locationPermissionDialogPositiveButtonText) {
        this.locationPermissionDialogPositiveButtonText = locationPermissionDialogPositiveButtonText;
        return this;
    }

    public OpenLocatorRequestBuilder setLocationPermissionDialogNegativeButtonText(String locationPermissionDialogNegativeButtonText) {
        this.locationPermissionDialogNegativeButtonText = locationPermissionDialogNegativeButtonText;
        return this;
    }

    public OpenLocatorRequestBuilder setFallBackToLastLocationTime(long fallBackToLastLocationTime) {
        this.fallBackToLastLocationTime = fallBackToLastLocationTime;
        return this;
    }

    public OpenLocatorRequest build() {
        return new OpenLocatorRequest(locationRequest, locationSettingsDialogTitle, locationSettingsDialogMessage, locationSettingsDialogPositiveButtonText, locationSettingsDialogNegativeButtonText, locationPermissionDialogTitle, locationPermissionDialogMessage, locationPermissionDialogPositiveButtonText, locationPermissionDialogNegativeButtonText, fallBackToLastLocationTime);
    }
}