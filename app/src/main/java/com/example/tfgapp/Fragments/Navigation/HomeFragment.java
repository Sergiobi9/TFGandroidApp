package com.example.tfgapp.Fragments.Navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Activities.Login.LoginActivity;
import com.example.tfgapp.Entities.Artist.ArtistInfo;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Fragments.Artist.ArtistFragment;
import com.example.tfgapp.Fragments.Navigation.User.ConcertInfoFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
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
    private CarouselView mostSearchedCarousel;
    private ArrayList<ConcertReduced> suggestionConcertsArrayList;
    private ArrayList<ArtistSimplified> suggestionArtistsArrayList;
    private ArrayList<ConcertReduced> mostSearchedArrayList;

    private ImageView viralImageView;
    private ImageView loginIcon;
    private TextView welcomeTv;

    private UserSession userSession;
    private String userRole = null;

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

        getMostWantedConcert();
        getSuggestedConcerts();
        getSuggestedArtists();
        getMostSearchedConcerts();

        return view;
    }

    private void initView(){
        loginIcon = view.findViewById(R.id.login_icon);

        loginIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginScreen();
            }
        });

        welcomeTv = view.findViewById(R.id.warm_welcome);

        String warmWelcomeText = "";
        warmWelcomeText = getIntervalDay(context, userSession);

        if (userRole != null) {
           loginIcon.setVisibility(View.GONE);
            String userFullName = userSession.getUser().getFirstName();
            warmWelcomeText = warmWelcomeText + ", " + userFullName;
        }

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
                                goLoginScreen();
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

        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getHomeConcerts("hello");
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

    private void goLoginScreen(){
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    private void getMostSearchedConcerts(){

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

    private void initMostSearchedCarousel(){
/*
        if (mostSearchedArrayList != null && mostSearchedArrayList.size() != 0){
            mostSearchedCarousel = view.findViewById(R.id.most_searched);

            mostSearchedCarousel.setSize(mostSearchedArrayList.size());
            mostSearchedCarousel.setResource(R.layout.carousel_items_list);
            mostSearchedCarousel.setAutoPlay(false);
            mostSearchedCarousel.setAutoPlayDelay(3000);
            mostSearchedCarousel.hideIndicator(true);
            mostSearchedCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            mostSearchedCarousel.setCarouselOffset(OffsetType.START);
            mostSearchedCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    LinearLayout concertInfoLayout = view.findViewById(R.id.concert_info_layout);
                    ViewGroup.LayoutParams params = concertInfoLayout.getLayoutParams();
                    concertInfoLayout.setLayoutParams(params);

                    CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                    params = concertImageLayout.getLayoutParams();
                    params.height = (int) (screenWidth * 0.7 / 1.714);
                    params.width = (int) (screenWidth * 0.7);
                    concertImageLayout.setLayoutParams(params);

                    ImageView imageView = view.findViewById(R.id.imageView);
                    String imageUrl = mostSearchedArrayList.get(position).getConcertCoverImage();
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

            mostSearchedCarousel.show();
        }
*/
    }
}