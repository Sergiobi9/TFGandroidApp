package com.example.tfgapp.Activities.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.tfgapp.Activities.Register.Fragments.RegisterBirthdayFragment;
import com.example.tfgapp.Activities.Register.Fragments.RegisterEmailFragment;
import com.example.tfgapp.Activities.Register.Fragments.RegisterGenderFragment;
import com.example.tfgapp.Activities.Register.Fragments.RegisterNameFragment;
import com.example.tfgapp.Activities.Register.Fragments.RegisterPasswordFragment;
import com.example.tfgapp.R;

public class RegisterAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        getSupportFragmentManager().beginTransaction().replace(R.id.register_account_fragment, new RegisterNameFragment()).addToBackStack(null).commit();
    }

    public void goBackRegisterProcess(View view) {
    }
}