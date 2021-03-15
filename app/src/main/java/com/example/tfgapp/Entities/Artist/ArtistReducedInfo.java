package com.example.tfgapp.Entities.Artist;

import com.google.gson.annotations.SerializedName;

public class ArtistReducedInfo {

    @SerializedName("artistId")
    private String artistId;
    @SerializedName("name")
    private String name;
    @SerializedName("imageUrl")
    private String imageUrl;

    public ArtistReducedInfo(){}

    public ArtistReducedInfo(String artistId, String name, String imageUrl) {
        this.artistId = artistId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
