package com.example.tfgapp.Activities.Concert.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tfgapp.Activities.Concert.CreateConcertActivity;
import com.example.tfgapp.Entities.Concert.ConcertLocation;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class ConcertPlaceFragment extends Fragment {

    private final String TAG = "ConcertPlaceFragment";
    private View view;
    private Button nextBtn;
    private EditText concertPlace;
    private Context context;
    private ConcertLocation concertLocation;
    private AutocompleteSupportFragment autocompleteFragment;

    private MapView mapView;
    private GoogleMap googleMap;

    public ConcertPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concert_place, container, false);
        context = getContext();

        initView();
        initMapView(savedInstanceState);
        return view;
    }

    private void initView(){
        initPlacePicker();
        concertLocation = CreateConcertActivity.getRegisteredConcertLocation();

        concertPlace = view.findViewById(R.id.concert_place);

        if (concertLocation.getPlaceName() != null)
            concertPlace.setText(concertLocation.getPlaceName());

        nextBtn = view.findViewById(R.id.concert_place_next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String concertLocationPlace = concertPlace.getText().toString();
                if (concertLocationPlace.isEmpty()){
                    Globals.displayShortToast(context, "Por favor, selecciona el lugar donde se va a dar lugar");
                    return;
                }

                setConcertPlace();
                getFragmentManager().beginTransaction().replace(R.id.concert_fragment, new ConcertDateFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void initMapView(Bundle savedInstanceState){
        mapView = view.findViewById(R.id.concert_place_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

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

    private void initPlacePicker(){
        Places.initialize(context, "AIzaSyALidrIEb0aSpRWmHPPh7UCXfsg5o16mmU");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(context);

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.OPENING_HOURS, Place.Field.RATING, Place.Field.PHONE_NUMBER));
        autocompleteFragment.setText("");

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.d(TAG, place.toString());
                concertPlace.setText(place.getName());

                concertLocation = new ConcertLocation();
                concertLocation.setLatitude(place.getLatLng().latitude);
                concertLocation.setLongitude(place.getLatLng().longitude);
                concertLocation.setPlaceName(place.getName());
                concertLocation.setAddress(place.getAddress());

                focusLocationInMap(place);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    private void focusLocationInMap(Place place){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16));
        createPoint(place);
    }

    private void createPoint(Place place){
        googleMap.clear();

        googleMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                        .title(place.getName())
                        .snippet(place.getAddress())
                        .icon(Utils.vectorToBitmap(context.getResources().getDrawable(R.drawable.map_marker),
                                -1, 90, 90))

        );
    }

    private void setConcertPlace(){
        CreateConcertActivity.setRegisteredConcertLocation(concertLocation);
    }

}