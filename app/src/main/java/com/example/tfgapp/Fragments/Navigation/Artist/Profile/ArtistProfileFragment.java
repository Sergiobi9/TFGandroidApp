package com.example.tfgapp.Fragments.Navigation.Artist.Profile;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tfgapp.Activities.Login.LoginActivity;
import com.example.tfgapp.Entities.Artist.ArtistInfo;
import com.example.tfgapp.Entities.Artist.ArtistProfileInfo;
import com.example.tfgapp.Entities.Artist.ArtistUserProfile;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistProfileFragment extends Fragment {

    private final String TAG = "ArtistProfileFragment";
    private View view;
    private TextView logoutBtn;
    private Button editProfile;
    private ArtistUserProfile artistInfo;
    private Context context;


    public ArtistProfileFragment() {
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
        view = inflater.inflate(R.layout.fragment_artist_profile, container, false);
        context = getContext();

        intiView();
        getArtistProfile();
        return view;
    }

    private void getArtistProfile(){
        String artistId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        Call<ArtistUserProfile> call = Api.getInstance().getAPI().getArtistUserProfile(artistId);
        call.enqueue(new Callback<ArtistUserProfile>() {
            @Override
            public void onResponse(Call<ArtistUserProfile> call, Response<ArtistUserProfile> response) {
                switch (response.code()) {
                    case 200:
                        artistInfo = response.body();
                        fillArtistProfile();
                        break;
                    default:
                        Log.d(TAG, "Get user default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArtistUserProfile> call, Throwable t) {
                Log.d(TAG, "Get user failure " + t.getLocalizedMessage());
            }
        });
    }

    private void fillArtistProfile() {
        ImageView artistImage = view.findViewById(R.id.artist_profile_image);
        Picasso.get().load(artistInfo.getProfileUrl()).transform(new CircleTransform()).into(artistImage);

        TextView artistName = view.findViewById(R.id.artist_name);
        artistName.setText(artistInfo.getArtistName());

        TextView musicStyle = view.findViewById(R.id.artist_music_style);
        musicStyle.setText("Cantante de " + artistInfo.getMusicStyleName());
        Utils.responsiveViewWidth(artistImage, 0.35, getActivity());
    }

    private void intiView(){
        logoutBtn = view.findViewById(R.id.logout_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginScreen();
            }
        });

    }

    private void goLoginScreen() {

        CurrentUser.getInstance(context).setCurrentUser(new UserSession());
        CurrentUser.getInstance(context).setUserLogin(false);

        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
}