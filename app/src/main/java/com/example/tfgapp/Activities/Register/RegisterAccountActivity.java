package com.example.tfgapp.Activities.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tfgapp.Activities.Login.LoginActivity;
import com.example.tfgapp.Activities.MainActivity;
import com.example.tfgapp.Activities.Register.Fragments.UserAccount.RegisterEmailFragment;
import com.example.tfgapp.Entities.Login.AuthenticationData;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.UserLocationInformation;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Global.Permissions;
import com.example.tfgapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterAccountActivity extends AppCompatActivity {

    private static User registeredUser;
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String TAG = "RegisterAccountActivity";

    private double userLatitude = 0, userLongitude = 0;
    private static boolean isRegisterFirstScreen = true;

    private static Dialog dialog;

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

    private static void showDialog(Context context, String message){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialog.setContentView(view);

        TextView textView = view.findViewById(R.id.title);
        textView.setText(message);

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
    }

    private static void dismissDialog(){
        if (dialog != null){
            dialog.dismiss();
        }
    }

    public static void doUserFinalRegister(Context context){
        /* Register user with pop up loading */
        showDialog(context, "Creando tu cuenta, por favor espera");

        Call<User> call = Api.getInstance().getAPI().registerUser(registeredUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "User registered success " + response.body());

                        String userEmail = registeredUser.getEmail();
                        String userPassword = registeredUser.getPassword();

                        doUserLogin(context, userEmail, userPassword);

                        break;
                    default:
                        Log.d(TAG, "User registered default " + response.code());
                        Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
                        break;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "User registered failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
            }
        });
    }

    private static void doUserLogin(Context context, String userEmail, String userPassword){
        /* Login user with pop up loading */
        Call<UserSession> call = Api.getInstance().getAPI().doUserLogin(new AuthenticationData(userEmail, userPassword));
        call.enqueue(new Callback<UserSession>() {
            @Override
            public void onResponse(Call<UserSession> call, Response<UserSession> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "User login success " + response.body());

                        UserSession userSession = response.body();

                        CurrentUser.getInstance(context).setCurrentUser(userSession);
                        CurrentUser.getInstance(context).setUserLogin(true);

                        dismissDialog();

                        goRegisterMusicStyles(context);
                        break;
                    default:
                        Log.d(TAG, "User login default " + response.code());
                        Globals.displayShortToast(context, "Incorrect email or password");
                        dismissDialog();
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserSession> call, Throwable t) {
                Log.d(TAG, "User login failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
                dismissDialog();
            }
        });
    }

    private static void goRegisterMusicStyles(Context context){
        /* Open screen with music styles scroll */
        Intent intent = new Intent(context, RegisterCustomLikesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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