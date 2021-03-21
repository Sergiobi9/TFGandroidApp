package com.example.tfgapp.API;

import com.example.tfgapp.Entities.Artist.ArtistUserRegisterSelection;
import com.example.tfgapp.Entities.Concert.ConcertHome;
import com.example.tfgapp.Entities.Concert.ConcertRegister;
import com.example.tfgapp.Entities.CustomUserLikes.MusicStyle;
import com.example.tfgapp.Entities.Login.AuthenticationData;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Entities.User.UserExists;
import com.example.tfgapp.Entities.User.UserPreferences;
import com.example.tfgapp.Entities.User.UserSession;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/concert/home/{userId}")
    Call<ArrayList<ConcertHome>> getHomeConcerts(@Path("userId") String userId);

    @GET("/concert/map/{userLatitude}/{userLongitude}/{radius}")
    Call<ArrayList<ConcertHome>> getConcertsNearby(@Path("userLatitude") double userLatitude, @Path("userLongitude") double userLongitude, @Path("radius") double radius);

    @POST("/auth/login")
    Call<UserSession> doUserLogin(@Body AuthenticationData authenticationData);

    @POST("/user/create")
    Call<User> registerUser(@Body User user);

    @GET("/user/existing/{email}")
    Call<UserExists> checkUserAlreadyExists(@Path("email") String email);

    @GET("/music/style/all")
    Call<ArrayList<MusicStyle>> getMusicStyles();

    @POST("/artist/all/styles")
    Call<ArrayList<ArtistUserRegisterSelection>> getArtistsByMusicStylesSelected(@Body ArrayList<String> musicStylesIds);

    @PUT("/user/preferences/save")
    Call<UserPreferences> saveUserPreferences(@Body UserPreferences userPreferences);

    @POST("/concert/create")
    Call<String> createConcert(@Body ConcertRegister concertRegister);
}