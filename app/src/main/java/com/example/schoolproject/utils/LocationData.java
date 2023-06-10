package com.example.schoolproject.utils;

public class LocationData {
    private double latitude;
    private double longitude;
    private String locationName;

    public LocationData(double latitude, double longitude, String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocationName() {
        return locationName;
    }
}

