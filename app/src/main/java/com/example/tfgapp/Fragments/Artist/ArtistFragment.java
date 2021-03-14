package com.example.tfgapp.Fragments.Artist;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

public class ArtistFragment extends Fragment {

    private View view;
    private ImageView artistImage;

    private int screenWidth;

    public ArtistFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_artist, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        initView();
        return view;
    }

    private void initView() {
        String imageUrl = "https://i0.wp.com/www.primerafila.cat/wp-content/uploads/2021/02/post-malone.jpg?fit=1024%2C768&ssl=1";

        artistImage = view.findViewById(R.id.artist_image);

        ViewGroup.LayoutParams params = artistImage.getLayoutParams();
        params.height = (int) (screenWidth * 0.30);
        params.width = (int) (screenWidth * 0.30);
        artistImage.setLayoutParams(params);

        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(artistImage);
    }
}