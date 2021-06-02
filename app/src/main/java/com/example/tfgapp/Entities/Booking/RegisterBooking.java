package com.example.tfgapp.Entities.Booking;

import android.text.format.DateUtils;

import com.example.tfgapp.Entities.Concert.Pricing.ConcertIntervalPricingDetails;
import com.example.tfgapp.Global.Helpers;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RegisterBooking {

    @SerializedName("userId")
    private String userId;
    @SerializedName("concertId")
    private String concertId;
    @SerializedName("bookings")
    private ArrayList<ConcertIntervalPricingDetails> bookings;
    @SerializedName("dateBooked")
    private String dateBooked;

    public RegisterBooking() { }

    public RegisterBooking(String userId, String concertId, ArrayList<ConcertIntervalPricingDetails> bookings) {
        this.userId = userId;
        this.concertId = concertId;
        this.bookings = bookings;
        this.dateBooked = Helpers.getTimeStamp();
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

    public ArrayList<ConcertIntervalPricingDetails> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<ConcertIntervalPricingDetails> bookings) {
        this.bookings = bookings;
    }

    public String getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(String dateBooked) {
        this.dateBooked = dateBooked;
    }

    @Override
    public String toString() {
        return "RegisterBooking{" +
                "userId='" + userId + '\'' +
                ", concertId='" + concertId + '\'' +
                ", bookings=" + bookings +
                ", dateBooked='" + dateBooked + '\'' +
                '}';
    }
}
