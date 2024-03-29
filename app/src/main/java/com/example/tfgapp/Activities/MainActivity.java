package com.example.tfgapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.tfgapp.Activities.Login.LoginActivity;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Fragments.Navigation.Artist.Concert.ConcertFragment;
import com.example.tfgapp.Fragments.Navigation.Artist.Home.HomeArtistFragment;
import com.example.tfgapp.Fragments.Navigation.Artist.Profile.ArtistProfileFragment;
import com.example.tfgapp.Fragments.Navigation.HomeFragment;
import com.example.tfgapp.Fragments.Navigation.MapFragment;
import com.example.tfgapp.Fragments.Navigation.SearchFragment;
import com.example.tfgapp.Fragments.Navigation.User.Search.SearchConcertFragment;
import com.example.tfgapp.Fragments.Navigation.User.Tickets.TicketsFragment;
import com.example.tfgapp.Fragments.Navigation.User.UserProfileFragment;
import com.example.tfgapp.Global.Constants;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    public static AnimatedBottomBar animatedBottomBar;
    private Context context;

    private boolean mapIntent = false, isLoggedIn = false;
    private UserSession userSession;
    private String userRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        System.out.println(Helpers.getTimeStamp());

        context = getApplicationContext();

        userSession = CurrentUser.getInstance(context).getCurrentUser();
        userRole = CurrentUser.getInstance(context).getUserRole();

        initView();
    }

    private void initView(){

        if (userRole == null) {
            animatedBottomBar = findViewById(R.id.main_navigation);
            animatedBottomBar.setVisibility(View.VISIBLE);
            selectNavigationItem(1);
            animatedBottomBar.selectTabAt(1, true);
        } else if (userRole.equals(Constants.USER_NORMAL_ROLE)) {
            animatedBottomBar = findViewById(R.id.normal_user_navigation);
            animatedBottomBar.setVisibility(View.VISIBLE);
            selectNavigationItem(2);
            animatedBottomBar.selectTabAt(2, true);
        } else if (userRole.equals(Constants.ARTIST_ROLE)) {
            animatedBottomBar = findViewById(R.id.artist_navigation);
            animatedBottomBar.setVisibility(View.VISIBLE);
            selectNavigationItem(0);
            animatedBottomBar.selectTabAt(0, true);
        }

        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastPosition, @Nullable AnimatedBottomBar.Tab tab, int newPosition, @NotNull AnimatedBottomBar.Tab tab1) {
                selectNavigationItem(newPosition);
            }

            @Override
            public void onTabReselected(int itemPosition, @NotNull AnimatedBottomBar.Tab tab) {
                selectNavigationItem(itemPosition);
            }
        });
    }

    private void selectNavigationItem(int itemPosition){
        if (userRole == null)
            //mainNavigationController(itemPosition);
            goLogginScreen();
        else if (userRole.equals(Constants.USER_NORMAL_ROLE))
            userNavigationController(itemPosition);
        else if (userRole.equals(Constants.ARTIST_ROLE))
            artistNavigationController(itemPosition);
    }

    private void userNavigationController(int itemPosition){
        switch (itemPosition){
            case 0:
                openFragment(new MapFragment());
                break;
            case 1:
                openFragment(new SearchFragment());
                break;
            case 2:
                openFragment(new HomeFragment());
                break;
            case 3:
                openFragment(new TicketsFragment());
                break;
            case 4:
                openFragment(new UserProfileFragment());
                break;
            default:
                break;

        }
    }

    private void artistNavigationController(int itemPosition){
        switch (itemPosition){
            case 0:
                openFragment(new HomeArtistFragment());
                break;
            case 1:
                openFragment(new ConcertFragment());
                break;
            case 2:
                openFragment(new ArtistProfileFragment());
                break;
            default:
                break;

        }
    }

    private void mainNavigationController(int itemPosition){
        Log.d(TAG, "Selection item");
        switch (itemPosition){
            case 0:
                openFragment(new MapFragment());
                break;
            case 1:
                openFragment(new HomeFragment());
                break;
            case 2:
                openFragment(new SearchFragment());
                break;
            default:
                break;

        }
    }

    private void goLogginScreen(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void openFragment(Fragment fragmentToBeOpened){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragmentToBeOpened).commit();
    }

    private void requestLocationPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (mapIntent) {
                            openFragment(new MapFragment());
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()){
                            Globals.displayShortToast(context, "Location is necessary");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
}