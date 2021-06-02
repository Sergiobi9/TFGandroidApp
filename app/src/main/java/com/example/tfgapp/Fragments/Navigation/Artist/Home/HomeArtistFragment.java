package com.example.tfgapp.Fragments.Navigation.Artist.Home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.example.tfgapp.Activities.Concert.Fragment.ConcertArtistsFragment;
import com.example.tfgapp.Activities.MainActivity;
import com.example.tfgapp.Adapters.ArtistUsersActivityAdapter;
import com.example.tfgapp.Adapters.ConcertsAdapter;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Entities.Concert.ConcertActivity;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Entities.Rating.Rating;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Fragments.Navigation.Artist.Concert.ConcertFragment;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.tfgapp.Global.Helpers.getIntervalDay;

public class HomeArtistFragment extends Fragment {

    private View view;
    private Context context;
    private final String TAG = "HomeArtistFragment";

    private TextView welcomeTv;
    private UserSession userSession;

    private ArrayList<ConcertActivity> concertActivityArrayList = new ArrayList<>();
    private ArrayList<ConcertActivity> concertActivityCommentsArrayList = new ArrayList<>();
    private List<ConcertActivity> concertsCommentsOnlyFive = new ArrayList<>();

    private RecyclerView activityRecyclerView;
    private ArtistUsersActivityAdapter artistUsersActivityAdapter;

    private Button createConcertBtn, concertListBtn;

    private CarouselView artistUsersActivityCommentsCarousel;

