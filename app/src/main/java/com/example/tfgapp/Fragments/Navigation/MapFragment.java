package com.example.tfgapp.Fragments.Navigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgapp.Activities.MainActivity;
import com.example.tfgapp.Broadcasts.UserLocationBroadcast;
import com.example.tfgapp.Global.Global;
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

public class MapFragment extends Fragment {

    private final String TAG = "MapFragment";
    private View view;
    private Context context;
    private Activity activity;

    private MapView mapView;
    private GoogleMap googleMap;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

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

            }
        });
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
                            Global.displayShortToast(context, "Error getting your location");
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