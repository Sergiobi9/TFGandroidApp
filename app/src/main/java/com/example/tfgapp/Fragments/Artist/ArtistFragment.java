package com.example.tfgapp.Fragments.Artist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tfgapp.Adapters.ConcertsAdapter;
import com.example.tfgapp.Entities.Artist.ArtistProfileInfo;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Fragments.Navigation.User.ConcertInfoFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistFragment extends Fragment implements ConcertsAdapter.OnConcertListener {

    private View view;
    private ImageView artistImage;
    private Context context;
    private final String TAG = "ArtistFragment";

    private LinearLayout concertsLayout, followersLayout, sinceLayout;
    private Button followBtn, unfollowBtn;
    private String artistId;
    private TextView artistName, artistMusicStyle, artistFollowers, artistConcertsMade,
            artistSince, artistBio, noRecentConcerts;
    private ImageView spotifyLink, facebookLink, twitterLink, instagramLink, youtubeLink, snapchatLink;

    private ArrayList<ConcertReduced> artistConcertsArrayList = new ArrayList<>();
    private RecyclerView artistConcertsRecyclerView;
    private ConcertsAdapter artistConcertsAdapter;
    private ConcertsAdapter.OnConcertListener onConcertListener;

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
        onConcertListener = this;

        initView();
        return view;
    }

    private void getArtistProfileInfo(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        String currentDate = Helpers.getTimeStamp();

        Call<ArtistProfileInfo> call = Api.getInstance().getAPI().getArtistInfo(currentDate, artistId, userId);
        call.enqueue(new Callback<ArtistProfileInfo>() {
            @Override
            public void onResponse(Call<ArtistProfileInfo> call, Response<ArtistProfileInfo> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artist profile info success " + response.body());
                        loadArtistInfoView(response.body());
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

    private void loadArtistInfoView(ArtistProfileInfo artistProfileInfo){
        Picasso.get().load(artistProfileInfo.getProfileUrl()).transform(new CircleTransform()).into(artistImage);

        artistName.setText(artistProfileInfo.getArtistName());
        artistMusicStyle.setText("Cantante de " + artistProfileInfo.getMusicStyleName());
        artistFollowers.setText(String.valueOf(artistProfileInfo.getFollowers()));
        artistConcertsMade.setText(String.valueOf(artistProfileInfo.getNumberOfConcerts().size()));
        artistSince.setText(Helpers.getDateSince(artistProfileInfo.getMemberSince(), context));
        artistBio.setText(artistProfileInfo.getBio());

        setSocialMediaLinks(spotifyLink, artistProfileInfo.getSpotifyLink());
        setSocialMediaLinks(facebookLink, artistProfileInfo.getFacebookLink());
        setSocialMediaLinks(twitterLink, artistProfileInfo.getTwitterLink());
        setSocialMediaLinks(instagramLink, artistProfileInfo.getInstagramLink());
        setSocialMediaLinks(youtubeLink, artistProfileInfo.getYoutubeLink());
        setSocialMediaLinks(snapchatLink, artistProfileInfo.getSnapchatLink());

        if (artistProfileInfo.getNumberOfConcerts() == null || artistProfileInfo.getNumberOfConcerts().size() <= 0){
            noRecentConcerts.setVisibility(View.VISIBLE);
        }

        if (artistProfileInfo.isFollowing()){
            unfollowBtn.setVisibility(View.VISIBLE);
            followBtn.setVisibility(View.GONE);
        } else {
            unfollowBtn.setVisibility(View.GONE);
            followBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setSocialMediaLinks(ImageView socialMediaIcon, String link){
        if (link == null){
            socialMediaIcon.setVisibility(View.GONE);
        } else {
            socialMediaIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLink(link);
                }
            });
        }
    }

    private void openLink(String link){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        startActivity(i);
    }

    private void initView() {
        concertsLayout = view.findViewById(R.id.concerts_layout);
        followersLayout = view.findViewById(R.id.followers_layout);
        sinceLayout = view.findViewById(R.id.since_layout);

        artistImage = view.findViewById(R.id.artist_image);

        Utils.responsiveView(artistImage, 0.3, 0.3, getActivity());

        Utils.responsiveViewWidth(concertsLayout, 0.3, getActivity());
        Utils.responsiveViewWidth(followersLayout, 0.3, getActivity());
        Utils.responsiveViewWidth(sinceLayout, 0.3, getActivity());

        followBtn = view.findViewById(R.id.follow_btn);
        unfollowBtn = view.findViewById(R.id.unfollow_btn);

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followArtist(true);
            }
        });

        unfollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followArtist(false);
            }
        });

        artistName = view.findViewById(R.id.artist_name);
        artistMusicStyle = view.findViewById(R.id.artist_music_style);
        artistFollowers = view.findViewById(R.id.artist_followers);
        artistConcertsMade = view.findViewById(R.id.concerts_made);
        artistSince = view.findViewById(R.id.artist_since);
        artistBio = view.findViewById(R.id.artist_bio);

        spotifyLink = view.findViewById(R.id.spotify_link);
        facebookLink = view.findViewById(R.id.facebook_link);
        twitterLink = view.findViewById(R.id.twitter_link);
        instagramLink = view.findViewById(R.id.instagram_link);
        youtubeLink = view.findViewById(R.id.youtube_link);
        snapchatLink = view.findViewById(R.id.snapchat_link);

        noRecentConcerts = view.findViewById(R.id.no_concerts_text_view);

        artistConcertsRecyclerView = view.findViewById(R.id.artist_next_concerts_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        artistConcertsRecyclerView.setLayoutManager(mLayoutManager);
        artistConcertsRecyclerView.setNestedScrollingEnabled(false);

        getArtistProfileInfo();
        getConcerts();
    }
    
    private void getConcerts(){
        String currentDate = Helpers.getTimeStamp();
        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getArtistConcerts(artistId, currentDate);
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artist concerts success " + response.body());
                        artistConcertsArrayList = response.body();
                        initArtistConcertsList();
                        break;
                    default:
                        Log.d(TAG, "Get artist concerts default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertReduced>> call, Throwable t) {
                Log.d(TAG, "Get artist concerts onFaulure " + t.getLocalizedMessage());
            }
        });
    }

    private void initArtistConcertsList() {
        if (!artistConcertsArrayList.isEmpty()){
            artistConcertsRecyclerView.setVisibility(View.VISIBLE);
            artistConcertsAdapter = new ConcertsAdapter(context, artistConcertsArrayList, onConcertListener, getActivity());
            artistConcertsRecyclerView.setAdapter(artistConcertsAdapter);
            noRecentConcerts.setVisibility(View.GONE);
        } else {
            noRecentConcerts.setVisibility(View.VISIBLE);
            artistConcertsRecyclerView.setVisibility(View.GONE);
        }
    }

    private void followArtist(boolean follow){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();

        Call<ResponseBody> call = Api.getInstance().getAPI().followArtist(artistId, userId, follow);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Follow artist success " + response.body());
                        if(follow){
                            followBtn.setVisibility(View.GONE);
                            unfollowBtn.setVisibility(View.VISIBLE);
                        } else {
                            followBtn.setVisibility(View.VISIBLE);
                            unfollowBtn.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        Log.d(TAG, "Follow artist default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Follow artist onFaulure " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onConcertClicked(int position) {
        Bundle bundle = new Bundle();
        String concertId = artistConcertsArrayList.get(position).getConcertId();
        bundle.putString("concertId", concertId);
        ConcertInfoFragment concertInfoFragment = new ConcertInfoFragment();
        concertInfoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, concertInfoFragment).addToBackStack(null).commit();
    }
}