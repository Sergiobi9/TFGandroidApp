package com.example.tfgapp.Fragments.Navigation.User.Concert;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.example.tfgapp.Adapters.RatingAdapter;
import com.example.tfgapp.Adapters.TicketsAdapter;
import com.example.tfgapp.Entities.Booking.BookingTicketsList;
import com.example.tfgapp.Entities.Rating.Rating;
import com.example.tfgapp.Entities.Rating.RatingSimplified;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConcertsAssistedFragment extends Fragment implements RatingAdapter.OnRatingListener {

    private View view;
    private Context context;
    private final String TAG = "ConcertsAssistedFragment";

    private ArrayList<RatingSimplified> ratingArrayList = new ArrayList<>();
    private RecyclerView ratingsRecyclerView;
    private RatingAdapter ratingAdapter;
    private RatingAdapter.OnRatingListener onRatingListener;

    public ConcertsAssistedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concerts_assisted, container, false);
        context = getContext();
        onRatingListener = this;

        initView();
        return view;
    }

    private void initView(){

        ratingsRecyclerView = view.findViewById(R.id.asisted_concerts_recicler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ratingsRecyclerView.setLayoutManager(mLayoutManager);
        ratingsRecyclerView.setNestedScrollingEnabled(false);

        getUserConcertRatings();
    }

    private void getUserConcertRatings(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        String currentDate = Helpers.getTimeStamp();
        Call<ArrayList<RatingSimplified>> call = Api.getInstance().getAPI().getUserConcertRatings(userId, currentDate);
        call.enqueue(new Callback<ArrayList<RatingSimplified>>() {
            @Override
            public void onResponse(Call<ArrayList<RatingSimplified>> call, Response<ArrayList<RatingSimplified>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Tickets concerts success " + response.body());
                        ratingArrayList = response.body();

                        if (ratingArrayList != null && ratingArrayList.size() > 0)
                            initRatingsList();
                        else {
                            view.findViewById(R.id.no_ratings_yet).setVisibility(View.VISIBLE);
                            ratingsRecyclerView.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        Log.d(TAG, "Tickets concerts default " + response.code());
                        view.findViewById(R.id.no_ratings_yet).setVisibility(View.VISIBLE);
                        ratingsRecyclerView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RatingSimplified>> call, Throwable t) {
                Log.d(TAG, "Tickets concerts failure " + t.getLocalizedMessage());
                view.findViewById(R.id.no_ratings_yet).setVisibility(View.VISIBLE);
                ratingsRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void initRatingsList(){
        ratingAdapter = new RatingAdapter(context, ratingArrayList, onRatingListener);
        ratingsRecyclerView.setAdapter(ratingAdapter);
    }

    @Override
    public void onRatingClicked(int position) {

    }
}