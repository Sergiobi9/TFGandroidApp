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
import com.example.tfgapp.Adapters.CustomUserMusicStyleAdapter;
import com.example.tfgapp.Entities.CustomUserLikes.MusicStyle;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCustomMusicStylesFragment extends Fragment implements CustomUserMusicStyleAdapter.OnMusicStyleListener {

    private View view;
    private Context context;
    private final String TAG = "MusicStylesFragment";

    private Button continueBtn;

    private ArrayList<MusicStyle> musicStyleArrayList;
    private RecyclerView musicStyleRecyclerView;
    private CustomUserMusicStyleAdapter customUserMusicStyleAdapter;
    private CustomUserMusicStyleAdapter.OnMusicStyleListener onMusicStyleListener;

    public UserCustomMusicStylesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_custom_music_styles, container, false);
        RegisterCustomLikesActivity.setIsUserPreferencesFirstScreen(true);
        context = getContext();
        onMusicStyleListener = this;

        initView();
        return view;
    }

    private void initView() {
        musicStyleRecyclerView = view.findViewById(R.id.music_style_recyclerview);

        getMusicStyles();

        continueBtn = view.findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> musicStylesIds = RegisterCustomLikesActivity.getMusicStylesIdsSelected();

                if (musicStylesIds.size() > 0) {
                    RegisterCustomLikesActivity.setIsUserPreferencesFirstScreen(false);
                    getFragmentManager().beginTransaction().replace(R.id.register_user_likes_fragment, new UserCustomArtistsLikedFragment()).addToBackStack(null).commit();
                }
                else {
                    Globals.displayShortToast(context, "Selecciona almenos un estilo musical");
                }
            }
        });
    }

    private void getMusicStyles() {
        Call<ArrayList<MusicStyle>> call = Api.getInstance().getAPI().getMusicStyles();
        call.enqueue(new Callback<ArrayList<MusicStyle>>() {
            @Override
            public void onResponse(Call<ArrayList<MusicStyle>> call, Response<ArrayList<MusicStyle>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get music styles success " + response.body());

                        musicStyleArrayList = response.body();
                        initMusicStylesView();
                        break;
                    default:
                        Log.d(TAG, "Get music styles default " + response.code());
                        Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MusicStyle>> call, Throwable t) {
                Log.d(TAG, "Get music styles failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
            }
        });
    }

    private void initMusicStylesView() {
        customUserMusicStyleAdapter = new CustomUserMusicStyleAdapter(context, musicStyleArrayList, onMusicStyleListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        musicStyleRecyclerView.setLayoutManager(gridLayoutManager);
        musicStyleRecyclerView.setNestedScrollingEnabled(false);
        musicStyleRecyclerView.setAdapter(customUserMusicStyleAdapter);
    }

    @Override
    public void onMusicStyleClicked(int position) {
        Log.d(TAG, "WTF");
        boolean isSelected = !musicStyleArrayList.get(position).isSelected();
        String musicStyleId = musicStyleArrayList.get(position).id;
        musicStyleArrayList.get(position).setSelected(isSelected);

        ArrayList<String> musicStylesSelected = RegisterCustomLikesActivity.getMusicStylesIdsSelected();
        if (isSelected)
            musicStylesSelected.add(musicStyleId);
        else
            musicStylesSelected.remove(musicStyleId);

        RegisterCustomLikesActivity.setMusicStylesIdsSelected(musicStylesSelected);
        customUserMusicStyleAdapter.notifyItemChanged(position);
    }
}