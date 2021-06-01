package com.example.tfgapp.Activities.Concert.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tfgapp.Adapters.ConcertTicketsRegisterPricingAdapter;
import com.example.tfgapp.Adapters.SelectArtistsAdapter;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Concert.Pricing.ConcertIntervalPricing;
import com.example.tfgapp.R;

import java.util.ArrayList;

public class ConcertIntervalPricingFragment extends Fragment {

    private View view;
    private Context context;

    private TextView addPricing;
    private ArrayList<ConcertIntervalPricing> concertIntervalPricing = new ArrayList<>();
    private RecyclerView concertPricingRecyclerView;
    private ConcertTicketsRegisterPricingAdapter concertTicketsRegisterPricingAdapter;

    public ConcertIntervalPricingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_concert_interval_pricing, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        concertIntervalPricing.add(new ConcertIntervalPricing());

        concertPricingRecyclerView = view.findViewById(R.id.concert_assistance_recyclerview);

        concertTicketsRegisterPricingAdapter = new ConcertTicketsRegisterPricingAdapter(getContext(), concertIntervalPricing, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        concertPricingRecyclerView.setLayoutManager(mLayoutManager);
        concertPricingRecyclerView.setNestedScrollingEnabled(false);
        concertPricingRecyclerView.setAdapter(concertTicketsRegisterPricingAdapter);

        addPricing = view.findViewById(R.id.add_new_pricing);
        addPricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concertIntervalPricing.add(new ConcertIntervalPricing());
                concertTicketsRegisterPricingAdapter.notifyDataSetChanged();
            }
        });
    }
}