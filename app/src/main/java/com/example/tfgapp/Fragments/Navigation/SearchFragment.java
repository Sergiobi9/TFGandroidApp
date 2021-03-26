package com.example.tfgapp.Fragments.Navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.tfgapp.Adapters.ConcertSearchAdapter;
import com.example.tfgapp.Adapters.CustomUserArtistsAdapter;
import com.example.tfgapp.Adapters.TicketsAdapter;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Fragments.Navigation.User.ConcertInfoFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements ConcertSearchAdapter.OnConcertListener {

    private final String TAG = "SearchFragment";
    private View view;
    private Context context;
    private SearchView concertsSearch;

    private ArrayList<ConcertReduced> concertsReducedArrayList = new ArrayList<>();
    private RecyclerView concertsRecyclerView;
    private ConcertSearchAdapter concertSearchAdapter;
    private ConcertSearchAdapter.OnConcertListener onConcertListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        onConcertListener = this;

        concertsRecyclerView = view.findViewById(R.id.searched_concerts_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        concertsRecyclerView.setLayoutManager(mLayoutManager);
        concertsRecyclerView.setNestedScrollingEnabled(false);

        concertsSearch = view.findViewById(R.id.concerts_search);
        concertsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                concertSearchAdapter.filter(arg0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                concertSearchAdapter.filter(arg0);
                return false;
            }
        });

        getConcerts();
    }

    private void getConcerts(){
        String currentDate = Helpers.getTimeStamp();
        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getAllConcertsActiveByCurrentDate(currentDate);
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get concerts for search success " + response.body());
                        concertsReducedArrayList = response.body();
                        initConcertsView();
                        break;
                    default:
                        Log.d(TAG, "Get concerts for search default " + response.code());
                        Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertReduced>> call, Throwable t) {
                Log.d(TAG, "Get concerts for search failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
            }
        });
    }

    private void initConcertsView(){
        concertSearchAdapter = new ConcertSearchAdapter(context, concertsReducedArrayList, onConcertListener, getActivity());
        concertsRecyclerView.setAdapter(concertSearchAdapter);
    }


    @Override
    public void onConcertClicked(int position) {
        /* Open fragment specific concert info */
        String concertId = concertsReducedArrayList.get(position).getConcertId();

        openConcertInfo(concertId);
    }

    private void openConcertInfo(String concertId){
        Bundle bundle = new Bundle();
        bundle.putString("concertId", concertId);
        ConcertInfoFragment concertInfoFragment = new ConcertInfoFragment();
        concertInfoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, concertInfoFragment).addToBackStack(null).commit();
    }
}