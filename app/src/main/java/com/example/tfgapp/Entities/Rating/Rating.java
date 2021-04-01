package com.example.tfgapp.Entities.Rating;

public class Rating {

    private String id;
    private String userId;
    private String concertId;
    private double rate;
    private String comment;
    private String ratingRatePosted;

    public Rating() { }

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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRatingRatePosted() {
        return ratingRatePosted;
    }

    public void setRatingRatePosted(String ratingRatePosted) {
        this.ratingRatePosted = ratingRatePosted;
    }
}
