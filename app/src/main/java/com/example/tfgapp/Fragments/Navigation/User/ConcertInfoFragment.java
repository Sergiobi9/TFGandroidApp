package com.example.tfgapp.Fragments.Navigation.User;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Adapters.ConcertTicketsAdapter;
import com.example.tfgapp.Adapters.ConcertsAdapter;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Booking.RegisterBooking;
import com.example.tfgapp.Entities.Concert.ConcertLocationReduced;
import com.example.tfgapp.Entities.Concert.FullConcertDetails;
import com.example.tfgapp.Entities.Concert.Pricing.ConcertIntervalPricingDetails;
import com.example.tfgapp.Entities.InfoResponse.InfoResponse;
import com.example.tfgapp.Fragments.Artist.ArtistFragment;
import com.example.tfgapp.Fragments.Navigation.User.Tickets.TicketsFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.Global.Constants;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
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

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    private TextView concertName, concertArtistsNames, concertDescription, concertExtraDescription,
    concertPrice, concertTicketsRemaining, howManyTicketsBought, ticketsCounter, concertDay,
            concertYear, concertHour;
    private CarouselView artistsCarousel, placeImagesCarousel;
    private Button goPlaceIndicationsBtn, bookTickets, updateTickets;

    private LinearLayout userTicketsBoughtLayout, userTicketsNoBoughtLayout;
    private FloatingActionButton addTickets, removeTickets;
    private int howManyTicketsToBookCounter = 1;
    private ArrayList<ConcertIntervalPricingDetails> concertIntervalPricingDetails;
    private RecyclerView ticketsRecyclerView;
    private ConcertTicketsAdapter concertTicketsAdapter;

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

        bookTickets = view.findViewById(R.id.buy_tickets_btn);
        bookTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookTickets();
            }
        });

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

        concertIntervalPricingDetails = fullConcertDetails.getConcertTickets();

        boolean userAlreadyGotTickets = getUserGotAtLeastOneTicket();
        userTicketsBoughtLayout = view.findViewById(R.id.user_already_bought_tickets_layout);
        userTicketsNoBoughtLayout = view.findViewById(R.id.user_not_tickets_bought_layout);

        if (userAlreadyGotTickets){
            userTicketsNoBoughtLayout.setVisibility(View.GONE);
            userTicketsBoughtLayout.setVisibility(View.VISIBLE);
            howManyTicketsBought = view.findViewById(R.id.how_many_tickets);

            String text = getTicketsBought();
            howManyTicketsBought.setText(text);

            view.findViewById(R.id.buy_more_tickets).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userTicketsNoBoughtLayout.setVisibility(View.VISIBLE);
                    userTicketsBoughtLayout.setVisibility(View.GONE);

                    for (int i = 0; i < concertIntervalPricingDetails.size(); i++){
                        concertIntervalPricingDetails.get(i).setTicketsBought(0);
                    }

                    ticketsRecyclerView = view.findViewById(R.id.tickets_available);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    ticketsRecyclerView.setLayoutManager(mLayoutManager);
                    ticketsRecyclerView.setNestedScrollingEnabled(false);

                    concertTicketsAdapter = new ConcertTicketsAdapter(context, concertIntervalPricingDetails, getActivity());
                    ticketsRecyclerView.setAdapter(concertTicketsAdapter);
                }
            });

        } else{
            userTicketsNoBoughtLayout.setVisibility(View.VISIBLE);
            userTicketsBoughtLayout.setVisibility(View.GONE);

            ticketsRecyclerView = view.findViewById(R.id.tickets_available);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            ticketsRecyclerView.setLayoutManager(mLayoutManager);
            ticketsRecyclerView.setNestedScrollingEnabled(false);

            concertTicketsAdapter = new ConcertTicketsAdapter(context, concertIntervalPricingDetails, getActivity());
            ticketsRecyclerView.setAdapter(concertTicketsAdapter);
        }



        concertName = view.findViewById(R.id.concert_name);
        concertArtistsNames = view.findViewById(R.id.concert_artists);

        concertName.setText(fullConcertDetails.getConcertDetails().getConcertName());
        ArrayList<ArtistSimplified> artists = fullConcertDetails.getConcertArtists();
        concertArtistsNames.setText(getCurrentArtist(artists));

        Calendar concertDate = Helpers.getDateAsCalendar(fullConcertDetails.getConcertDetails().getConcertDate());

        concertDay = view.findViewById(R.id.concert_day);
        concertYear = view.findViewById(R.id.concert_year);

        String concertDayStr = Utils.getMonthSimplified(concertDate.get(Calendar.MONTH)) + " " + concertDate.get(Calendar.DATE);
        String minute = concertDate.get(Calendar.MINUTE) < 10 ? "0" + concertDate.get(Calendar.MINUTE): String.valueOf(concertDate.get(Calendar.MINUTE));
        String hour = concertDate.get(Calendar.HOUR_OF_DAY) + ":" +  minute + "h";

        concertHour = view.findViewById(R.id.concert_hour);
        concertHour.setText("El concierto empieza a las " + hour);
        concertDay.setText(concertDayStr);

        String concertYearStr = String.valueOf(concertDate.get(Calendar.YEAR));
        concertYear.setText(concertYearStr);

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

    private boolean getUserGotAtLeastOneTicket(){
        for (int i = 0; i < concertIntervalPricingDetails.size(); i++){
            int ticketsBought = concertIntervalPricingDetails.get(i).getTicketsBought();

            if (ticketsBought != 0){
                return true;
            }
        }

        return false;
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

    private void bookTickets(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        dialogPurchasingTickets();
        getTickets(userId);
    }

    private Dialog dialogPurchasing;

    private void dialogPurchasingTickets(){
        dialogPurchasing = new Dialog(context);
        dialogPurchasing.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPurchasing.setCancelable(false);
        dialogPurchasing.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialogPurchasing.setContentView(view);

        TextView textView = view.findViewById(R.id.title);
        textView.setText("Comprando tickets para este concierto");

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialogPurchasing.show();
    }

    private void getTickets(String userId){
        RegisterBooking registerBooking = new RegisterBooking(userId, concertId, concertIntervalPricingDetails);
        System.out.println(registerBooking.toString());
       Call<InfoResponse> call = Api.getInstance().getAPI().bookConcertTickets(registerBooking);
        call.enqueue(new Callback<InfoResponse>() {
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Purchase tickets success " + response.body());
                        String info = response.body().getInfo();
                        dialogPurchasing.dismiss();
                        if (info.equals(Constants.BOOKING_SUCCEEDED)){
                            loadTicketsPurchasedDialog();
                        } else if (info.equals(Constants.BOOKING_EXCEEDED)){
                            Globals.displayShortToast(context, "No hay entradas disponibles para este concierto");
                        }
                        break;
                    default:
                        Log.d(TAG, "Purchase tickets default " + response.code());
                        dialogPurchasing.dismiss();
                        Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
                        break;
                }
            }

            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {
                Log.d(TAG, "Purchase tickets failure " + t.getLocalizedMessage());
                dialogPurchasing.dismiss();
                Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
            }
        });
    }

    private void loadTicketsPurchasedDialog(){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_tickets_purchased, null);
        dialog.setContentView(view);

        TextView textView = view.findViewById(R.id.title);

        String text = getTicketsBought();

        textView.setText(text);

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        Button accept = view.findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goTickets();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
                goTickets();
            }
        });

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
    }

    @NotNull
    private String getTicketsBought() {
        String text = "Has comprado las siguientes entradas:\n\n";

        for (int i = 0; i < concertIntervalPricingDetails.size(); i++){
            int ticketsBooked = concertIntervalPricingDetails.get(i).getTicketsBought();

            if (ticketsBooked != 0){
                text = text + "\t" + (ticketsBooked == 1? ticketsBooked +" entrada " : ticketsBooked + " entradas ") + concertIntervalPricingDetails.get(i).getName() + "\n";
            }
        }
        return text;
    }

    private void goTickets(){
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, new TicketsFragment()).addToBackStack(null).commit();
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