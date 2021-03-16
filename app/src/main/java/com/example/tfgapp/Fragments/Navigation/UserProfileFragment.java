package com.example.tfgapp.Fragments.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tfgapp.Activities.Login.LoginActivity;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;

public class UserProfileFragment extends Fragment {

    private final String TAG = "UserProfileFragment";
    private View view;
    private Context context;

    private TextView logoutBtn;
    private LinearLayout artistsFollowedLayout, musicStylesFollowedLayout;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        artistsFollowedLayout = view.findViewById(R.id.artists_following_layout);
        musicStylesFollowedLayout = view.findViewById(R.id.music_styles_following_layout);

        Utils.responsiveViewWidth(artistsFollowedLayout, 0.5, getActivity());
        Utils.responsiveViewWidth(musicStylesFollowedLayout, 0.5, getActivity());
        /*logoutBtn = view.findViewById(R.id.logout_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginScreen();
            }
        });*/
    }

    private void goLoginScreen(){
        /* Create first dialog to assure */

        CurrentUser.getInstance(context).setCurrentUser(new UserSession());
        CurrentUser.getInstance(context).setUserLogin(false);

        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
}