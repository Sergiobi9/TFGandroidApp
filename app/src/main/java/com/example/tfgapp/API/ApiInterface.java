package com.example.tfgapp.API;

import com.example.tfgapp.Entities.Artist.ArtistProfileInfo;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Booking.BookingTicketsList;
import com.example.tfgapp.Entities.Booking.RegisterBooking;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Entities.Concert.ConcertRegister;
import com.example.tfgapp.Entities.Concert.FullConcertDetails;
import com.example.tfgapp.Entities.CustomUserLikes.MusicStyle;
import com.example.tfgapp.Entities.Login.AuthenticationData;
import com.example.tfgapp.Entities.Rating.RatingSimplified;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Entities.InfoResponse.InfoResponse;
import com.example.tfgapp.Entities.User.UserPreferences;
import com.example.tfgapp.Entities.User.UserSession;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/concert/home/suggestions/{userId}/{currentDate}")
    Call<ArrayList<ConcertReduced>> getHomeConcerts(@Path("userId") String userId, @Path("currentDate") String currentDate);

    @GET("/concert/home/popular/{userId}/{currentDate}")
    Call<ArrayList<ConcertReduced>> getPopularConcerts(@Path("userId") String userId, @Path("currentDate") String currentDate);

    @GET("/concert/map/{userLatitude}/{userLongitude}/{radius}")
    Call<ArrayList<ConcertReduced>> getConcertsNearby(@Path("userLatitude") double userLatitude, @Path("userLongitude") double userLongitude, @Path("radius") double radius);

    @POST("/auth/login")
    Call<UserSession> doUserLogin(@Body AuthenticationData authenticationData);

    @POST("/user/create")
    Call<User> registerUser(@Body User user);

    @GET("/user/existing/{email}")
    Call<InfoResponse> checkUserAlreadyExists(@Path("email") String email);

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
    Call<ResponseBody> followArtist(@Path("artistId") String artistId, @Path("userId") String userId, @Path("follow") boolean follow);

    @GET("/artist/info/{currentDate}/{artistId}/{userId}")
    Call<ArtistProfileInfo> getArtistInfo(@Path("currentDate") String currentDate, @Path("artistId") String artistId, @Path("userId") String userId);

    @GET("/concert/info/{userId}/{concertId}")
    Call<FullConcertDetails> getFullConcertDetails(@Path("userId") String userId, @Path("concertId") String concertId);

    @POST("/booking/create")
    Call<InfoResponse> bookConcertTickets(@Body RegisterBooking registerBooking);

    @GET("/booking/all/user/{userId}/{currentDate}")
    Call<ArrayList<BookingTicketsList>> getUserTicketsBooked(@Path("userId") String userId, @Path("currentDate") String currentDate);

    @GET("/concert/rating/all/userId/{userId}/{currentDate}")
    Call<ArrayList<RatingSimplified>> getUserConcertRatings(@Path("userId") String userId, @Path("currentDate") String currentDate);

    @GET("/user/{userId}")
    Call<User> getUser(@Path("userId") String userId);

    @GET("/music/style/following/userId/{userId}")
    Call<ArrayList<MusicStyle>> getMusicStylesFollowingByUserId(@Path("userId") String userId);

    @GET("/artist/following/userId/{userId}")
    Call<ArrayList<ArtistSimplified>> getArtistsFollowingByUserId(@Path("userId") String userId);

    @PUT("/user/update")
    Call<User> updateUser(@Body User user);

    @PUT("/concert/rating/post/{currentDate}")
    Call<RatingSimplified> updateUserConcertRating(@Path("currentDate") String currentDate, @Body RatingSimplified ratingSimplified);

}