package com.example.tfgapp.Entities.Rating;

public class RatingSimplified {


    private String id;
    private double rate;
    private String comment;
    private String concertCover;
    private String ratingDatePosted;

    public RatingSimplified(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getConcertCover() {
        return concertCover;
    }

    public void setConcertCover(String concertCover) {
        this.concertCover = concertCover;
    }

    public String getRatingDatePosted() {
        return ratingDatePosted;
    }

    public void setRatingDatePosted(String ratingDatePosted) {
        this.ratingDatePosted = ratingDatePosted;
    }
}
