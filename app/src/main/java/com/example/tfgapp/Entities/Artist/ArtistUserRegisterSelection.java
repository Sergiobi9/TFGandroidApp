package com.example.tfgapp.Entities.Artist;

import com.google.gson.annotations.SerializedName;

public class ArtistUserRegisterSelection {

    @SerializedName("artistId")
    private String artistId;
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("profileUrl")
    private String profileUrl;
    @SerializedName("musicalStyle")
    private String musicalStyle;
    private boolean selected;

    public ArtistUserRegisterSelection() {
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getMusicalStyle() {
        return musicalStyle;
    }

    public void setMusicalStyle(String musicalStyle) {
        this.musicalStyle = musicalStyle;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
