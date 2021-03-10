package com.example.tfgapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.tfgapp.Fragments.Navigation.HomeFragment;
import com.example.tfgapp.Fragments.Navigation.MapFragment;
import com.example.tfgapp.Fragments.Navigation.SearchFragment;
import com.example.tfgapp.Fragments.Navigation.UserProfileFragment;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Permissions;
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
    private AnimatedBottomBar animatedBottomBar;
    private Context context;

    private boolean mapIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        initView();
    }

    private void initView(){
        animatedBottomBar = findViewById(R.id.navigation_bottom_bar);

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

        animatedBottomBar.selectTabAt(0, true);

    }

    private void selectNavigationItem(int itemPosition){
        switch (itemPosition){
            case 1:
                goMapSecction();
                break;
            case 2:
                goSearchFragment();
                break;
            case 3:
                goUserProfileFragment();
                break;
            default:
                goHomeFragment();
                break;

        }
    }

    private void goMapSecction(){
        Log.d(TAG, "Opening map fragment");

        if (Permissions.checkLocationPermission(context)){
            openFragment(new MapFragment());
        } else {
            mapIntent = true;
            requestLocationPermission();
        }
    }

    private void goSearchFragment(){
        Log.d(TAG, "Opening search fragment");

        openFragment(new SearchFragment());
    }

    private void goUserProfileFragment(){
        Log.d(TAG, "Opening profile fragment");

        openFragment(new UserProfileFragment());
    }

    private void goHomeFragment(){
        Log.d(TAG, "Opening home fragment");

        openFragment(new HomeFragment());
    }

    private void openFragment(Fragment fragmentToBeOpened){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragmentToBeOpened).addToBackStack(null).commit();
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