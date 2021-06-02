package com.example.tfgapp.Entities.Concert;

import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Concert.Pricing.ConcertIntervalPricing;
import com.example.tfgapp.Entities.Concert.Pricing.ConcertIntervalPricingDetails;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FullConcertDetails {

    @SerializedName("concertDetails")
    private ConcertDetails concertDetails;
    @SerializedName("concertLocation")
    private ConcertLocationReduced concertLocation;
    @SerializedName("concertArtists")
    private ArrayList<ArtistSimplified> concertArtists;
    @SerializedName("concertTickets")
    private ArrayList<ConcertIntervalPricingDetails> concertTickets;
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

    public ArrayList<ConcertIntervalPricingDetails> getConcertTickets() {
        return concertTickets;
    }

    public void setConcertTickets(ArrayList<ConcertIntervalPricingDetails> concertTickets) {
        this.concertTickets = concertTickets;
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
