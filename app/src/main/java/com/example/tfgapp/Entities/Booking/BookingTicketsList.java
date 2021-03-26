package com.example.tfgapp.Entities.Booking;

import java.util.ArrayList;

public class BookingTicketsList {

    private String concertId;
    private double concertLatitude;
    private double concertLongitude;
    private String concertPlaceName;
    private String concertName;
    private String concertCover;
    private String concertStarts;
    private ArrayList<Booking> bookings;

    public BookingTicketsList(){}

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    public double getConcertLatitude() {
        return concertLatitude;
    }

    public void setConcertLatitude(double concertLatitude) {
        this.concertLatitude = concertLatitude;
    }

    public double getConcertLongitude() {
        return concertLongitude;
    }

    public void setConcertLongitude(double concertLongitude) {
        this.concertLongitude = concertLongitude;
    }

    public String getConcertPlaceName() {
        return concertPlaceName;
    }

    public void setConcertPlaceName(String concertPlaceName) {
        this.concertPlaceName = concertPlaceName;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getConcertStarts() {
        return concertStarts;
    }

    public void setConcertStarts(String concertStarts) {
        this.concertStarts = concertStarts;
    }

    public String getConcertCover() {
        return concertCover;
    }

    public void setConcertCover(String concertCover) {
        this.concertCover = concertCover;
    }
}
