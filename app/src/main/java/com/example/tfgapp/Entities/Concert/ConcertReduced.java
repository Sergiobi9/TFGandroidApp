package com.example.tfgapp.Entities.Concert;

import com.example.tfgapp.Entities.Artist.ArtistInfo;

import java.util.ArrayList;

public class ConcertReduced {

    private String concertId;
    private String name;
    private double placeLatitude;
    private double placeLongitude;
    private String placeName;
    private String placeAddress;
    private double price;
    private String dateStarts;
    private int numberAssistants;
    private String description;
    private String placeDescription;
    private String extraDescription;
    private String concertCoverImage;
    private ArrayList<String> imagesUrls;
    private ArrayList<ArtistInfo> artists;

    public ConcertReduced() { }

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

    public double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public double getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(double placeLongitude) {
        this.placeLongitude = placeLongitude;
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
