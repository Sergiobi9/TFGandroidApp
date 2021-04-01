package com.example.tfgapp.Fragments.Navigation.User;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tfgapp.Activities.Login.LoginActivity;
import com.example.tfgapp.Activities.MainActivity;
import com.example.tfgapp.Adapters.ArtistFollowingAdapter;
import com.example.tfgapp.Adapters.ConcertSearchAdapter;
import com.example.tfgapp.Adapters.CustomUserMusicStyleAdapter;
import com.example.tfgapp.Adapters.MusicStyleFollowingAdapter;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.CustomUserLikes.MusicStyle;
import com.example.tfgapp.Entities.Rating.RatingSimplified;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Fragments.Legal.AboutAppFragment;
import com.example.tfgapp.Fragments.Legal.ConditionTermsFragment;
import com.example.tfgapp.Fragments.Legal.PrivacyPolicyFragment;
import com.example.tfgapp.Fragments.Navigation.User.Concert.ConcertsAssistedFragment;
import com.example.tfgapp.Fragments.Navigation.User.EditProfile.EditUserProfileFragment;
import com.example.tfgapp.Fragments.Navigation.User.Tickets.TicketsFragment;
import com.example.tfgapp.Fragments.Navigation.User.Tickets.TicketsQRFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {

    private final String TAG = "UserProfileFragment";
    private View view;
    private Context context;
    private User user;

    private TextView logoutBtn;
    private LinearLayout artistsFollowedLayout, musicStylesFollowedLayout;
    private Button editProfile;

    private ArrayList<ArtistSimplified> artistsFollowing = new ArrayList<>();
    private ArrayList<MusicStyle> musicStylesFollowing = new ArrayList<>();

    private TextView myTickets, assistedEvents, aboutApp, policy, terms;
    private TextView firstNameLetter, userName, userEmail, deleteAccountBtn;
    private TextView artistsFollowingCounter, musicStylesFollowingCounter;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        context = getContext();

        initView();

        getUserInfo();
        getArtistsFollowing();
        getMusicStylesFollowing();

        return view;
    }

    private void getArtistsFollowing(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        Call<ArrayList<ArtistSimplified>> call = Api.getInstance().getAPI().getArtistsFollowingByUserId(userId);
        call.enqueue(new Callback<ArrayList<ArtistSimplified>>() {
            @Override
            public void onResponse(Call<ArrayList<ArtistSimplified>> call, Response<ArrayList<ArtistSimplified>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artists following success " + response.body());
                        artistsFollowing = response.body();
                        artistsFollowingCounter.setText(String.valueOf(artistsFollowing.size()));
                        break;
                    default:
                        Log.d(TAG, "Get artists following default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArtistSimplified>> call, Throwable t) {
                Log.d(TAG, "Get artists following failure " + t.getLocalizedMessage());
            }
        });
    }

    private void getMusicStylesFollowing(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        Call<ArrayList<MusicStyle>> call = Api.getInstance().getAPI().getMusicStylesFollowingByUserId(userId);
        call.enqueue(new Callback<ArrayList<MusicStyle>>() {
            @Override
            public void onResponse(Call<ArrayList<MusicStyle>> call, Response<ArrayList<MusicStyle>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get artists following success " + response.body());
                        musicStylesFollowing = response.body();
                        musicStylesFollowingCounter.setText(String.valueOf(musicStylesFollowing.size()));
                        break;
                    default:
                        Log.d(TAG, "Get artists following default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MusicStyle>> call, Throwable t) {
                Log.d(TAG, "Get artists following failure " + t.getLocalizedMessage());
            }
        });
    }

    private void getUserInfo(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        Call<User> call = Api.getInstance().getAPI().getUser(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get user success " + response.body());
                        user = response.body();
                        fillUserInformation();
                        break;
                    default:
                        Log.d(TAG, "Get user default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Get user failure " + t.getLocalizedMessage());
            }
        });
    }

    private void fillUserInformation(){
        String firstNameLetterStr = String.valueOf(user.getFirstName().charAt(0)).toUpperCase();
        firstNameLetter.setText(firstNameLetterStr);

        userName.setText(user.getFirstName());
        userEmail.setText(user.getEmail());
    }

    private void initView(){
        artistsFollowedLayout = view.findViewById(R.id.artists_following_layout);

        artistsFollowedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openArtistsFollowedDialog();
            }
        });
        musicStylesFollowedLayout = view.findViewById(R.id.music_styles_following_layout);
        musicStylesFollowedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMusicStylesFollowedDialog();
            }
        });

        Utils.responsiveViewWidth(artistsFollowedLayout, 0.5, getActivity());
        Utils.responsiveViewWidth(musicStylesFollowedLayout, 0.5, getActivity());

        editProfile = view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",  user);

                EditUserProfileFragment editUserProfileFragment = new EditUserProfileFragment();
                editUserProfileFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, editUserProfileFragment).addToBackStack(null).commit();
            }
        });

        artistsFollowingCounter = view.findViewById(R.id.artists_following);
        musicStylesFollowingCounter = view.findViewById(R.id.music_styles_following);

        myTickets = view.findViewById(R.id.tickets);
        myTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.animatedBottomBar.selectTabAt(3, true);
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new TicketsFragment()).addToBackStack(null).commit();
            }
        });

        assistedEvents = view.findViewById(R.id.assisted_events);
        assistedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new ConcertsAssistedFragment()).addToBackStack(null).commit();
            }
        });

        aboutApp = view.findViewById(R.id.about_app);
        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new AboutAppFragment()).addToBackStack(null).commit();
            }
        });

        policy = view.findViewById(R.id.policy);
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new PrivacyPolicyFragment()).addToBackStack(null).commit();
            }
        });

        terms = view.findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new ConditionTermsFragment()).addToBackStack(null).commit();
            }
        });
        logoutBtn = view.findViewById(R.id.logout_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginScreen();
            }
        });

        deleteAccountBtn = view.findViewById(R.id.delete_account);
        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountDialog();
            }
        });

        firstNameLetter = view.findViewById(R.id.user_first_name_letter);
        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
    }

    private void openMusicStylesFollowedDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_following, null);
        dialog.setContentView(view);

        TextView title = view.findViewById(R.id.title);
        title.setText("Géneros seguidos");

        RecyclerView musicStyleFollowingRecyclerView;
        MusicStyleFollowingAdapter musicStyleFollowingAdapter;

        musicStyleFollowingRecyclerView = view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        musicStyleFollowingRecyclerView.setLayoutManager(mLayoutManager);
        musicStyleFollowingRecyclerView.setNestedScrollingEnabled(false);

        musicStyleFollowingAdapter = new MusicStyleFollowingAdapter(context, musicStylesFollowing);
        musicStyleFollowingRecyclerView.setAdapter(musicStyleFollowingAdapter);

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
    }

    private void openArtistsFollowedDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_following, null);
        dialog.setContentView(view);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        int dialogHeight = lp.height;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        dialog.getWindow().setLayout((int) (screenWidth* 0.9), dialogHeight);

        TextView title = view.findViewById(R.id.title);
        title.setText("Artistas seguidos");


        RecyclerView artistFollowingRecyclerView;
        ArtistFollowingAdapter artistFollowingAdapter;

        artistFollowingRecyclerView = view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        artistFollowingRecyclerView.setLayoutManager(mLayoutManager);
        artistFollowingRecyclerView.setNestedScrollingEnabled(false);

        artistFollowingAdapter = new ArtistFollowingAdapter(context, artistsFollowing, dialog);
        artistFollowingRecyclerView.setAdapter(artistFollowingAdapter);

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
    }

    private void goLoginScreen(){
        /* Create first dialog to assure */

        CurrentUser.getInstance(context).setCurrentUser(new UserSession());
        CurrentUser.getInstance(context).setUserLogin(false);

        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    private void deleteAccountDialog(){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_delete_account, null);
        dialog.setContentView(view);

        Button next = view.findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                proceedToDeleteAccount();
            }
        });
        Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
    }

    private void proceedToDeleteAccount(){
        Dialog loadingDialog = loadingDialogDeletingAccount();
        loadingDialog.show();

        String userId = user.getId();
        Call<ResponseBody> call = Api.getInstance().getAPI().deleteUser(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "User deleted success " + response.body());
                        loadingDialog.cancel();
                        Globals.displayShortToast(context, "Se ha borrado tu cuenta");
                        goLoginScreen();
                        break;
                    default:
                        Log.d(TAG, "User deleted following default " + response.code());
                        loadingDialog.cancel();
                        Globals.displayShortToast(context, "No se ha podido borrar tu cuenta. Prueba de nuevo más tarde");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "User deleted following failure " + t.getLocalizedMessage());
                loadingDialog.cancel();
                Globals.displayShortToast(context, "No se ha podido borrar tu cuenta. Prueba de nuevo más tarde");
            }
        });
    }

    private Dialog loadingDialogDeletingAccount() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialog.setContentView(view);

        TextView textView = view.findViewById(R.id.title);
        textView.setText("Eliminando cuenta");

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        return dialog;
    }
}