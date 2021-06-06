package com.example.tfgapp.Fragments.Navigation;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Broadcasts.UserLocationBroadcast;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Fragments.Artist.ArtistFragment;
import com.example.tfgapp.Fragments.Navigation.User.ConcertInfoFragment;
import com.example.tfgapp.Fragments.Navigation.User.Search.SearchArtistsFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Permissions;
import com.example.tfgapp.Global.UserLocation;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.tfgapp.Global.Helpers.getIntervalDay;

public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";
    private Context context;
    private View view;

    private CarouselView suggestionConcertsCarousel;
    private CarouselView suggestionArtistsCarousel;
    private CarouselView popularConcertsCarousel;
    private CarouselView nearConcertsCarousel;
    private ArrayList<ConcertReduced> suggestionConcertsArrayList;
    private ArrayList<ArtistSimplified> suggestionArtistsArrayList;
    private ArrayList<ConcertReduced> popularConcertsArrayList;
    private ArrayList<ConcertReduced> nearConcertsArrayList;

    private ImageView viralImageView;
    private ImageView loginIcon;
    private TextView welcomeTv, nearConcertsTv;

    private UserSession userSession;
    private String userRole = null;
    private int radius = 100;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();

        userSession = CurrentUser.getInstance(context).getCurrentUser();
        userRole = CurrentUser.getInstance(context).getUserRole();

        initView();

        getCurrentLocation();
        getMostWantedConcert();
        getSuggestedConcerts();
        getSuggestedArtists();
        getMostSearchedConcerts();

        return view;
    }

    private void getCurrentLocation() {
        if (Permissions.checkLocationPermission(context)){
            buildLocationRequest();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                getNearConcerts(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
        } else {
            Permissions.requestLocationPermission(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
            if (Permissions.checkLocationPermission(context)){
                buildLocationRequest();
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

                fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null){
                                    getNearConcerts(location.getLatitude(), location.getLongitude());
                                }
                            }
                        });
            }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, UserLocationBroadcast.class);
        intent.setAction(UserLocationBroadcast.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    private void getNearConcerts(double latitude, double longitude){
        String currentDate = Helpers.getTimeStamp();

        String startDate  = Helpers.getTimeStamp();
        String endDate  = Helpers.addYearToTimestamp();

        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getConcertsNearby(latitude, longitude, radius, currentDate, startDate, endDate);
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Nearby concerts successful");
                        nearConcertsArrayList = response.body();
                        if (!nearConcertsArrayList.isEmpty()){
                            nearConcertsCarousel.setVisibility(View.VISIBLE);
                            nearConcertsTv.setVisibility(View.VISIBLE);
                            loadNearConcertsCarousel();
                        }
                        break;
                    default:
                        Log.d(TAG, "Nearby concerts default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ConcertReduced>> call, Throwable t) {
                Log.d(TAG, "Nearby concerts onFailure " + t.getLocalizedMessage());
            }
        });
    }

    private void loadNearConcertsCarousel() {
        try {
            nearConcertsCarousel.setSize(nearConcertsArrayList.size());
            nearConcertsCarousel.setResource(R.layout.carousel_items_list);
            nearConcertsCarousel.setAutoPlay(false);
            nearConcertsCarousel.setAutoPlayDelay(3000);
            nearConcertsCarousel.hideIndicator(true);
            nearConcertsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            nearConcertsCarousel.setCarouselOffset(OffsetType.START);
            nearConcertsCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    /* Get screen size */
                    CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                    Utils.responsiveView(concertImageLayout, 0.7, 0.7 / 1.714, getActivity());

                    TextView concertName = view.findViewById(R.id.concert_name);
                    TextView concertPlace = view.findViewById(R.id.concert_place);

                    concertName.setText(nearConcertsArrayList.get(position).getName());
                    concertPlace.setText(nearConcertsArrayList.get(position).getPlaceName());

                    ImageView imageView = view.findViewById(R.id.imageView);
                    String imageUrl = nearConcertsArrayList.get(position).getConcertCoverImage();

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            String concertId = nearConcertsArrayList.get(position).getConcertId();
                            bundle.putString("concertId", concertId);
                            ConcertInfoFragment concertInfoFragment = new ConcertInfoFragment();
                            concertInfoFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.main_fragment, concertInfoFragment).addToBackStack(null).commit();
                        }
                    });

                    Glide.with(context).load(imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(imageView);
                }
            });

            nearConcertsCarousel.show();
        } catch (IllegalStateException ie){

        }
    }

    private void initView(){
        loginIcon = view.findViewById(R.id.login_icon);

        loginIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSearchArtists();
            }
        });

        welcomeTv = view.findViewById(R.id.warm_welcome);

        String warmWelcomeText = "";
        warmWelcomeText = getIntervalDay(context, userSession);

        if (userRole != null) {
           loginIcon.setVisibility(View.GONE);
            String userFullName = userSession.getUser().getFirstName();
            if (userFullName != null) {
                userFullName =  userFullName.split("\\s+")[0];
            }
            warmWelcomeText = warmWelcomeText + ", " + userFullName;
        }

        nearConcertsTv = view.findViewById(R.id.near_you_text_view);
        nearConcertsCarousel =view.findViewById(R.id.near_concerts);

        welcomeTv.setText(warmWelcomeText);
    }

    private void getSuggestedArtists(){
        String userId = "";
        if (userSession != null){
            userId = userSession.getUser().getId();
        }

        Call<ArrayList<ArtistSimplified>> call = Api.getInstance().getAPI().getSuggestedArtists(userId);
        call.enqueue(new Callback<ArrayList<ArtistSimplified>>() {
            @Override
            public void onResponse(Call<ArrayList<ArtistSimplified>> call, Response<ArrayList<ArtistSimplified>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artists suggested success " + response.body());
                        suggestionArtistsArrayList = response.body();
                        initSuggestedArtistsCarousel();
                        break;
                    default:
                        Log.d(TAG, "Get artists suggested default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArtistSimplified>> call, Throwable t) {
                Log.d(TAG, "Get artists suggested failure " + t.getLocalizedMessage());
            }
        });

    }

    private void initSuggestedArtistsCarousel(){
        if (suggestionArtistsArrayList != null && suggestionArtistsArrayList.size() != 0){
            suggestionArtistsCarousel = view.findViewById(R.id.artists_to_follow);

            suggestionArtistsCarousel.setSize(suggestionArtistsArrayList.size());
            suggestionArtistsCarousel.setResource(R.layout.home_artists_carrousel_lists);
            suggestionArtistsCarousel.setAutoPlay(false);
            suggestionArtistsCarousel.setAutoPlayDelay(3000);
            suggestionArtistsCarousel.hideIndicator(true);
            suggestionArtistsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            suggestionArtistsCarousel.setCarouselOffset(OffsetType.START);
            suggestionArtistsCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    /* Get screen size */

                    RelativeLayout artistContainerLayout = view.findViewById(R.id.artist_container);
                    TextView artistName = view.findViewById(R.id.artist_name);

                    if (position != suggestionArtistsArrayList.size() - 1){
                        ImageView artistImageView = view.findViewById(R.id.imageView);

                        artistName.setText(suggestionArtistsArrayList.get(position).getArtistName());

                        String artistImageUrl = suggestionArtistsArrayList.get(position).getProfileUrl();

                        Utils.responsiveView(artistImageView, 0.3, 0.3, getActivity());

                        Picasso.get().load(artistImageUrl).transform(new CircleTransform()).into(artistImageView);

                        artistContainerLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                String artistId = suggestionArtistsArrayList.get(position).getArtistId();
                                bundle.putString("artistId", artistId);
                                ArtistFragment artistFragment = new ArtistFragment();
                                artistFragment.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.main_fragment, artistFragment).addToBackStack(null).commit();
                            }
                        });
                    } else {
                        artistContainerLayout.setVisibility(View.GONE);
                        artistName.setVisibility(View.GONE);

                        RelativeLayout addArtistsContainerLayout = view.findViewById(R.id.more_artists_container);
                        addArtistsContainerLayout.setVisibility(View.VISIBLE);

                        Utils.responsiveView(addArtistsContainerLayout, 0.3, 0.3, getActivity());

                        addArtistsContainerLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goSearchArtists();
                            }
                        });
                    }
                }
            });

            suggestionArtistsCarousel.show();
        }

    }

    private void getMostWantedConcert() {
        String imageUrl = "https://images.dailyhive.com/20161031091319/Post-Malone-DHV-Brandon-Artis-Photography-9-e1477931590637.jpg";

        CardView viralCardView = view.findViewById(R.id.viral);
        LinearLayout viralInfoLayout = view.findViewById(R.id.viral_info_layout);

        Utils.responsiveView(viralCardView, 0.85, 0.7 / 1.5, getActivity());
        Utils.responsiveViewWidth(viralInfoLayout, 0.85, getActivity());

        viralImageView = view.findViewById(R.id.viral_image_view);
        Glide.with(context).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(viralImageView);
    }

    private void getSuggestedConcerts() {
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        String currentDate = Helpers.getTimeStamp();
        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getHomeConcerts(userId, currentDate);
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Home concerts success " + response.body());
                        suggestionConcertsArrayList = response.body();
                        initSuggestionsCarousel();
                        break;
                    default:
                        Log.d(TAG, "Home concerts default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertReduced>> call, Throwable t) {
                Log.d(TAG, "Home concerts failure " + t.getLocalizedMessage());
            }
        });
    }

    private void goSearchArtists(){
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, new SearchArtistsFragment()).addToBackStack(null).commit();
    }

    private void getMostSearchedConcerts(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        String currentDate = Helpers.getTimeStamp();
        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getPopularConcerts(userId, currentDate);
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Popular concerts success " + response.body());
                        popularConcertsArrayList = response.body();
                        initPopularConcertsCarousel();
                        break;
                    default:
                        Log.d(TAG, "Popular concerts default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertReduced>> call, Throwable t) {
                Log.d(TAG, "Popular concerts failure " + t.getLocalizedMessage());
            }
        });
    }

    private void initSuggestionsCarousel(){
        if (suggestionConcertsArrayList != null && suggestionConcertsArrayList.size() != 0){
            suggestionConcertsCarousel = view.findViewById(R.id.suggestion_concerts);

            suggestionConcertsCarousel.setSize(suggestionConcertsArrayList.size());
            suggestionConcertsCarousel.setResource(R.layout.carousel_items_list);
            suggestionConcertsCarousel.setAutoPlay(false);
            suggestionConcertsCarousel.setAutoPlayDelay(3000);
            suggestionConcertsCarousel.hideIndicator(true);
            suggestionConcertsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            suggestionConcertsCarousel.setCarouselOffset(OffsetType.START);
            suggestionConcertsCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    /* Get screen size */
                    CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                    Utils.responsiveView(concertImageLayout, 0.7, 0.7 / 1.714, getActivity());

                    TextView concertName = view.findViewById(R.id.concert_name);
                    TextView concertPlace = view.findViewById(R.id.concert_place);

                    concertName.setText(suggestionConcertsArrayList.get(position).getName());
                    concertPlace.setText(suggestionConcertsArrayList.get(position).getPlaceName());

                    ImageView imageView = view.findViewById(R.id.imageView);
                    String imageUrl = suggestionConcertsArrayList.get(position).getConcertCoverImage();

                    System.out.println(imageUrl);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            String concertId = suggestionConcertsArrayList.get(position).getConcertId();
                            bundle.putString("concertId", concertId);
                            ConcertInfoFragment concertInfoFragment = new ConcertInfoFragment();
                            concertInfoFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.main_fragment, concertInfoFragment).addToBackStack(null).commit();
                        }
                    });

                    Glide.with(context).load(imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(imageView);
                }
            });

            suggestionConcertsCarousel.show();
        }

    }

    private void initPopularConcertsCarousel(){

        if (popularConcertsArrayList != null && popularConcertsArrayList.size() != 0){
            popularConcertsCarousel = view.findViewById(R.id.most_searched);

            popularConcertsCarousel.setSize(popularConcertsArrayList.size());
            popularConcertsCarousel.setResource(R.layout.carousel_items_list);
            popularConcertsCarousel.setAutoPlay(false);
            popularConcertsCarousel.setAutoPlayDelay(3000);
            popularConcertsCarousel.hideIndicator(true);
            popularConcertsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            popularConcertsCarousel.setCarouselOffset(OffsetType.START);
            popularConcertsCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    LinearLayout concertInfoLayout = view.findViewById(R.id.concert_info_layout);
                    ViewGroup.LayoutParams params = concertInfoLayout.getLayoutParams();
                    concertInfoLayout.setLayoutParams(params);

                    CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                    Utils.responsiveView(concertImageLayout, 0.7, 0.7 / 1.714, getActivity());

                    TextView concertName = view.findViewById(R.id.concert_name);
                    TextView concertPlace = view.findViewById(R.id.concert_place);

                    concertName.setText(popularConcertsArrayList.get(position).getName());
                    concertPlace.setText(popularConcertsArrayList.get(position).getPlaceName());

                    ImageView imageView = view.findViewById(R.id.imageView);
                    String imageUrl = popularConcertsArrayList.get(position).getConcertCoverImage();
                    Glide.with(context).load(imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(imageView);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            String concertId = popularConcertsArrayList.get(position).getConcertId();
                            bundle.putString("concertId", concertId);
                            ConcertInfoFragment concertInfoFragment = new ConcertInfoFragment();
                            concertInfoFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.main_fragment, concertInfoFragment).addToBackStack(null).commit();
                        }
                    });
                }
            });

            popularConcertsCarousel.show();
        }

    }
}