package com.example.tfgapp.API;

import com.example.tfgapp.Entities.Concert.ConcertHome;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/concert/home/{userId}")
    Call<ArrayList<ConcertHome>> getHomeConcerts(@Path("userId") String userId);
}