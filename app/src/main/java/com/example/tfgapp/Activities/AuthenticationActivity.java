package com.example.tfgapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.tfgapp.Entities.Concert.ConcertHome;
import com.example.tfgapp.Entities.Login.AuthenticationData;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Entities.User.UserSession;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivity extends AppCompatActivity {

    private final String TAG = "AuthenticationActivity";
    private Context context;
    private EditText userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        context = getApplicationContext();
        initView();
    }

    public void goBack(View view) {
       finish();
    }

    private void initView(){
        userEmail = findViewById(R.id.login_email_input);
        userPassword = findViewById(R.id.login_password_input);
    }

    public void doUserLogin(View view) {
        String userEmailStr = userEmail.getText().toString();
        String userPasswordStr = userPassword.getText().toString();

        Call<UserSession> call = Api.getInstance().getAPI().doUserLogin(new AuthenticationData(userEmailStr, userPasswordStr));
        call.enqueue(new Callback<UserSession>() {
            @Override
            public void onResponse(Call<UserSession> call, Response<UserSession> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "User login success " + response.body());

                        UserSession userSession = response.body();

                        CurrentUser.getInstance(context).setCurrentUser(userSession);
                        CurrentUser.getInstance(context).setUserLogin(true);

                        goMainScreen();
                        break;
                    default:
                        Log.d(TAG, "User login default " + response.code());
                        Globals.displayShortToast(context, "Incorrect email or password");
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserSession> call, Throwable t) {
                Log.d(TAG, "User login failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
            }
        });
    }

    private void goMainScreen(){
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent);
    }
}