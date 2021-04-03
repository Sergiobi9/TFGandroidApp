package com.example.tfgapp.Fragments.Navigation.Artist.Home;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Adapters.ArtistUsersActivityAdapter;
import com.example.tfgapp.Adapters.ConcertsAdapter;
import com.example.tfgapp.Entities.Concert.ConcertActivity;
import com.example.tfgapp.Entities.Rating.Rating;
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
        return view;
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

                        getComments();

                        initActivityList();
                        initCommentsCarousel();
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
        welcomeTv = view.findViewById(R.id.warm_welcome);

        String warmWelcomeText = "";
        warmWelcomeText = getIntervalDay(context, userSession);
        String userFullName = userSession.getUser().getFirstName();
        if (userFullName != null) {
            userFullName =  userFullName.split("\\s+")[0];
        }
        warmWelcomeText = warmWelcomeText + ", " + userFullName;

        welcomeTv.setText(warmWelcomeText);

        activityRecyclerView = view.findViewById(R.id.users_registered_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        activityRecyclerView.setLayoutManager(mLayoutManager);
        activityRecyclerView.setNestedScrollingEnabled(false);

    }
}