package com.example.tfgapp.Fragments.Navigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Broadcasts.UserLocationBroadcast;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Fragments.Navigation.User.ConcertInfoFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.jama.carouselview.CarouselScrollListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

import java.util.ArrayList;
import java.util.Calendar;

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
    private ArrayList<ConcertReduced> concertsArrayList;
    private int radius = 100;

    private UserLocation userLocation;
    private int lastPostionCarrousel = 0;

    private FloatingActionButton searchDateBtn, rangePickerBtn;

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

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {

                        Long first = selection.first;
                        Long last = selection.second;

                    }
                });

        searchDateBtn = view.findViewById(R.id.search_date_by_date_btn);
        searchDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        rangePickerBtn = view.findViewById(R.id.filter_range_btn);
        rangePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRangeDialog();
            }
        });

        if (Permissions.checkLocationPermission(context)){
            initMap();
            updateUserLocation();
        } else {
            Permissions.requestLocationPermission(activity);
        }

        return view;
    }

    private void openRangeDialog(){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_slider_map, null);
        dialog.setContentView(view);

        TextView textKm = view.findViewById(R.id.km_text);
        textKm.setText(radius + " kilómetros");
        Slider slider = view.findViewById(R.id.range_picker);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                radius = (int) value;
                textKm.setText(radius + " kilómetros");
            }
        });
        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
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
            ConcertReduced concertToShow = concertsArrayList.get(i);

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(concertToShow.getPlaceLatitude(), concertToShow.getPlaceLongitude()))
                            .title(concertToShow.getName())
                            .snippet(concertToShow.getPlaceAddress())
                            .icon(Utils.vectorToBitmap(context.getResources().getDrawable(R.drawable.map_marker),
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

                    TextView concertNameTv = view.findViewById(R.id.concert_name);
                    concertNameTv.setText(concertsArrayList.get(position).getName());
                    TextView concertAddress = view.findViewById(R.id.concert_place);
                    concertAddress.setText(concertsArrayList.get(position).getPlaceName());

                    TextView concertDate = view.findViewById(R.id.concert_date);
                    Calendar concertDateCalendar = Helpers.getDateAsCalendar(concertsArrayList.get(position).getDateStarts());
                    concertDate.setText(concertDateCalendar.get(Calendar.DATE) + " " + Utils.getMonthSimplified(concertDateCalendar.get(Calendar.MONTH)) + " " + concertDateCalendar.get(Calendar.YEAR));

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

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            String concertId = concertsArrayList.get(position).getConcertId();
                            bundle.putString("concertId", concertId);
                            ConcertInfoFragment concertInfoFragment = new ConcertInfoFragment();
                            concertInfoFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.main_fragment, concertInfoFragment).addToBackStack(null).commit();
                        }
                    });
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
        ConcertReduced concertHome = concertsArrayList.get(position);

        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(concertHome.getPlaceLatitude(), concertHome.getPlaceLongitude()), 16));
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
        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getConcertsNearby(Double.parseDouble(userLocation.latitude), Double.parseDouble(userLocation.longitude), radius);
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
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
            public void onFailure(@NonNull Call<ArrayList<ConcertReduced>> call, Throwable t) {
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