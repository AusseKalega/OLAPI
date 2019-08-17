# OLAPI
[![](https://jitpack.io/v/AusseKalega/OLAPI.svg)](https://jitpack.io/#AusseKalega/OLAPI)
## Getting started

In your `build.gradle`:

Add the following maven{} line to your **PROJECT** build.gradle file

```
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }   // add this line
		}
	}
```

**com.google.android.gms:play-services-location** dependency also needs to be added like this

**x.x.x** can be replaced with google play service version your app is using [versions information available here](https://developers.google.com/android/guides/releases) 

```gradle
 dependencies {
    compile 'com.github.AusseKalega:OLAPI:1.0'
    compile "com.google.android.gms:play-services-location:x.x.x"
 }
```

Extend your `Activity` from `OpenLocatorAppCompatActivity` or `OpenLocatorActivity`:

*Create location request according to your needs*

```java
LocationRequest locationRequest = new LocationRequest()
        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        .setInterval(5000)
        .setFastestInterval(5000);
```                        
*Create OpenLocator request, and set locationRequest created*
```java
OpenLocatorRequest openLocatorRequest = new OpenLocatorRequestBuilder()
        .setLocationRequest(locationRequest)
        .setFallBackToLastLocationTime(3000)
        .build();
}
```
**Request Single location update like this**
```java
requestSingleLocationFix(openLocatorRequest);
```
**Or Request Multiple location updates like this**
```java
requestLocationUpdates(openLocatorRequest);
```

**You're good to go!**, You will get below callbacks now in your activity

```java
    @Override
    public void onLocationPermissionGranted() {
    }

    @Override
    public void onLocationPermissionDenied() {
    }

    @Override
    public void onLocationReceived(Location location) {
    }

    @Override
    public void onLocationProviderEnabled() {
    }

    @Override
    public void onLocationProviderDisabled() {
    }
```

**Additional Options**

Specify what messages you want to show to user using *EasyLocationRequestBuilder*
```java
EasyLocationRequest openLocatorRequest = new OpenLocatorRequestBuilder()
.setLocationRequest(locationRequest)
.setLocationPermissionDialogTitle(getString(R.string.location_permission_dialog_title))
.setLocationPermissionDialogMessage(getString(R.string.location_permission_dialog_message))
.setLocationPermissionDialogNegativeButtonText(getString(R.string.not_now))
.setLocationPermissionDialogPositiveButtonText(getString(R.string.yes))
.setLocationSettingsDialogTitle(getString(R.string.location_services_off))
.setLocationSettingsDialogMessage(getString(R.string.open_location_settings))
.setLocationSettingsDialogNegativeButtonText(getString(R.string.not_now))
.setLocationSettingsDialogPositiveButtonText(getString(R.string.yes))
.build();
```

## Library License

    Copyright 2019 Ausses Kalega

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
