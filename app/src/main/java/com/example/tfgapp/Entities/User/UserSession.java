package com.example.tfgapp.Entities.User;

import com.google.gson.Gson;

public class UserSession {

    private String token;
    private User user;

    public UserSession() {
        user = new User();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static UserSession fromJson(String serializedObject) {
        Gson gson = new Gson();
        return gson.fromJson(serializedObject, UserSession.class);
    }
}
