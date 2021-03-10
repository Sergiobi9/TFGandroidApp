package com.example.tfgapp.Fragments.Navigation;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
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
import com.example.tfgapp.Adapters.HomeAdapter;
import com.example.tfgapp.Entities.Concert;
import com.example.tfgapp.R;
import com.jama.carouselview.CarouselScrollListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";
    private Context context;
    private View view;

    private CarouselView suggestionConcertsCarousel;
    private CarouselView mostSearchedCarousel;
    private ArrayList<Concert> suggestionConcertsArrayList;
    private ArrayList<Concert> mostSearchedArrayList;

    private ImageView viralImageView;


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

        loadConcerts();
        initView();

        return view;
    }

    private void initView() {
        String imageUrl = "https://images.dailyhive.com/20161031091319/Post-Malone-DHV-Brandon-Artis-Photography-9-e1477931590637.jpg";
        /* Get screen size */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        if (suggestionConcertsArrayList != null && suggestionConcertsArrayList.size() != 0){
            suggestionConcertsCarousel = view.findViewById(R.id.suggestion_concerts);

            suggestionConcertsCarousel.setSize(suggestionConcertsArrayList.size());
            suggestionConcertsCarousel.setResource(R.layout.carousel_items_list);
            suggestionConcertsCarousel.setAutoPlay(false);
            suggestionConcertsCarousel.setAutoPlayDelay(3000);
            suggestionConcertsCarousel.hideIndicator(true);
            suggestionConcertsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            suggestionConcertsCarousel.setCarouselOffset(OffsetType.CENTER);
            suggestionConcertsCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    /* Get screen size */
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int width = displayMetrics.widthPixels;

                    LinearLayout concertInfoLayout = view.findViewById(R.id.concert_info_layout);
                    ViewGroup.LayoutParams params = concertInfoLayout.getLayoutParams();
                    concertInfoLayout.setLayoutParams(params);

                    CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                    params = concertImageLayout.getLayoutParams();
                    params.height = (int) (width * 0.7 / 1.714);
                    params.width = (int) (width * 0.7);
                    concertImageLayout.setLayoutParams(params);

                    ImageView imageView = view.findViewById(R.id.imageView);
                    String imageUrl = "https://images.dailyhive.com/20161031091319/Post-Malone-DHV-Brandon-Artis-Photography-9-e1477931590637.jpg";

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

        if (mostSearchedArrayList != null && mostSearchedArrayList.size() != 0){
            mostSearchedCarousel = view.findViewById(R.id.most_searched);

            mostSearchedCarousel.setSize(mostSearchedArrayList.size());
            mostSearchedCarousel.setResource(R.layout.carousel_items_list);
            mostSearchedCarousel.setAutoPlay(false);
            mostSearchedCarousel.setAutoPlayDelay(3000);
            mostSearchedCarousel.hideIndicator(true);
            mostSearchedCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
            mostSearchedCarousel.setCarouselOffset(OffsetType.CENTER);
            mostSearchedCarousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, final int position) {
                    LinearLayout concertInfoLayout = view.findViewById(R.id.concert_info_layout);
                    ViewGroup.LayoutParams params = concertInfoLayout.getLayoutParams();
                    concertInfoLayout.setLayoutParams(params);

                    CardView concertImageLayout = view.findViewById(R.id.concert_cards);
                    params = concertImageLayout.getLayoutParams();
                    params.height = (int) (width * 0.7 / 1.714);
                    params.width = (int) (width * 0.7);
                    concertImageLayout.setLayoutParams(params);

                    ImageView imageView = view.findViewById(R.id.imageView);

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

        CardView viralCardView = view.findViewById(R.id.viral);
        ViewGroup.LayoutParams params = viralCardView.getLayoutParams();

        params.height = (int) (width * 0.7 / 1.5);
        params.width = (int) (width * 0.85);
        viralCardView.setLayoutParams(params);

        LinearLayout viralInfoLayout = view.findViewById(R.id.viral_info_layout);
        params = viralInfoLayout.getLayoutParams();
        params.width = (int) (width * 0.85);
        viralInfoLayout.setLayoutParams(params);

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

    private void loadConcerts() {

        suggestionConcertsArrayList = new ArrayList<>();
        suggestionConcertsArrayList.add(new Concert());
        suggestionConcertsArrayList.add(new Concert());
        suggestionConcertsArrayList.add(new Concert());
        suggestionConcertsArrayList.add(new Concert());

        mostSearchedArrayList = new ArrayList<>();
        mostSearchedArrayList.add(new Concert());
        mostSearchedArrayList.add(new Concert());
        mostSearchedArrayList.add(new Concert());
        mostSearchedArrayList.add(new Concert());
    }
}