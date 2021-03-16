package com.example.tfgapp.Activities.Register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.tfgapp.Activities.MainActivity;
import com.example.tfgapp.Activities.Register.Fragments.UserAccount.RegisterEmailFragment;
import com.example.tfgapp.Activities.Register.Fragments.UserCustomLikes.UserCustomMusicStylesFragment;
import com.example.tfgapp.Adapters.TicketsAdapter;
import com.example.tfgapp.Entities.Concert.ConcertHome;
import com.example.tfgapp.Entities.User.UserPreferences;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCustomLikesActivity extends AppCompatActivity {

    private static final String TAG = "RegisterLikesActivity";
    private static ArrayList<String> musicStylesIdsSelected = new ArrayList<>();
    private static ArrayList<String> artistsSelectedIdsArrayList = new ArrayList<>();

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

    public static ArrayList<String> getArtistsSelectedIdsArrayList() {
        return artistsSelectedIdsArrayList;
    }

    public static void setArtistsSelectedIdsArrayList(ArrayList<String> artistsIds) {
        artistsSelectedIdsArrayList = artistsIds;
    }

    public static void saveUserPreferences(Context context){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        UserPreferences userPreferences = new UserPreferences(userId, artistsSelectedIdsArrayList, musicStylesIdsSelected);

        Call<UserPreferences> call = Api.getInstance().getAPI().saveUserPreferences(userPreferences);
        call.enqueue(new Callback<UserPreferences>() {
            @Override
            public void onResponse(Call<UserPreferences>call, Response<UserPreferences> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Save user preferences success " + response.body());
                        goMainScreen(context);
                        break;
                    default:
                        Log.d(TAG, "Save user preferences default " + response.code());
                        Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserPreferences> call, Throwable t) {
                Log.d(TAG, "Save user preferences failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
            }
        });
    }

    private static void goMainScreen(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}