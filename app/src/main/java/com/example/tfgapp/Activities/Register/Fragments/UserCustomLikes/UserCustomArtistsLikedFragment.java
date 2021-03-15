package com.example.tfgapp.Activities.Register.Fragments.UserCustomLikes;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgapp.Adapters.CustomUserArtistsAdapter;
import com.example.tfgapp.Adapters.CustomUserMusicStyleAdapter;
import com.example.tfgapp.Entities.Artist.ArtistReducedInfo;
import com.example.tfgapp.Entities.CustomUserLikes.MusicStyle;
import com.example.tfgapp.R;

import java.util.ArrayList;

public class UserCustomArtistsLikedFragment extends Fragment implements CustomUserArtistsAdapter.OnArtistListener {

    private View view;
    private Context context;

    private ArrayList<ArtistReducedInfo> artistReducedInfoArrayList;
    private RecyclerView artistsRecyclerView;
    private CustomUserArtistsAdapter customUserArtistsAdapter;
    private CustomUserArtistsAdapter.OnArtistListener onArtistListener;

    public UserCustomArtistsLikedFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_custom_artists_liked, container, false);
        onArtistListener = this;

        initView();
        return view;
    }

    private void initView(){
        initArtists();

        artistsRecyclerView = view.findViewById(R.id.artists_to_follow_recyclerview);

        customUserArtistsAdapter = new CustomUserArtistsAdapter(context, artistReducedInfoArrayList, onArtistListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        artistsRecyclerView.setLayoutManager(gridLayoutManager);
        artistsRecyclerView.setNestedScrollingEnabled(false);
        artistsRecyclerView.setAdapter(customUserArtistsAdapter);

    }

    private void initArtists(){
        artistReducedInfoArrayList = new ArrayList<>();

        artistReducedInfoArrayList.add(new ArtistReducedInfo("1", "Post Malone", "https://pyxis.nymag.com/v1/imgs/f5f/3ea/3d78db572590823a4e7640ab6346c5ba30-drake.rsquare.w1200.jpg"));
        artistReducedInfoArrayList.add(new ArtistReducedInfo("2", "Travis Scott", "https://pyxis.nymag.com/v1/imgs/f5f/3ea/3d78db572590823a4e7640ab6346c5ba30-drake.rsquare.w1200.jpg"));
        artistReducedInfoArrayList.add(new ArtistReducedInfo("3", "Drake", "https://pyxis.nymag.com/v1/imgs/f5f/3ea/3d78db572590823a4e7640ab6346c5ba30-drake.rsquare.w1200.jpg"));
        artistReducedInfoArrayList.add(new ArtistReducedInfo("4", "Rosalia", "https://pyxis.nymag.com/v1/imgs/f5f/3ea/3d78db572590823a4e7640ab6346c5ba30-drake.rsquare.w1200.jpg"));
        artistReducedInfoArrayList.add(new ArtistReducedInfo("5", "The Weekend", "https://pyxis.nymag.com/v1/imgs/f5f/3ea/3d78db572590823a4e7640ab6346c5ba30-drake.rsquare.w1200.jpg"));
        artistReducedInfoArrayList.add(new ArtistReducedInfo("7", "Robbie Williams", "https://pyxis.nymag.com/v1/imgs/f5f/3ea/3d78db572590823a4e7640ab6346c5ba30-drake.rsquare.w1200.jpg"));
        artistReducedInfoArrayList.add(new ArtistReducedInfo("8", "Bruno Mars", "https://pyxis.nymag.com/v1/imgs/f5f/3ea/3d78db572590823a4e7640ab6346c5ba30-drake.rsquare.w1200.jpg"));
    }

    @Override
    public void onArtistClicked(int position) {

    }
}