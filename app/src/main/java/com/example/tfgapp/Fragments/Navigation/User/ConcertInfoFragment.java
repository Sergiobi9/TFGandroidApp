package com.example.tfgapp.Fragments.Navigation.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgapp.Entities.Artist.ArtistProfileInfo;
import com.example.tfgapp.Entities.Concert.FullConcertDetails;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConcertInfoFragment extends Fragment {

    private final String TAG = "ConcertInfoFragment";
    private View view;
    private String concertId;
    private Context context;
    private MapView mapView;
    private GoogleMap googleMap;


    public ConcertInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_concert_info, container, false);

        context = getContext();
        concertId = this.getArguments().getString("concertId");

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        initMap();
        getConcertInfo();
        return view;
    }

    private void getConcertInfo(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();

        Call<FullConcertDetails> call = Api.getInstance().getAPI().getFullConcertDetails(userId, concertId);
        call.enqueue(new Callback<FullConcertDetails>() {
            @Override
            public void onResponse(Call<FullConcertDetails> call, Response<FullConcertDetails> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get concert info success " + response.body());
                        loadConcertInfoView(response.body());
                        break;
                    default:
                        Log.d(TAG, "Get concert info default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<FullConcertDetails> call, Throwable t) {
                Log.d(TAG, "Get concert info failure " + t.getLocalizedMessage());
            }
        });
    }

    private void loadConcertInfoView(FullConcertDetails fullConcertDetails){

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
}