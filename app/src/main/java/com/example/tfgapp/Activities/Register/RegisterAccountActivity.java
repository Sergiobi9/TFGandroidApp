package com.example.tfgapp.Activities.Register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tfgapp.Activities.Login.LoginActivity;
import com.example.tfgapp.Activities.Register.Fragments.RegisterEmailFragment;
import com.example.tfgapp.Activities.SignIn.AuthenticationActivity;
import com.example.tfgapp.Global.UserLocationInformation;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Global.Permissions;
import com.example.tfgapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class RegisterAccountActivity extends AppCompatActivity {

    private static User registeredUser;
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String TAG = "RegisterAccountActivity";

    private double userLatitude = 0, userLongitude = 0;
    private static boolean isRegisterFirstScreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        context = getApplicationContext();
        registeredUser = new User();

        getUserLocation();

        getSupportFragmentManager().beginTransaction().replace(R.id.register_account_fragment, new RegisterEmailFragment()).addToBackStack(null).commit();
    }

    private void getUserLocation(){
            if (Permissions.checkLocationPermission(context)) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(RegisterAccountActivity.this);

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(
                                new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            Log.d(TAG, "New location in register " + location.getLatitude() + " " + location.getLongitude());
                                            userLatitude = location.getLongitude();
                                            userLongitude = location.getLatitude();

                                            String country = UserLocationInformation.getCountryCode(context);
                                            String city = UserLocationInformation.getUserCity(context, userLatitude, userLongitude);
                                            String zipCode = UserLocationInformation.getZipCode(context, userLatitude, userLongitude);

                                            registeredUser.setCountry(country);
                                            registeredUser.setCity(city);
                                            registeredUser.setZipCode(zipCode);
                                        }
                                    }
                                });
            }

    }

    public void goBackRegisterProcess(View view) {
        if (isRegisterFirstScreen)
            goLoginActivity();
        else
            getSupportFragmentManager().popBackStack();
    }

    public void goLoginActivity() {
        Intent intent = new Intent(RegisterAccountActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public static User getRegisteredUser() {
        return registeredUser;
    }

    public static void setRegisteredUser(User registeredUser) {
        Log.d(TAG, registeredUser.toString());
        RegisterAccountActivity.registeredUser = registeredUser;
    }

    public static void isRegisterFirstScreen(boolean isRegisterFirstScreen){
        RegisterAccountActivity.isRegisterFirstScreen = isRegisterFirstScreen;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}