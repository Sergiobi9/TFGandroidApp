package com.example.tfgapp.Entities.User;

import java.util.ArrayList;

public class UserPreferences {

    public String id;
    public String userId;
    public ArrayList<String> artistsIds;
    public ArrayList<String> musicStylesIds;

    public UserPreferences(){}

    public UserPreferences(String userId, ArrayList<String> artistsIds, ArrayList<String> musicStylesIds) {
        this.userId = userId;
        this.artistsIds = artistsIds;
        this.musicStylesIds = musicStylesIds;
    }

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

    public ArrayList<String> getArtistsIds() {
        return artistsIds;
    }

    public void setArtistsIds(ArrayList<String> artistsIds) {
        this.artistsIds = artistsIds;
    }

    public ArrayList<String> getMusicStylesIds() {
        return musicStylesIds;
    }

    public void setMusicStylesIds(ArrayList<String> musicStylesIds) {
        this.musicStylesIds = musicStylesIds;
    }
}
