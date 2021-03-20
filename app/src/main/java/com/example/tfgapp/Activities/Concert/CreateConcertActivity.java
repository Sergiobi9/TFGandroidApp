package com.example.tfgapp.Activities.Concert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Activities.Concert.Fragment.ConcertNameFragment;
import com.example.tfgapp.R;

public class CreateConcertActivity extends AppCompatActivity {

    private static Concert registeredConcert;
    private static final String TAG = "CreateConcertActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_concert);

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        registeredConcert = new Concert();

        getSupportFragmentManager().beginTransaction().replace(R.id.concert_fragment, new ConcertNameFragment()).commit();
    }

    public static Concert getRegisteredConcert() {
        return registeredConcert;
    }

    public static void setRegisteredConcert(Concert concert) {
        Log.d(TAG, registeredConcert.toString());
        System.out.println(registeredConcert.toString());
        registeredConcert = concert;
    }
}