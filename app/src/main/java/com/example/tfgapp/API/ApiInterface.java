package com.example.tfgapp.API;

import com.example.tfgapp.Entities.Artist.ArtistProfileInfo;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Artist.ArtistUserProfile;
import com.example.tfgapp.Entities.Booking.BookingTicketsList;
import com.example.tfgapp.Entities.Booking.RegisterBooking;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Entities.Concert.ConcertActivity;
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
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/concert/home/suggestions/{userId}/{currentDate}")
    Call<ArrayList<ConcertReduced>> getHomeConcerts(@Path("userId") String userId, @Path("currentDate") String currentDate);

    @GET("/concert/home/popular/{userId}/{currentDate}")
    Call<ArrayList<ConcertReduced>> getPopularConcerts(@Path("userId") String userId, @Path("currentDate") String currentDate);

    @GET("/concert/map/latitude/{userLatitude}/longitude/{userLongitude}/radius/{radius}/currentDate/{currentDate}/startDate/{startDate}/endDate/{endDate}")
    Call<ArrayList<ConcertReduced>> getConcertsNearby(@Path("userLatitude") double userLatitude, @Path("userLongitude") double userLongitude, @Path("radius") double radius,  @Path("currentDate") String currentDate,  @Path("startDate") String startDate,  @Path("endDate") String endDate);

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

    @GET("/concert/all/currentDate/{currentDate}/startDate/{startDate}/endDate/{endDate}")
    Call<ArrayList<ConcertReduced>> getAllConcertsActiveByCurrentDate(@Path("startDate") String startDate, @Path("endDate") String endDate, @Path("currentDate") String currentDate);

    @GET("/artist/follow/{artistId}/{userId}/{follow}")
    Call<ResponseBody> followArtist(@Path("artistId") String artistId, @Path("userId") String userId, @Path("follow") boolean follow);

    @GET("/artist/info/{currentDate}/{artistId}/{userId}")
    Call<ArtistProfileInfo> getArtistInfo(@Path("currentDate") String currentDate, @Path("artistId") String artistId, @Path("userId") String userId);

    @GET("/artist/info/artistId/{artistId}")
    Call<ArtistUserProfile> getArtistUserProfile(@Path("artistId") String artistId);

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

    @DELETE("/user/{userId}")
    Call<ResponseBody> deleteUser(@Path("userId") String userId);

    @PUT("/concert/rating/post/{currentDate}")
    Call<RatingSimplified> updateUserConcertRating(@Path("currentDate") String currentDate, @Body RatingSimplified ratingSimplified);

    @GET("/artist/all")
    Call<ArrayList<ArtistSimplified>> getAllArtists();

    @GET("/concert/artist/{artistId}/{currentDate}")
    Call<ArrayList<ConcertReduced>> getArtistConcerts(@Path("artistId") String artistId, @Path("currentDate") String currentDate);

    @GET("/concert/all/activity/{artistId}")
    Call<ArrayList<ConcertActivity>> getArtistActivityByUsers(@Path("artistId") String artistId);

    @GET("/concert/all/hosting/artistId/{artistId}/{currentDate}")
    Call<ArrayList<ConcertReduced>> getArtistCreatedConcerts(@Path("artistId") String artistId, @Path("currentDate") String currentDate);

    @GET("/concert/all/finished/artistId/{artistId}/{currentDate}")
    Call<ArrayList<ConcertReduced>> getArtistFinishedConcerts(@Path("artistId") String artistId, @Path("currentDate") String currentDate);

    @GET("/concert/all/featuring/artistId/{artistId}/{currentDate}")
    Call<ArrayList<ConcertReduced>> getArtistConcertFeaturing(@Path("artistId") String artistId, @Path("currentDate") String currentDate);

    @GET("/concert/next/artistId/{artistId}/{currentDate}")
    Call<ConcertReduced> getArtistNextConcert(@Path("artistId") String artistId, @Path("currentDate") String currentDate);

    @DELETE("/concert/delete/concertId/{concertId}")
    Call<ResponseBody> deleteConcertByConcertId(@Path("concertId") String concertId);
}