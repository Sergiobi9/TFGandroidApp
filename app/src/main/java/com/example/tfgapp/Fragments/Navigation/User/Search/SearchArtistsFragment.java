package com.example.tfgapp.Fragments.Navigation.User.Search;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.tfgapp.Adapters.SelectArtistsAdapter;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Fragments.Artist.ArtistFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchArtistsFragment extends Fragment implements SelectArtistsAdapter.OnArtistListener {

    private final String TAG = "SearchArtistsFragment";
    private Context context;
    private View view;
    private SearchView artistSearch;

    private ArrayList<ArtistSimplified> artistsArrayList = new ArrayList<>();
    private RecyclerView artistsRecyclerView;
    private SelectArtistsAdapter selectArtistsAdapter;
    private SelectArtistsAdapter.OnArtistListener onArtistListener;


    public SearchArtistsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search_artists, container, false);
        context = getContext();

        onArtistListener = this;

        initView();

        getArtists();
        return view;
    }

    private void initView() {
        artistSearch = view.findViewById(R.id.artists_search);
        artistSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                selectArtistsAdapter.filter(arg0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                selectArtistsAdapter.filter(arg0);
                return false;
            }
        });
    }

    private void getArtists(){
        Call<ArrayList<ArtistSimplified>> call = Api.getInstance().getAPI().getAllArtists();
        call.enqueue(new Callback<ArrayList<ArtistSimplified>>() {
            @Override
            public void onResponse(Call<ArrayList<ArtistSimplified>> call, Response<ArrayList<ArtistSimplified>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artists success " + response.body());
                        artistsArrayList = response.body();

                        fillArtists();
                        break;
                    default:
                        Log.d(TAG, "Get artists default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArtistSimplified>> call, Throwable t) {
                Log.d(TAG, "Get artists failure " + t.getLocalizedMessage());
            }
        });
    }

    private void fillArtists(){
        artistsRecyclerView = view.findViewById(R.id.artists_recycler_view);

        selectArtistsAdapter = new SelectArtistsAdapter(context, artistsArrayList, onArtistListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        artistsRecyclerView.setLayoutManager(gridLayoutManager);
        artistsRecyclerView.setNestedScrollingEnabled(false);
        artistsRecyclerView.setAdapter(selectArtistsAdapter);
    }

    @Override
    public void onArtistClicked(int position) {
        ArtistSimplified artist = artistsArrayList.get(position);

        String artistId = artist.getArtistId();
        goArtistProfile(artistId);
    }

    private void goArtistProfile(String artistId) {
        Bundle bundle = new Bundle();
        bundle.putString("artistId", artistId);
        ArtistFragment artistFragment = new ArtistFragment();
        artistFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, artistFragment).addToBackStack(null).commit();
    }
}