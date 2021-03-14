package com.example.tfgapp.Fragments.Navigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
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
import com.example.tfgapp.Entities.Concert.ConcertHome;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Permissions;
import com.example.tfgapp.Global.UserLocation;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jama.carouselview.CarouselScrollListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private ArrayList<ConcertHome> concertsArrayList;
    private int radius = 1000;

    private UserLocation userLocation;
    private int lastPostionCarrousel = 0;

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

            }
        });
    }

    private void showPoints(){
        for (int i = 0; i < concertsArrayList.size(); i++) {
            ConcertHome concertToShow = concertsArrayList.get(i);

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(concertToShow.getLatitude(), concertToShow.getLongitude()))
                            .title(concertToShow.getName())
                            .snippet(concertToShow.getStreet())
                            .icon(Utils.vectorToBitmap(getResources().getDrawable(R.drawable.map_marker),
                                    -1, 90, 90))

            );
        }

        /* Add listener for markers on click */
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try {
                    Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s&mode=d", marker.getPosition().latitude, marker.getPosition().longitude));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private void initCarrousel(){

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

                    CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                    ImageView imageView = view.findViewById(R.id.imageView);

                    Utils.responsiveView(concertImageLayout, 0.85, 0.3, getActivity());
                    Utils.responsiveView(imageView, 0.3, 0.3, getActivity());

                    String imageUrl = concertsArrayList.get(position).getConcertCoverImage();

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

            concertsCarousel.setCarouselScrollListener(new CarouselScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState, int position) {
                    if (lastPostionCarrousel != position) {
                        lastPostionCarrousel = position;
                        focusConcertOnCarouselScrolled(position);
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                }

            });


            concertsCarousel.show();
        }

        showPoints();
    }

    private void focusConcertOnCarouselScrolled(int position) {
        ConcertHome concertHome = concertsArrayList.get(position);

        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(concertHome.getLatitude(), concertHome.getLongitude()), 16));
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

            createPoints();
        }
    }

    private void createPoints(){
        Call<ArrayList<ConcertHome>> call = Api.getInstance().getAPI().getConcertsNearby(Double.parseDouble(userLocation.latitude), Double.parseDouble(userLocation.longitude), radius);
        call.enqueue(new Callback<ArrayList<ConcertHome>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ConcertHome>> call, Response<ArrayList<ConcertHome>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Nearby concerts successful");
                        concertsArrayList = response.body();
                        initCarrousel();
                        break;
                    default:
                        Log.d(TAG, "Nearby concerts default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ConcertHome>> call, Throwable t) {
                Log.d(TAG, "Nearby concerts onFailure " + t.getLocalizedMessage());
            }
        });
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }
}