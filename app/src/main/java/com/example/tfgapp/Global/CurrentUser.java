package com.example.tfgapp.Global;

import com.example.tfgapp.API.ApiClient;
import com.example.tfgapp.API.ApiInterface;
import com.example.tfgapp.Entities.User.UserSession;

public class CurrentUser {

    private UserSession userSession;
    private static CurrentUser instance;
    private ApiInterface apiInterface;

    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public ApiInterface getAPI() {
        return this.apiInterface;
    }
}
