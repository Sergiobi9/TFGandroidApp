package com.example.tfgapp.Activities.Register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tfgapp.Activities.Register.Fragments.UserAccount.RegisterEmailFragment;
import com.example.tfgapp.Activities.Register.Fragments.UserCustomLikes.UserCustomMusicStylesFragment;
import com.example.tfgapp.Adapters.TicketsAdapter;
import com.example.tfgapp.Entities.Concert.ConcertHome;
import com.example.tfgapp.R;

import java.util.ArrayList;

public class RegisterCustomLikesActivity extends AppCompatActivity {

    private static String userId;
    private static ArrayList<String> musicStylesIdsSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_custom_likes);

        getSupportFragmentManager().beginTransaction().replace(R.id.register_user_likes_fragment, new UserCustomMusicStylesFragment()).addToBackStack(null).commit();
    }

    public static ArrayList<String> getMusicStylesIdsSelected(){
        return musicStylesIdsSelected;
    }

    public static void setMusicStylesIdsSelected(ArrayList<String> musicStylesIds){
        musicStylesIdsSelected = musicStylesIds;
    }
}