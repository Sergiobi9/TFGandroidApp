package com.example.tfgapp.Fragments.Navigation.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Entities.Artist.ArtistInfo;
import com.example.tfgapp.Entities.Artist.ArtistProfileInfo;
import com.example.tfgapp.Entities.Artist.ArtistReducedInfo;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Concert.ConcertLocationReduced;
import com.example.tfgapp.Entities.Concert.FullConcertDetails;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Fragments.Artist.ArtistFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConcertInfoFragment extends Fragment {

    private final String TAG = "ConcertInfoFragment";

    private View view;
    private Context context;
    private MapView mapView;
    private GoogleMap googleMap;

    private ImageView concertCover;
    private TextView concertAddress, concertArtistsNames, concertDescription, concertExtraDescription,
    concertPrice, concertTicketsRemaining, howManyTicketsBought, ticketsCounter;
    private CarouselView artistsCarousel, placeImagesCarousel;
    private Button goPlaceIndicationsBtn, bookTickets, updateTickets;

    private LinearLayout userTicketsBoughtLayout, userTicketsNoBoughtLayout;
    private FloatingActionButton addTickets, removeTickets;
    private int howManyTicketsToBookCounter = 1;

    private String concertId;



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
        concertCover = view.findViewById(R.id.concert_cover);

        String imageUrl = fullConcertDetails.getConcertDetails().getConcertCover();
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
                }).into(concertCover);

        concertAddress = view.findViewById(R.id.concert_address);
        concertArtistsNames = view.findViewById(R.id.concert_artists);

        concertAddress.setText(fullConcertDetails.getConcertLocation().getPlaceName());
        ArrayList<ArtistSimplified> artists = fullConcertDetails.getConcertArtists();
        concertArtistsNames.setText(getCurrentArtist(artists));

        concertDescription = view.findViewById(R.id.concert_description);
        String description = fullConcertDetails.getConcertDetails().getDescription();
        if (description == null || description.isEmpty())
            concertDescription.setVisibility(View.GONE);
        else concertDescription.setText(description);

            concertExtraDescription = view.findViewById(R.id.concert_extra_description);
        String extraDescription  = fullConcertDetails.getConcertDetails().getExtraDescription();
        if (extraDescription == null || extraDescription.isEmpty())
            concertExtraDescription.setVisibility(View.GONE);
        else concertExtraDescription.setText(extraDescription);

        concertPrice = view.findViewById(R.id.concert_price);
        double price = fullConcertDetails.getConcertDetails().getPrice();

        if (price <= 0) concertPrice.setText("GRATIS");
        else concertPrice.setText(new DecimalFormat("###.#").format(price) + " €");

        concertTicketsRemaining = view.findViewById(R.id.concert_tickets_remining);
        int ticketsRemaining = fullConcertDetails.getPlacesRemaining();

        concertTicketsRemaining.setText(ticketsRemaining  + " disponibles");

        userTicketsBoughtLayout = view.findViewById(R.id.user_already_bought_tickets_layout);
        userTicketsNoBoughtLayout = view.findViewById(R.id.user_not_tickets_bought_layout);
        howManyTicketsBought = view.findViewById(R.id.how_many_tickets);

        int ticketsBought = fullConcertDetails.getBookingsIds().size();
        if (ticketsBought <= 0) {
            userTicketsNoBoughtLayout.setVisibility(View.VISIBLE);
            userTicketsBoughtLayout.setVisibility(View.GONE);
        } else {
            userTicketsNoBoughtLayout.setVisibility(View.GONE);
            userTicketsBoughtLayout.setVisibility(View.VISIBLE);
            howManyTicketsBought.setText("Tienes " + ticketsBought + " entradas para este concierto");
        }

        ticketsCounter = view.findViewById(R.id.current_tickets_counter_to_book);

        addTickets = view.findViewById(R.id.add_tickets);
        addTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howManyTicketsToBookCounter++;
                ticketsCounter.setText(String.valueOf(howManyTicketsToBookCounter));
            }
        });

        removeTickets = view.findViewById(R.id.remove_tickets);
        removeTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (howManyTicketsToBookCounter - 1 <= 1)
                    howManyTicketsToBookCounter = 1;
                else howManyTicketsToBookCounter--;

                ticketsCounter.setText(String.valueOf(howManyTicketsToBookCounter));
            }
        });

        bookTickets = view.findViewById(R.id.buy_tickets_btn);
        bookTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.displayShortToast(context, "Ready to book " + howManyTicketsToBookCounter + " tickets");
            }
        });

        goPlaceIndicationsBtn = view.findViewById(R.id.go_place_btn);
        goPlaceIndicationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsIndications(fullConcertDetails.getConcertLocation().getLatitude(), fullConcertDetails.getConcertLocation().getLongitude());
            }
        });
        initArtistsCarousel(artists);
        initCarrouselPlaceImages(fullConcertDetails.getImagesUrls());
        focusConcertLocation(fullConcertDetails.getConcertLocation());
    }

    private void focusConcertLocation(ConcertLocationReduced concertLocationReduced){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);

                showMarker(concertLocationReduced);
            }
        });
    }

    private void showMarker(ConcertLocationReduced concertLocationReduced){
        double placeLatitude = concertLocationReduced.getLatitude();
        double placeLongitude = concertLocationReduced.getLongitude();
        googleMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(placeLatitude, placeLongitude))
                        .title(concertLocationReduced.getPlaceName())
                        .snippet(concertLocationReduced.getAddress())
                        .icon(Utils.vectorToBitmap(context.getResources().getDrawable(R.drawable.map_marker),
                                -1, 90, 90))

        );

        moveCameraToMarker(placeLatitude, placeLongitude);
    }

    private void openMapsIndications(double latitude, double longitude){
        try {
            Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s&mode=d", latitude, longitude));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveCameraToMarker(double latitude, double longitude){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
    }

    private void initArtistsCarousel(ArrayList<ArtistSimplified> artists){
        if (artists != null && artists.size() != 0){
            artistsCarousel = view.findViewById(R.id.artists_carousel);

            artistsCarousel.setSize(artists.size());
            artistsCarousel.setResource(R.layout.concert_artists_carousel_view);
            artistsCarousel.setAutoPlay(false);
            artistsCarousel.setAutoPlayDelay(3000);
            artistsCarousel.hideIndicator(true);
            artistsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            artistsCarousel.setCarouselOffset(OffsetType.START);
            artistsCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    /* Get screen size */
                    String artistId = artists.get(position).getArtistId();
                    String artistImageUrl = artists.get(position).getProfileUrl();

                    ImageView artistImage = view.findViewById(R.id.artist_image);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("artistId", artistId);
                            ArtistFragment artistFragment = new ArtistFragment();
                            artistFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.main_fragment, artistFragment).addToBackStack(null).commit();                        }
                    });

                    Picasso.get().load(artistImageUrl).transform(new CircleTransform()).into(artistImage);

                }
            });

            artistsCarousel.show();
        }
    }

    private void initCarrouselPlaceImages(ArrayList<String> placeImages){
        if (placeImages != null && placeImages.size() != 0){
            placeImagesCarousel = view.findViewById(R.id.carrousel_concert_place_images);

            placeImagesCarousel.setSize(placeImages.size());
            placeImagesCarousel.setResource(R.layout.carousel_concert_place_images);
            placeImagesCarousel.setAutoPlay(false);
            placeImagesCarousel.setAutoPlayDelay(3000);
            placeImagesCarousel.hideIndicator(true);
            placeImagesCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            placeImagesCarousel.setCarouselOffset(OffsetType.START);
            placeImagesCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    /* Get screen size */

                    String placeImageUrl = placeImages.get(position);
                    ImageView placeImageView = view.findViewById(R.id.concert_image);

                    Utils.responsiveViewWidth(placeImageView, 0.6, getActivity());

                    Picasso.get().load(placeImageUrl).into(placeImageView);
                }
            });

            placeImagesCarousel.show();
        }
    }

    private String getCurrentArtist(ArrayList<ArtistSimplified> artists) {
        String artistsToReturn = "";
        for (int i = 0; i < artists.size(); i++){
            String artistName = artists.get(i).getArtistName();
            if (i == 0){
                artistsToReturn = artistsToReturn + artistName;
            } else if (i == 1){
                artistsToReturn = artistsToReturn + " ft " + artistName;
            } else {
                artistsToReturn = artistsToReturn + ", " + artistName;
            }
        }
        return artistsToReturn;
    }

}