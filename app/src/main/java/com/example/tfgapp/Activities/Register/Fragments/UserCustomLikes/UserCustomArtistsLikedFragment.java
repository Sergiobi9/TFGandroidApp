package com.example.tfgapp.Activities.Register.Fragments.UserCustomLikes;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tfgapp.Activities.Register.RegisterCustomLikesActivity;
import com.example.tfgapp.Adapters.CustomUserArtistsAdapter;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCustomArtistsLikedFragment extends Fragment implements CustomUserArtistsAdapter.OnArtistListener {

    private View view;
    private Context context;
    private final String TAG = "ArtistLikesFragment";

    private ArrayList<ArtistSimplified> artistReducedInfoArrayList = new ArrayList<>();
    private RecyclerView artistsRecyclerView;
    private CustomUserArtistsAdapter customUserArtistsAdapter;
    private CustomUserArtistsAdapter.OnArtistListener onArtistListener;

    private Button continueBtn;

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
        context = getContext();
        onArtistListener = this;

        initView();
        return view;
    }

    private void initView(){
        getArtists();

        continueBtn = view.findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> artistsSelected = RegisterCustomLikesActivity.getArtistsSelectedIdsArrayList();
                if (artistsSelected.size() > 0)
                    RegisterCustomLikesActivity.saveUserPreferences(context);
                else
                    Globals.displayShortToast(context, "Selecciona almenos tres artistas");
            }
        });
    }

    private void getArtists(){
        artistReducedInfoArrayList = new ArrayList<>();
        ArrayList<String> musicStylesIds = RegisterCustomLikesActivity.getMusicStylesIdsSelected();

        Call<ArrayList<ArtistSimplified>> call = Api.getInstance().getAPI().getArtistsByMusicStylesSelected(musicStylesIds);
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
                        Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArtistSimplified>> call, Throwable t) {
                Log.d(TAG, "Get artists by styles failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
            }
        });
    }

    private void initArtistsView(){
        artistsRecyclerView = view.findViewById(R.id.artists_to_follow_recyclerview);

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