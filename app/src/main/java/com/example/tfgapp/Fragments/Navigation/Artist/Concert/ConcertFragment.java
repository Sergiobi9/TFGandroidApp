package com.example.tfgapp.Fragments.Navigation.Artist.Concert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tfgapp.Activities.Concert.CreateConcertActivity;
import com.example.tfgapp.R;


public class ConcertFragment extends Fragment {

    private LinearLayout addConcertBtn;
    private View view;
    private Context context;

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

        return view;
    }

    private void initView(){
        addConcertBtn = view.findViewById(R.id.add_concert);
        addConcertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CreateConcertActivity.class));
            }
        });
    }
}