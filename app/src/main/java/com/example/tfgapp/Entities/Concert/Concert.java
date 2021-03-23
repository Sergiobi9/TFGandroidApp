package com.example.tfgapp.Entities.Concert;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Concert {

    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("dateCreated")
    public String dateCreated;
    @SerializedName("dateStarts")
    public String dateStarts;
    @SerializedName("userId")
    public String userId;
    @SerializedName("price")
    public double price;
    @SerializedName("numberAssistants")
    public int numberAssistants;
    @SerializedName("description")
    public String description;
    @SerializedName("extraDescription")
    public String extraDescription;
    @SerializedName("finished")
    public boolean finished;
    @SerializedName("numberImages")
    public int numberImages;
    @SerializedName("artistsIds")
    public ArrayList<String> artistsIds;

    public Concert(){
        this.finished = false;
    }

    public Concert(String id, String name, String dateCreated, String dateStarts, double price, int numberAssistants, String description, String extraDescription, boolean finished, int numberImages, ArrayList<String> artistsIds) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateStarts = dateStarts;
        this.price = price;
        this.numberAssistants = numberAssistants;
        this.description = description;
        this.extraDescription = extraDescription;
        this.finished = finished;
        this.numberImages = numberImages;
        this.artistsIds = artistsIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateStarts() {
        return dateStarts;
    }

    public void setDateStarts(String dateStarts) {
        this.dateStarts = dateStarts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberAssistants() {
        return numberAssistants;
    }

    public void setNumberAssistants(int numberAssistants) {
        this.numberAssistants = numberAssistants;
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getNumberImages() {
        return numberImages;
    }

    public void setNumberImages(int numberImages) {
        this.numberImages = numberImages;
    }

    public ArrayList<String> getArtistsIds() {
        return artistsIds;
    }

    public void setArtistsIds(ArrayList<String> artistsIds) {
        this.artistsIds = artistsIds;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateStarts='" + dateStarts + '\'' +
                ", price=" + price +
                ", numberAssistants=" + numberAssistants +
                ", description='" + description + '\'' +
                ", extraDescription='" + extraDescription + '\'' +
                ", finished=" + finished +
                ", numberImages=" + numberImages +
                ", artistsIds=" + artistsIds +
                '}';
    }
}
