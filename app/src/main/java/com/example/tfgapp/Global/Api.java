package com.example.tfgapp.Global;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tfgapp.API.ApiClient;
import com.example.tfgapp.API.ApiInterface;

public class Api {

    private static Api instance;
    private ApiInterface apiInterface;

    public static synchronized Api getInstance() {
        if (instance == null) {
            instance = new Api();
            instance.apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        }
        return instance;
    }

    public ApiInterface getAPI() {
        return this.apiInterface;
    }
}
