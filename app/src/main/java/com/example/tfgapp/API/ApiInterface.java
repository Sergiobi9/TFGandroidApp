package com.example.tfgapp.API;

import com.amazonaws.http.HttpResponse;
import com.example.tfgapp.Entities.Artist.ArtistProfileInfo;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
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
    Call<ArrayList<ConcertReduced>> getHomeConcerts(@Path("userId") String userId);

    @GET("/concert/map/{userLatitude}/{userLongitude}/{radius}")
    Call<ArrayList<ConcertReduced>> getConcertsNearby(@Path("userLatitude") double userLatitude, @Path("userLongitude") double userLongitude, @Path("radius") double radius);

    @POST("/auth/login")
    Call<UserSession> doUserLogin(@Body AuthenticationData authenticationData);

    @POST("/user/create")
    Call<User> registerUser(@Body User user);

    @GET("/user/existing/{email}")
    Call<UserExists> checkUserAlreadyExists(@Path("email") String email);

    @GET("/music/style/all")
    Call<ArrayList<MusicStyle>> getMusicStyles();

    @POST("/artist/all/styles")
    Call<ArrayList<ArtistSimplified>> getArtistsByMusicStylesSelected(@Body ArrayList<String> musicStylesIds);

    @PUT("/user/preferences/save")
    Call<UserPreferences> saveUserPreferences(@Body UserPreferences userPreferences);

    @POST("/concert/create")
    Call<Concert> createConcert(@Body ConcertRegister concertRegister);

    @GET("/artist/all/{userId}")
    Call<ArrayList<ArtistSimplified>> getAllArtists(@Path("userId") String userId);

    @GET("/artist/home/suggested/{userId}")
    Call<ArrayList<ArtistSimplified>> getSuggestedArtists(@Path("userId") String userId);

    @GET("/concert/all/{currentDate}")
    Call<ArrayList<ConcertReduced>> getAllConcertsActiveByCurrentDate(@Path("currentDate") String currentDate);

    @GET("/artist/follow/{artistId}/{userId}/{follow}")
    Call<HttpResponse> followArtist(@Path("artistId") String currentDate, @Path("userId") String userId, @Path("follow") boolean follow);

    @GET("/artist/info/{artistId}/{userId}")
    Call<ArtistProfileInfo> getArtistInfo(@Path("artistId") String currentDate, @Path("userId") String userId);

}