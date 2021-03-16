package com.example.tfgapp.Entities.User;

import java.util.ArrayList;

public class UserPreferences {

    public String id;
    public String userId;
    public ArrayList<String> artistsId;
    public ArrayList<String> musicStylesIds;

    public UserPreferences(){}

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

    public ArrayList<String> getArtistsId() {
        return artistsId;
    }

    public void setArtistsId(ArrayList<String> artistsId) {
        this.artistsId = artistsId;
    }

    public ArrayList<String> getMusicStylesIds() {
        return musicStylesIds;
    }

    public void setMusicStylesIds(ArrayList<String> musicStylesIds) {
        this.musicStylesIds = musicStylesIds;
    }
}
