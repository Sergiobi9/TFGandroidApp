package com.example.tfgapp.Fragments.Navigation.User.Concert;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgapp.Adapters.RatingAdapter;
import com.example.tfgapp.Adapters.TicketsAdapter;
import com.example.tfgapp.Entities.Booking.BookingTicketsList;
import com.example.tfgapp.Entities.Rating.Rating;
import com.example.tfgapp.R;

import java.util.ArrayList;

public class ConcertsAssistedFragment extends Fragment implements RatingAdapter.OnRatingListener {

    private View view;
    private Context context;

    private ArrayList<Rating> ratingArrayList = new ArrayList<>();
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
        ratingArrayList.add(new Rating());
        ratingArrayList.add(new Rating());

        ratingsRecyclerView = view.findViewById(R.id.asisted_concerts_recicler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ratingsRecyclerView.setLayoutManager(mLayoutManager);
        ratingsRecyclerView.setNestedScrollingEnabled(false);

        ratingAdapter = new RatingAdapter(context, ratingArrayList, onRatingListener);
        ratingsRecyclerView.setAdapter(ratingAdapter);
    }

    @Override
    public void onRatingClicked(int position) {

    }
}