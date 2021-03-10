package com.example.tfgapp.Entities.Artist;

import com.google.gson.annotations.SerializedName;

public class ArtistInfo {

    @SerializedName("userId")
    private String userId;
    @SerializedName("name")
    private String name;
    @SerializedName("country")
    private String country;
    @SerializedName("gender")
    private int gender;
    @SerializedName("profileUrl")
    private String profileUrl;
    @SerializedName("bio")
    private String bio;
    @SerializedName("musicalStyle")
    private String musicalStyle;

    public ArtistInfo(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getMusicalStyle() {
        return musicalStyle;
    }

    public void setMusicalStyle(String musicalStyle) {
        this.musicalStyle = musicalStyle;
    }
}
