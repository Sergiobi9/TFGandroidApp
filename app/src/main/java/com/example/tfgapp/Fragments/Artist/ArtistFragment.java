package com.example.tfgapp.Fragments.Artist;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tfgapp.Entities.Artist.ArtistProfileInfo;
import com.example.tfgapp.Entities.Artist.ArtistReducedInfo;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistFragment extends Fragment {

    private View view;
    private ImageView artistImage;
    private Context context;
    private final String TAG = "ArtistFragment";

    private LinearLayout concertsLayout, followersLayout, sinceLayout;
    private Button followBtn, unfollowBtn;
    private String artistId;
    private TextView artistName;

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

        context = getContext();

        Globals.displayShortToast(getContext(), artistId);

        initView();
        getArtistProfileInfo();
        return view;
    }

    private void getArtistProfileInfo(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();

        Call<ArtistProfileInfo> call = Api.getInstance().getAPI().getArtistInfo(artistId, userId);
        call.enqueue(new Callback<ArtistProfileInfo>() {
            @Override
            public void onResponse(Call<ArtistProfileInfo> call, Response<ArtistProfileInfo> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artist profile info success " + response.body());
                        loadArtistInfoView();
                        break;
                    default:
                        Log.d(TAG, "Get artist profile info default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArtistProfileInfo> call, Throwable t) {
                Log.d(TAG, "Get artist profile info failure " + t.getLocalizedMessage());
            }
        });
    }

    private void loadArtistInfoView(){

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
    }
}