package com.example.tfgapp.Activities.Register.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tfgapp.Activities.Login.LoginActivity;
import com.example.tfgapp.Activities.MainActivity;
import com.example.tfgapp.R;

public class RegisterEmailFragment extends Fragment {

    private Context context;
    private View view;
    private Button registerEmailBtn;

    public RegisterEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_email, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        registerEmailBtn = view.findViewById(R.id.register_email_btn);
        registerEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.register_account_fragment, new RegisterPasswordFragment()).addToBackStack(null).commit();
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    goBackLoginScreen();
                    return true;
                }
                return false;
            }
        });
    }

    private void goBackLoginScreen(){
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
}