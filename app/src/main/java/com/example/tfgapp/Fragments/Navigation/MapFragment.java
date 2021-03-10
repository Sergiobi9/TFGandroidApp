package com.example.tfgapp.Fragments.Navigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Broadcasts.UserLocationBroadcast;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Permissions;
import com.example.tfgapp.Global.UserLocation;
import com.example.tfgapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

import java.util.ArrayList;

public class MapFragment extends Fragment {

    private final String TAG = "MapFragment";
    private View view;
    private Context context;
    private Activity activity;

    private MapView mapView;
    private GoogleMap googleMap;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    private CarouselView concertsCarousel;
    private ArrayList<Concert> concertsArrayList;

    private UserLocation userLocation;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);
        context = getContext();
        activity = getActivity();

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        if (Permissions.checkLocationPermission(context)){
            initMap();
            updateUserLocation();
        } else {
            Permissions.requestLocationPermission(activity);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initMap() {
      mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);

                showPoints();
            }
        });
    }

    private void showPoints(){
        concertsArrayList = new ArrayList<>();
        concertsArrayList.add(new Concert());
        concertsArrayList.add(new Concert());
        concertsArrayList.add(new Concert());
        concertsArrayList.add(new Concert());

        initCarrousel();
    }

    private void initCarrousel(){
        String imageUrl = "https://images.dailyhive.com/20161031091319/Post-Malone-DHV-Brandon-Artis-Photography-9-e1477931590637.jpg";
        /* Get screen size */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        if (concertsArrayList != null && concertsArrayList.size() != 0){
            concertsCarousel = view.findViewById(R.id.map_concerts_list);

            concertsCarousel.setSize(concertsArrayList.size());
            concertsCarousel.setResource(R.layout.map_concerts_carousel_view);
            concertsCarousel.setAutoPlay(false);
            concertsCarousel.setAutoPlayDelay(3000);
            concertsCarousel.hideIndicator(true);
            concertsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            concertsCarousel.setCarouselOffset(OffsetType.CENTER);
            concertsCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    LinearLayout concertInfoLayout = view.findViewById(R.id.concert_info_layout);
                    ViewGroup.LayoutParams params = concertInfoLayout.getLayoutParams();
                    concertInfoLayout.setLayoutParams(params);

                    CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                    params = concertImageLayout.getLayoutParams();
                    params.height = (int) (width * 0.3);
                    params.width = (int) (width * 0.85);
                    concertImageLayout.setLayoutParams(params);

                    ImageView imageView = view.findViewById(R.id.imageView);

                    params = imageView.getLayoutParams();
                    params.height = (int) (width * 0.3);
                    params.width = (int) (width * 0.3);
                    imageView.setLayoutParams(params);

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

            concertsCarousel.show();
        }
    }


    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, UserLocationBroadcast.class);
        intent.setAction(UserLocationBroadcast.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @SuppressLint("MissingPermission")
    private void updateUserLocation(){
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            userLocation = new UserLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                            focusUserInMap();
                        } else {
                            Globals.displayShortToast(context, "Error getting your location");
                        }
                    }
                });
    }

    private void focusUserInMap(){
        if (userLocation != null && !userLocation.longitude.equals("0.0") && !userLocation.latitude.equals("0.0")) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(userLocation.latitude), Double.parseDouble(userLocation.longitude)), 16));
        }
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }
}