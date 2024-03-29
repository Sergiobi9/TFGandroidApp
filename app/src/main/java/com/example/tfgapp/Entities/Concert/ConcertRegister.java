package com.example.tfgapp.Entities.Concert;

import com.example.tfgapp.Entities.Concert.Pricing.ConcertIntervalPricing;
import com.example.tfgapp.Global.Helpers;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ConcertRegister {

    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("placeName")
    private String placeName;
    @SerializedName("placeAddress")
    private String placeAddress;
    @SerializedName("placeDescription")
    private String placeDescription;
    @SerializedName("dateCreated")
    private String dateCreated;
    @SerializedName("dateStarts")
    private String dateStarts;
    @SerializedName("userId")
    private String userId;
    @SerializedName("description")
    private String description;
    @SerializedName("extraDescription")
    private String extraDescription;
    @SerializedName("finished")
    private boolean finished;
    @SerializedName("numberImages")
    private int numberImages;
    @SerializedName("concertIntervalPricing")
    private ArrayList<ConcertIntervalPricing> concertIntervalPricing;
    @SerializedName("artistsIds")
    private ArrayList<String> artistsIds;

    public ConcertRegister(Concert concert, ConcertLocation concertLocation, int numberImages, ArrayList<ConcertIntervalPricing> concertIntervalPricing){
        this.name = concert.getName();
        this.latitude = concertLocation.getLatitude();
        this.longitude =  concertLocation.getLongitude();
        this.placeName = concertLocation.getPlaceName();
        this.placeAddress = concertLocation.getAddress();
        this.placeDescription = concertLocation.getPlaceDescription();
        this.dateCreated = Helpers.getTimeStamp();
        this.dateStarts = concert.getDateStarts();
        this.userId = concert.getUserId();
        this.description = concert.getDescription();
        this.extraDescription = concert.getExtraDescription();
        this.finished = false;
        this.numberImages = numberImages;
        this.concertIntervalPricing = concertIntervalPricing;
        this.artistsIds = concert.getArtistsIds();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateStarts() {
        return dateStarts;
    }

    public void setDateStarts(String dateStarts) {
        this.dateStarts = dateStarts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtraDescription() {
        return extraDescription;
    }

    public void setExtraDescription(String extraDescription) {
        this.extraDescription = extraDescription;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getNumberImages() {
        return numberImages;
    }

    public void setNumberImages(int numberImages) {
        this.numberImages = numberImages;
    }

    public ArrayList<ConcertIntervalPricing> getConcertIntervalPricing() {
        return concertIntervalPricing;
    }

    public void setConcertIntervalPricing(ArrayList<ConcertIntervalPricing> concertIntervalPricing) {
        this.concertIntervalPricing = concertIntervalPricing;
    }

    public ArrayList<String> getArtistsIds() {
        return artistsIds;
    }

    public void setArtistsIds(ArrayList<String> artistsIds) {
        this.artistsIds = artistsIds;
    }

    @Override
    public String toString() {
        return "ConcertRegister{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", placeName='" + placeName + '\'' +
                ", placeAddress='" + placeAddress + '\'' +
                ", placeDescription='" + placeDescription + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateStarts='" + dateStarts + '\'' +
                ", userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                ", extraDescription='" + extraDescription + '\'' +
                ", finished=" + finished +
                ", numberImages=" + numberImages +
                ", concertIntervalPricing=" + concertIntervalPricing +
                ", artistsIds=" + artistsIds +
                '}';
    }
}
