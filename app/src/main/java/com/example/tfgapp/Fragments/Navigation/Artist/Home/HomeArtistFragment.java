package com.example.tfgapp.Fragments.Navigation.Artist.Home;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tfgapp.Adapters.ArtistUsersActivityAdapter;
import com.example.tfgapp.Adapters.ConcertsAdapter;
import com.example.tfgapp.Entities.Concert.ConcertActivity;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.R;

import java.util.ArrayList;
import java.util.Arrays;
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

    private RecyclerView activityRecyclerView;
    private ArtistUsersActivityAdapter artistUsersActivityAdapter;

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