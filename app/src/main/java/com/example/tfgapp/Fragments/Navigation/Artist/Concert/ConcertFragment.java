package com.example.tfgapp.Fragments.Navigation.Artist.Concert;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Activities.Concert.CreateConcertActivity;
import com.example.tfgapp.Entities.Concert.ConcertActivity;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Fragments.Navigation.User.ConcertInfoFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConcertFragment extends Fragment {

    private final String TAG = "ConcertFragment";
    private Button addConcertBtn;
    private View view;
    private Context context;

    private CarouselView ownedConcertsCarousel;
    private CarouselView featuringConcertsCarousel;
    private CarouselView finishedConcertsCarousel;

    private ArrayList<ConcertReduced> ownedConcertsArrayList = new ArrayList<>();
    private ArrayList<ConcertReduced> finishedConcertsArrayList = new ArrayList<>();
    private ArrayList<ConcertReduced> featuringConcertsArrayList = new ArrayList<>();

    private String artistId;

    public ConcertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concert, container, false);
        context = getContext();
        initView();

        artistId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();

        getOwnedConcerts();
        getFeaturingConcerts();
        getFinishedConcerts();

        return view;
    }

    private void getFinishedConcerts() {
        String currentDate = Helpers.getTimeStamp();

        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getArtistFinishedConcerts(artistId, currentDate);
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artist finished concerts success " + response.body());
                        finishedConcertsArrayList = response.body();

                        if (finishedConcertsArrayList.isEmpty()){
                            view.findViewById(R.id.no_finished_concerts_yet).setVisibility(View.VISIBLE);
                        } else {
                            initFinishedConcertsCarousel();
                        }
                        break;
                    default:
                        Log.d(TAG, "Get artist finished concerts default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertReduced>> call, Throwable t) {
                Log.d(TAG, "Get artist finished concerts failure " + t.getLocalizedMessage());
            }
        });
    }

    private void initFinishedConcertsCarousel() {
        finishedConcertsCarousel = view.findViewById(R.id.concerts_finished);
        finishedConcertsCarousel.setSize(finishedConcertsArrayList.size());
        finishedConcertsCarousel.setResource(R.layout.carousel_items_list);
        finishedConcertsCarousel.setAutoPlay(false);
        finishedConcertsCarousel.setAutoPlayDelay(3000);
        finishedConcertsCarousel.hideIndicator(true);
        finishedConcertsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
        finishedConcertsCarousel.setCarouselOffset(OffsetType.START);
        finishedConcertsCarousel.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, final int position) {
                /* Get screen size */
                CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                Utils.responsiveView(concertImageLayout, 0.7, 0.7 / 1.714, getActivity());

                TextView concertName = view.findViewById(R.id.concert_name);
                TextView concertPlace = view.findViewById(R.id.concert_place);

                concertName.setText(finishedConcertsArrayList.get(position).getName());
                concertPlace.setText(finishedConcertsArrayList.get(position).getPlaceName());

                ImageView imageView = view.findViewById(R.id.imageView);
                String imageUrl = finishedConcertsArrayList.get(position).getConcertCoverImage();

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showManageConcertDialog(finishedConcertsArrayList.get(position).getConcertId());
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

        finishedConcertsCarousel.show();
    }

    private void getFeaturingConcerts() {
        String currentDate = Helpers.getTimeStamp();

        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getArtistConcertFeaturing(artistId, currentDate);
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artist featuring concerts success " + response.body());
                        featuringConcertsArrayList = response.body();

                        if (featuringConcertsArrayList.isEmpty()){
                            view.findViewById(R.id.no_featured_concerts_yet).setVisibility(View.VISIBLE);
                        } else {
                            initFeaturingConcertsCarousel();
                        }
                        break;
                    default:
                        Log.d(TAG, "Get artist featuring concerts default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertReduced>> call, Throwable t) {
                Log.d(TAG, "Get artist featuring concerts failure " + t.getLocalizedMessage());
            }
        });
    }

    private void initFeaturingConcertsCarousel() {
        featuringConcertsCarousel = view.findViewById(R.id.concerts_featuring);
        featuringConcertsCarousel.setSize(featuringConcertsArrayList.size());
        featuringConcertsCarousel.setResource(R.layout.carousel_items_list);
        featuringConcertsCarousel.setAutoPlay(false);
        featuringConcertsCarousel.setAutoPlayDelay(3000);
        featuringConcertsCarousel.hideIndicator(true);
        featuringConcertsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
        featuringConcertsCarousel.setCarouselOffset(OffsetType.START);
        featuringConcertsCarousel.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, final int position) {
                /* Get screen size */
                CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                Utils.responsiveView(concertImageLayout, 0.7, 0.7 / 1.714, getActivity());

                TextView concertName = view.findViewById(R.id.concert_name);
                TextView concertPlace = view.findViewById(R.id.concert_place);

                concertName.setText(featuringConcertsArrayList.get(position).getName());
                concertPlace.setText(featuringConcertsArrayList.get(position).getPlaceName());

                ImageView imageView = view.findViewById(R.id.imageView);
                String imageUrl = featuringConcertsArrayList.get(position).getConcertCoverImage();

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showManageConcertDialog(featuringConcertsArrayList.get(position).getConcertId());
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

        featuringConcertsCarousel.show();
    }

    private void getOwnedConcerts() {
        String currentDate = Helpers.getTimeStamp();

        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getArtistCreatedConcerts(artistId, currentDate);
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artist owned concerts success " + response.body());
                        ownedConcertsArrayList = response.body();

                        if (ownedConcertsArrayList.isEmpty()){
                            view.findViewById(R.id.no_hosted_concerts_yet).setVisibility(View.VISIBLE);
                        } else {
                            initOwnedConcertsCarousel();
                        }
                        break;
                    default:
                        Log.d(TAG, "Get artist owned concerts default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertReduced>> call, Throwable t) {
                Log.d(TAG, "Get artist owned concerts failure " + t.getLocalizedMessage());
            }
        });
    }

    private void initOwnedConcertsCarousel() {
        ownedConcertsCarousel = view.findViewById(R.id.concerts_hosted);
        ownedConcertsCarousel.setSize(ownedConcertsArrayList.size());
        ownedConcertsCarousel.setResource(R.layout.carousel_items_list);
        ownedConcertsCarousel.setAutoPlay(false);
        ownedConcertsCarousel.setAutoPlayDelay(3000);
        ownedConcertsCarousel.hideIndicator(true);
        ownedConcertsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
        ownedConcertsCarousel.setCarouselOffset(OffsetType.START);
        ownedConcertsCarousel.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, final int position) {
                /* Get screen size */
                CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                Utils.responsiveView(concertImageLayout, 0.7, 0.7 / 1.714, getActivity());

                TextView concertName = view.findViewById(R.id.concert_name);
                TextView concertPlace = view.findViewById(R.id.concert_place);

                concertName.setText(ownedConcertsArrayList.get(position).getName());
                concertPlace.setText(ownedConcertsArrayList.get(position).getPlaceName());

                ImageView imageView = view.findViewById(R.id.imageView);
                String imageUrl = ownedConcertsArrayList.get(position).getConcertCoverImage();

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showManageConcertDialog(ownedConcertsArrayList.get(position).getConcertId());
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

        ownedConcertsCarousel.show();
    }


    private void showManageConcertDialog(String artistId){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_manage_concert, null);
        dialog.setContentView(view);

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
    }

    private void initView(){
        addConcertBtn = view.findViewById(R.id.add_concert_btn);
        addConcertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CreateConcertActivity.class));
            }
        });
    }
}