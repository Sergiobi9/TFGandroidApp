package com.example.tfgapp.Entities.Concert;

import com.google.gson.annotations.SerializedName;

public class ConcertLocationReduced {

    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("placeName")
    private String placeName;
    @SerializedName("address")
    private String address;
    @SerializedName("placeDescription")
    private String placeDescription;

    public ConcertLocationReduced() {}


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }
}
