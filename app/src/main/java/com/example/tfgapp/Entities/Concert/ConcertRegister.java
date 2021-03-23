package com.example.tfgapp.Entities.Concert;

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
    @SerializedName("price")
    private double price;
    @SerializedName("numberAssistants")
    private int numberAssistants;
    @SerializedName("description")
    private String description;
    @SerializedName("extraDescription")
    private String extraDescription;
    @SerializedName("finished")
    private boolean finished;
    @SerializedName("numberImages")
    private int numberImages;
    @SerializedName("artistsIds")
    private ArrayList<String> artistsIds;

    public ConcertRegister(Concert concert, ConcertLocation concertLocation, int numberImages){
        this.name = concert.getName();
        this.latitude = concertLocation.getLatitude();
        this.longitude =  concertLocation.getLongitude();
        this.placeName = concertLocation.getPlaceName();
        this.placeAddress = concertLocation.getAddress();
        this.placeDescription = concertLocation.getPlaceDescription();
        this.dateCreated = Helpers.getTimeStamp();
        this.dateStarts = concert.getDateStarts();
        this.userId = concert.getUserId();
        this.price = concert.getPrice();
        this.numberAssistants = concert.getNumberAssistants();
        this.description = concert.getDescription();
        this.extraDescription = concert.getExtraDescription();
        this.finished = false;
        this.numberImages = numberImages;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberAssistants() {
        return numberAssistants;
    }

    public void setNumberAssistants(int numberAssistants) {
        this.numberAssistants = numberAssistants;
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

    public ArrayList<String> getArtistsIds() {
        return artistsIds;
    }

    public void setArtistsIds(ArrayList<String> artistsIds) {
        this.artistsIds = artistsIds;
    }
}
