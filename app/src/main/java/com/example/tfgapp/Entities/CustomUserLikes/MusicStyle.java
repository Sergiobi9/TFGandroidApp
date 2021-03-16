package com.example.tfgapp.Entities.CustomUserLikes;

import com.google.gson.annotations.SerializedName;

public class MusicStyle {

    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("imageUrl")
    public String imageUrl;
    public boolean selected;

    public MusicStyle() {
    }

    public MusicStyle(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
