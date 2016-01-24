package com.lesgens.dodocommute.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by marc-antoinehinse on 2016-01-24.
 */
public class Location {
    private String locationName;
    private LatLng location;


    public Location() {
    }

    public Location(LatLng location, String locationName) {
        this.location = location;
        this.locationName = locationName;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