    public HomeArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_artist, container, false);
        context = getContext();

        userSession = CurrentUser.getInstance(context).getCurrentUser();
        initView();

        getArtistUserActivity();
        getArtistNextConcert();

        return view;
    }

    private void showManageConcertDialog(String concertId){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_manage_concert, null);
        dialog.setContentView(view);

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        TextView showInfo = view.findViewById(R.id.show);
        showInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("concertId", concertId);
                ConcertInfoFragment concertInfoFragment = new ConcertInfoFragment();
                concertInfoFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, concertInfoFragment).addToBackStack(null).commit();
                dialog.dismiss();
            }
        });

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
    }

    private void getArtistNextConcert() {
        String artistId = userSession.getUser().getId();
        String currentDate = Helpers.getTimeStamp();

        Call<ConcertReduced> call = Api.getInstance().getAPI().getArtistNextConcert(artistId, currentDate);
        call.enqueue(new Callback<ConcertReduced>() {
            @Override
            public void onResponse(Call<ConcertReduced> call, Response<ConcertReduced> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artist next concert success " + response.body());

                        initNextConcertView(response.body());
                        break;
                    default:
                        Log.d(TAG, "Get artist next concert default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ConcertReduced> call, Throwable t) {
                Log.d(TAG, "Get artist next concert failure " + t.getLocalizedMessage());
            }
        });
    }

    private void initNextConcertView(ConcertReduced concertReduced) {
        CardView nextCardView = view.findViewById(R.id.next_concert_card);
        nextCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showManageConcertDialog(concertReduced.getConcertId());
            }
        });

        LinearLayout concertInfoContainer = view.findViewById(R.id.concert_info_container);
        if (concertReduced.getConcertId() != null){

            Utils.responsiveView(nextCardView, 0.85, 0.7 / 1.5, getActivity());
            Utils.responsiveViewWidth(concertInfoContainer, 0.85, getActivity());
            ImageView nextConcertImage = view.findViewById(R.id.next_concert_image);
            Glide.with(context).load(concertReduced.getConcertCoverImage())
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
                    }).into(nextConcertImage);

            TextView concertName = view.findViewById(R.id.concert_name);
            TextView concertDay = view.findViewById(R.id.concert_day);
            TextView concertYear = view.findViewById(R.id.concert_year);

            Calendar concertDate = Helpers.getDateAsCalendar(concertReduced.getDateStarts());

            concertName.setText(concertReduced.getName());


            String concertDayStr = Utils.getMonthSimplified(concertDate.get(Calendar.MONTH)) + " " + concertDate.get(Calendar.DATE);
            concertDay.setText(concertDayStr);

            String concertYearStr = String.valueOf(concertDate.get(Calendar.YEAR));
            concertYear.setText(concertYearStr);
        } else {
            nextCardView.setVisibility(View.GONE);
            concertInfoContainer.setVisibility(View.GONE);
            TextView noConcert = view.findViewById(R.id.no_recent_concert);
            noConcert.setVisibility(View.VISIBLE);
        }

    }

    private void getArtistUserActivity() {
        String userId = userSession.getUser().getId();

        Call<ArrayList<ConcertActivity>> call = Api.getInstance().getAPI().getArtistActivityByUsers(userId);
        call.enqueue(new Callback<ArrayList<ConcertActivity>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertActivity>> call, Response<ArrayList<ConcertActivity>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artist user activity success " + response.body());
                        concertActivityArrayList = response.body();

                        if (concertActivityArrayList.isEmpty()){
                            view.findViewById(R.id.no_comments_yet).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.no_activity_yet).setVisibility(View.VISIBLE);
                        } else {
                            getComments();

                            initActivityList();
                            if (concertActivityCommentsArrayList.isEmpty()){
                                view.findViewById(R.id.no_comments_yet).setVisibility(View.VISIBLE);
                            } else {
                                initCommentsCarousel();
                            }
                        }
                        break;
                    default:
                        Log.d(TAG, "Get artist user activity default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertActivity>> call, Throwable t) {
                Log.d(TAG, "Get artist user activity failure " + t.getLocalizedMessage());
            }
        });
    }

    private void initCommentsCarousel() {
        int size = concertActivityCommentsArrayList.size();

        concertsCommentsOnlyFive = concertActivityCommentsArrayList;
        if (size >= 5){
            concertsCommentsOnlyFive = concertActivityCommentsArrayList.subList(0, 5);
        }


        artistUsersActivityCommentsCarousel  = view.findViewById(R.id.comments);
        artistUsersActivityCommentsCarousel.setSize(concertsCommentsOnlyFive.size());
        artistUsersActivityCommentsCarousel.setResource(R.layout.activity_comments_list);
        artistUsersActivityCommentsCarousel.setAutoPlay(true);
        artistUsersActivityCommentsCarousel.setAutoPlayDelay(3000);
        artistUsersActivityCommentsCarousel.hideIndicator(true);
        artistUsersActivityCommentsCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
        artistUsersActivityCommentsCarousel.setCarouselOffset(OffsetType.START);
        artistUsersActivityCommentsCarousel.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, final int position) {
                /* Get screen size */
                TextView commentDate = view.findViewById(R.id.comment_date);
                TextView comment = view.findViewById(R.id.comment);
                TextView userFirstNameTv = view.findViewById(R.id.user_first_name_letter);
                TextView userName = view.findViewById(R.id.user_name);

                RatingBar ratingBar = view.findViewById(R.id.rating);

                String date = concertsCommentsOnlyFive.get(position).getDate();
                String userComment = concertsCommentsOnlyFive.get(position).getUserComment();
                double userRate = concertsCommentsOnlyFive.get(position).getUserRate();

                String firstNameLetterStr = String.valueOf(concertsCommentsOnlyFive.get(position).getUserName().charAt(0)).toUpperCase();
                userFirstNameTv.setText(firstNameLetterStr);

                Calendar commentDateCalendar = Helpers.getDateAsCalendar(date);
                String commentDay = commentDateCalendar.get(Calendar.DATE) + " " +  Utils.getMonthSimplified(commentDateCalendar.get(Calendar.MONTH)) + " " +  commentDateCalendar.get(Calendar.YEAR);
                commentDate.setText(commentDay);

                comment.setText(userComment);
                ratingBar.setRating((float) userRate);
                userName.setText(concertsCommentsOnlyFive.get(position).getUserName());
            }
        });

        artistUsersActivityCommentsCarousel.show();
    }

    private void initActivityList() {
        int size = concertActivityArrayList.size();

        List<ConcertActivity> concertActivitiesOnlyFive = concertActivityArrayList;
        if (size >= 5){
            concertActivitiesOnlyFive = concertActivityArrayList.subList(0, 5);
        }
        artistUsersActivityAdapter = new ArtistUsersActivityAdapter(context, concertActivitiesOnlyFive);
        activityRecyclerView.setAdapter(artistUsersActivityAdapter);
    }

    private void getComments() {
        concertActivityCommentsArrayList = new ArrayList<>();
        for (ConcertActivity concertActivity : concertActivityArrayList){
            String activityType = concertActivity.getActivityType();

            if (activityType.equals("RATING")){
                String comment = concertActivity.getUserComment();
                double rating = concertActivity.getUserRate();

                if (!comment.isEmpty() ||rating != -1)
                    concertActivityCommentsArrayList.add(concertActivity);
            }
        }
    }


    private void initView(){
        activityRecyclerView = view.findViewById(R.id.users_registered_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        activityRecyclerView.setLayoutManager(mLayoutManager);
        activityRecyclerView.setNestedScrollingEnabled(false);

        concertListBtn = view.findViewById(R.id.list_concerts_btn);

        concertListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.animatedBottomBar.selectTabAt(1, true);
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new ConcertFragment()).addToBackStack(null).commit();
            }
        });
        createConcertBtn = view.findViewById(R.id.create_concert_btn);
        createConcertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CreateConcertActivity.class));
            }
        });

    }
}