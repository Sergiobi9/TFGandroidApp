package com.example.tfgapp.Entities.Concert;

import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FullConcertDetails {

    @SerializedName("concertDetails")
    private ConcertDetails concertDetails;
    @SerializedName("concertLocation")
    private ConcertLocationReduced concertLocation;
    @SerializedName("concertArtists")
    private ArrayList<ArtistSimplified> concertArtists;
    @SerializedName("placesRemaining")
    private int placesRemaining;
    @SerializedName("bookingsIds")
    private ArrayList<String> bookingsIds;
    @SerializedName("imagesUrls")
    private ArrayList<String> imagesUrls;

    public FullConcertDetails() { }

    public ConcertDetails getConcertDetails() {
        return concertDetails;
    }

    public void setConcertDetails(ConcertDetails concertDetails) {
        this.concertDetails = concertDetails;
    }

    public ConcertLocationReduced getConcertLocation() {
        return concertLocation;
    }

    public void setConcertLocation(ConcertLocationReduced concertLocation) {
        this.concertLocation = concertLocation;
    }

    public ArrayList<ArtistSimplified> getConcertArtists() {
        return concertArtists;
    }

    public void setConcertArtists(ArrayList<ArtistSimplified> concertArtists) {
        this.concertArtists = concertArtists;
    }

    public int getPlacesRemaining() {
        return placesRemaining;
    }

    public void setPlacesRemaining(int placesRemaining) {
        this.placesRemaining = placesRemaining;
    }

    public ArrayList<String> getBookingsIds() {
        return bookingsIds;
    }

    public void setBookingsIds(ArrayList<String> bookingsIds) {
        this.bookingsIds = bookingsIds;
    }

    public ArrayList<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(ArrayList<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }
}
