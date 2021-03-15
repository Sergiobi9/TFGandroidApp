package com.example.tfgapp.Activities.Register.Fragments.UserCustomLikes;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tfgapp.Activities.Register.Fragments.UserAccount.RegisterNameFragment;
import com.example.tfgapp.Adapters.CustomUserMusicStyleAdapter;
import com.example.tfgapp.Adapters.TicketsAdapter;
import com.example.tfgapp.Entities.CustomUserLikes.MusicStyle;
import com.example.tfgapp.R;

import java.util.ArrayList;

public class UserCustomMusicStylesFragment extends Fragment implements CustomUserMusicStyleAdapter.OnMusicStyleListener {

    private View view;
    private Context context;

    private Button continueBtn;

    private ArrayList<MusicStyle> musicStyleArrayList;
    private RecyclerView musicStyleRecyclerView;
    private CustomUserMusicStyleAdapter customUserMusicStyleAdapter;
    private CustomUserMusicStyleAdapter.OnMusicStyleListener onMusicStyleListener;

    public UserCustomMusicStylesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_custom_music_styles, container, false);
        context = getContext();
        onMusicStyleListener = this;

        initView();
        return view;
    }

    private void initView(){
        initMusicStyles();
        musicStyleRecyclerView = view.findViewById(R.id.music_style_recyclerview);

        customUserMusicStyleAdapter = new CustomUserMusicStyleAdapter(context, musicStyleArrayList, onMusicStyleListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        musicStyleRecyclerView.setLayoutManager(gridLayoutManager);
        musicStyleRecyclerView.setNestedScrollingEnabled(false);
        musicStyleRecyclerView.setAdapter(customUserMusicStyleAdapter);

        continueBtn = view.findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.register_user_likes_fragment, new UserCustomArtistsLikedFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void initMusicStyles(){
        musicStyleArrayList = new ArrayList<>();

        musicStyleArrayList.add(new MusicStyle("1", "Trap", "https://i.scdn.co/image/29180f7f9c945e04de9b39cb5188f6a9171ae485"));
        musicStyleArrayList.add(new MusicStyle("2", "Reggae", "https://i.scdn.co/image/29180f7f9c945e04de9b39cb5188f6a9171ae485"));
        musicStyleArrayList.add(new MusicStyle("3", "Dance", "https://i.scdn.co/image/29180f7f9c945e04de9b39cb5188f6a9171ae485"));
        musicStyleArrayList.add(new MusicStyle("4", "Pop", "https://i.scdn.co/image/29180f7f9c945e04de9b39cb5188f6a9171ae485"));
        musicStyleArrayList.add(new MusicStyle("5", "Rock", "https://i.scdn.co/image/29180f7f9c945e04de9b39cb5188f6a9171ae485"));
        musicStyleArrayList.add(new MusicStyle("6", "Jazz", "https://i.scdn.co/image/29180f7f9c945e04de9b39cb5188f6a9171ae485"));
        musicStyleArrayList.add(new MusicStyle("7", "Blue", "https://i.scdn.co/image/29180f7f9c945e04de9b39cb5188f6a9171ae485"));
        musicStyleArrayList.add(new MusicStyle("8", "Folklore", "https://i.scdn.co/image/29180f7f9c945e04de9b39cb5188f6a9171ae485"));
    }

    @Override
    public void onMusicStyleClicked(int position) {

    }
}