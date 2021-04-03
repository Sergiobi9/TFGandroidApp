package com.example.tfgapp.Entities.Concert;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ConcertActivity {

    private String userName;
    private String activityType;
    private String userImageUrl;
    private int ticketsAmount;
    private String concertName;
    private String userComment;
    private double userRate;
    private String date;

    public ConcertActivity(String userName, String activityType, String userImageUrl, int ticketsAmount, String concertName, String userComment, double userRate, String date) {
        this.userName = userName;
        this.activityType = activityType;
        this.userImageUrl = userImageUrl;
        this.ticketsAmount = ticketsAmount;
        this.concertName = concertName;
        this.userComment = userComment;
        this.userRate = userRate;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public int getTicketsAmount() {
        return ticketsAmount;
    }

    public void setTicketsAmount(int ticketsAmount) {
        this.ticketsAmount = ticketsAmount;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public double getUserRate() {
        return userRate;
    }

    public void setUserRate(double userRate) {
        this.userRate = userRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

