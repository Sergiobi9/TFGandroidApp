package com.example.tfgapp.Fragments.Artist;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

public class ArtistFragment extends Fragment {

    private View view;
    private ImageView artistImage;

    private LinearLayout concertsLayout, followersLayout, sinceLayout;
    private Button followBtn, unfollowBtn;
    private String artistId;

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
        artistId = this.getArguments().getString("artistId");

        Globals.displayShortToast(getContext(), artistId);

        initView();
        return view;
    }

    private void initView() {
        String imageUrl = "https://i.pinimg.com/564x/fc/4c/45/fc4c4546509202ab3d0c0fc91e8c4d69.jpg";

        concertsLayout = view.findViewById(R.id.concerts_layout);
        followersLayout = view.findViewById(R.id.followers_layout);
        sinceLayout = view.findViewById(R.id.since_layout);

        artistImage = view.findViewById(R.id.artist_image);

        Utils.responsiveView(artistImage, 0.3, 0.3, getActivity());

        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(artistImage);

        Utils.responsiveViewWidth(concertsLayout, 0.3, getActivity());
        Utils.responsiveViewWidth(followersLayout, 0.3, getActivity());
        Utils.responsiveViewWidth(sinceLayout, 0.3, getActivity());

        followBtn = view.findViewById(R.id.follow_btn);
        unfollowBtn = view.findViewById(R.id.unfollow_btn);

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followBtn.setVisibility(View.GONE);
                unfollowBtn.setVisibility(View.VISIBLE);
            }
        });

        unfollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfollowBtn.setVisibility(View.GONE);
                followBtn.setVisibility(View.VISIBLE);
            }
        });

        getArtistInfo();
    }

    private void getArtistInfo(){

    }
}