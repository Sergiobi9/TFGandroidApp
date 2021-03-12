package com.example.tfgapp.Activities.Register.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tfgapp.R;

public class RegisterBirthdayFragment extends Fragment {

    private Context context;
    private View view;

    public RegisterBirthdayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register_birthday, container, false);

        initView();
        return view;
    }
    private void initView(){

    }
}