package com.example.tfgapp.Fragments.Navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfgapp.Fragments.Navigation.User.Search.SearchArtistsFragment;
import com.example.tfgapp.Fragments.Navigation.User.Search.SearchConcertFragment;
import com.example.tfgapp.R;

public class SearchFragment extends Fragment {

    private View view;
    private Context context;


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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initView();
        return view;
    }

    private void initView(){
        String concertsImage = "https://townsquare.media/site/366/files/2019/02/post_malone_red_hot_chili_peppers_2019_grammys.jpg?w=980&q=75";
        String artistsImage = "https://i.pinimg.com/originals/46/60/53/466053e7d70f65eca9791d239059989b.jpg";


    }

    private void goSearchConcerts(){
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, new SearchConcertFragment()).addToBackStack(null).commit();
    }

    private void goSearchArtists(){
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, new SearchArtistsFragment()).addToBackStack(null).commit();
    }
}