package com.example.tfgapp.Entities.User;

public class UserSession {

    private String token;
    private User user;

    public UserSession() {
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
}
