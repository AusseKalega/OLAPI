package com.aussekalega.openlocator;

import android.location.Location;

class OpenLocator {
    private final Location location;

    public OpenLocator(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenLocator that = (OpenLocator) o;
        return location != null ? location.equals(that.location) : that.location == null;

    }

    @Override
    public int hashCode() {
        return location != null ? location.hashCode() : 0;
    }

    @Override
    public String toString() {
         return  location.getLatitude() +","+location.getLongitude();
    }
}