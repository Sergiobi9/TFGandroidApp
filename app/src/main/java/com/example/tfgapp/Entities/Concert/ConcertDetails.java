package com.example.tfgapp.Entities.Concert;

import com.google.gson.annotations.SerializedName;

public class ConcertDetails {

    @SerializedName("concertId")
    private String concertId;
    @SerializedName("concertName")
    private String concertName;
    @SerializedName("description")
    private String description;
    @SerializedName("extraDescription")
    private String extraDescription;
    @SerializedName("concertDate")
    private String concertDate;
    @SerializedName("price")
    private double price;

    public ConcertDetails() { }


    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtraDescription() {
        return extraDescription;
    }

    public void setExtraDescription(String extraDescription) {
        this.extraDescription = extraDescription;
    }

    public String getConcertDate() {
        return concertDate;
    }

    public void setConcertDate(String concertDate) {
        this.concertDate = concertDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
