package com.example.tfgapp.Activities.Concert.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tfgapp.R;
import com.theartofdev.edmodo.cropper.CropImage;

public class ConcertCoverFragment extends Fragment {

    private View view;
    private Context context;
    private LinearLayout imagePicker, photosLayout;


    public ConcertCoverFragment() {
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
        view = inflater.inflate(R.layout.fragment_concert_cover, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        photosLayout = view.findViewById(R.id.cover_photos_layout);
        imagePicker = view.findViewById(R.id.cover_picker);

        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(getActivity());
            }
        });
    }
}