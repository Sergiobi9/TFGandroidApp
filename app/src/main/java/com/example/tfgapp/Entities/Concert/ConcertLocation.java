package com.example.tfgapp.Entities.Concert;

import com.google.gson.annotations.SerializedName;

public class ConcertLocation {

    @SerializedName("id")
    public String id;
    @SerializedName("concertId")
    public String concertId;
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
    @SerializedName("placeName")
    public String placeName;
    @SerializedName("address")
    public String address;
    @SerializedName("placeDescription")
    public String placeDescription;

    public ConcertLocation(){}

    public ConcertLocation(String id, String concertId, double latitude, double longitude, String placeName, String address, String placeDescription) {
        this.id = id;
        this.concertId = concertId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
        this.address = address;
        this.placeDescription = placeDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

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

    @Override
    public String toString() {
        return "ConcertLocation{" +
                "id='" + id + '\'' +
                ", concertId='" + concertId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", placeName='" + placeName + '\'' +
                ", address='" + address + '\'' +
                ", placeDescription='" + placeDescription + '\'' +
                '}';
    }
}
