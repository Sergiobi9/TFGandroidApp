package com.example.tfgapp.Entities.Booking;

import com.google.gson.annotations.SerializedName;

public class Booking {

    @SerializedName("id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("ticketId")
    private String ticketId;
    @SerializedName("concertId")
    private String concertId;
    @SerializedName("price")
    private double price;
    @SerializedName("dateBooked")
    private String dateBooked;

    public Booking(String id, String userId, String ticketId, String concertId, double price, String dateBooked) {
        this.id = id;
        this.userId = userId;
        this.ticketId = ticketId;
        this.concertId = concertId;
        this.price = price;
        this.dateBooked = dateBooked;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Booking(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(String dateBooked) {
        this.dateBooked = dateBooked;
    }
}
