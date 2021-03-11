package com.example.tfgapp.API;

import com.example.tfgapp.Entities.Concert.ConcertHome;
import com.example.tfgapp.Entities.Login.AuthenticationData;
import com.example.tfgapp.Entities.User.UserSession;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/concert/home/{userId}")
    Call<ArrayList<ConcertHome>> getHomeConcerts(@Path("userId") String userId);

    @GET("/concert/map/{userLatitude}/{userLongitude}/{radius}")
    Call<ArrayList<ConcertHome>> getConcertsNearby(@Path("userLatitude") double userLatitude, @Path("userLongitude") double userLongitude, @Path("radius") double radius);

    @POST("/auth/login")
    Call<UserSession> doUserLogin(@Body AuthenticationData authenticationData);
}