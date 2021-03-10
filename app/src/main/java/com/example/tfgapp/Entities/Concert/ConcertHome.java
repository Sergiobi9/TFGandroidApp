package com.example.tfgapp.Entities.Concert;

import com.example.tfgapp.Entities.Artist.ArtistInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ConcertHome {

    @SerializedName("concertId")
    private String concertId;
    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("street")
    private String street;
    @SerializedName("price")
    private double price;
    @SerializedName("dateStarts")
    private String dateStarts;
    @SerializedName("numberAssistants")
    private int numberAssistants;
    @SerializedName("description")
    private String description;
    @SerializedName("placeDescription")
    private String placeDescription;
    @SerializedName("extraDescription")
    private String extraDescription;
    @SerializedName("concertCoverImage")
    private String concertCoverImage;
    @SerializedName("imagesUrls")
    private ArrayList<String> imagesUrls;
    @SerializedName("artists")
    private ArrayList<ArtistInfo> artists;

    public ConcertHome() {}

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDateStarts() {
        return dateStarts;
    }

    public void setDateStarts(String dateStarts) {
        this.dateStarts = dateStarts;
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

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public String getExtraDescription() {
        return extraDescription;
    }

    public void setExtraDescription(String extraDescription) {
        this.extraDescription = extraDescription;
    }

    public String getConcertCoverImage() {
        return concertCoverImage;
    }

    public void setConcertCoverImage(String concertCoverImage) {
        this.concertCoverImage = concertCoverImage;
    }

    public ArrayList<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(ArrayList<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public ArrayList<ArtistInfo> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<ArtistInfo> artists) {
        this.artists = artists;
    }
}
