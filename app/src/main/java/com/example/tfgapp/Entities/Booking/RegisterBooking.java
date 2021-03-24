package com.example.tfgapp.Entities.Booking;

import com.google.gson.annotations.SerializedName;

public class RegisterBooking {

    @SerializedName("userId")
    private String userId;
    @SerializedName("concertId")
    private String concertId;
    @SerializedName("bookings")
    private int bookings;
    @SerializedName("dateBooked")
    private String dateBooked;

    public RegisterBooking() { }

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

    public int getBookings() {
        return bookings;
    }

    public void setBookings(int bookings) {
        this.bookings = bookings;
    }

    public String getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(String dateBooked) {
        this.dateBooked = dateBooked;
    }
}
