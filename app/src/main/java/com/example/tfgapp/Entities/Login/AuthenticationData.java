package com.example.tfgapp.Entities.Login;

public class AuthenticationData {

    public String email;
    public String password;

    public AuthenticationData(){}

    public AuthenticationData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
