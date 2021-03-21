package com.example.tfgapp.Activities.Concert.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgapp.Activities.Register.RegisterCustomLikesActivity;
import com.example.tfgapp.Adapters.CustomUserArtistsAdapter;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConcertArtistsFragment extends Fragment implements CustomUserArtistsAdapter.OnArtistListener {

    private View view;
    private Context context;

    private final String TAG = "ConcertArtistsFragment";
    private ArrayList<ArtistSimplified> artistReducedInfoArrayList = new ArrayList<>();
    private RecyclerView artistsRecyclerView;
    private CustomUserArtistsAdapter customUserArtistsAdapter;
    private CustomUserArtistsAdapter.OnArtistListener onArtistListener;

    public ConcertArtistsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concert_artists, container, false);
        context = getContext();

        getArtists();
        return view;
    }

    private void getArtists(){
        artistReducedInfoArrayList = new ArrayList<>();
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();

        Call<ArrayList<ArtistSimplified>> call = Api.getInstance().getAPI().getAllArtists(userId);
        call.enqueue(new Callback<ArrayList<ArtistSimplified>>() {
            @Override
            public void onResponse(Call<ArrayList<ArtistSimplified>>call, Response<ArrayList<ArtistSimplified>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artists by styles success " + response.body());

                        artistReducedInfoArrayList = response.body();
                        initArtistsView();
                        break;
                    default:
                        Log.d(TAG, "Get artists by styles default " + response.code());
                        Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArtistSimplified>> call, Throwable t) {
                Log.d(TAG, "Get artists by styles failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
            }
        });
    }

    private void initArtistsView(){
        artistsRecyclerView = view.findViewById(R.id.concerts_artist_recycler_view);

        customUserArtistsAdapter = new CustomUserArtistsAdapter(context, artistReducedInfoArrayList, onArtistListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        artistsRecyclerView.setLayoutManager(gridLayoutManager);
        artistsRecyclerView.setNestedScrollingEnabled(false);
        artistsRecyclerView.setAdapter(customUserArtistsAdapter);
    }

    @Override
    public void onArtistClicked(int position) {
        boolean isSelected = !artistReducedInfoArrayList.get(position).isSelected();
        String artistId = artistReducedInfoArrayList.get(position).getArtistId();
        artistReducedInfoArrayList.get(position).setSelected(isSelected);

        ArrayList<String> artistsIdsSelected = RegisterCustomLikesActivity.getArtistsSelectedIdsArrayList();
        if (isSelected)
            artistsIdsSelected.add(artistId);
        else
            artistsIdsSelected.remove(artistId);

        RegisterCustomLikesActivity.setArtistsSelectedIdsArrayList(artistsIdsSelected);
        customUserArtistsAdapter.notifyItemChanged(position);
    }
}