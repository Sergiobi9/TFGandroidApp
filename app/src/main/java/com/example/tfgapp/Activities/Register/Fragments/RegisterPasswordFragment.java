package com.example.tfgapp.Activities.Register.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.tfgapp.Activities.Register.RegisterAccountActivity;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

public class RegisterPasswordFragment extends Fragment {

    private Context context;
    private View view;
    private EditText passwordEditText;
    private Button registerPasswordBtn;
    private ImageView showOrHidePassword;

    private boolean isPasswordShowing = false;

    public RegisterPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register_password, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        passwordEditText = view.findViewById(R.id.register_password);
        registerPasswordBtn = view.findViewById(R.id.confirm_password_btn);
        showOrHidePassword = view.findViewById(R.id.show_password_icon);

        registerPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userPassword = passwordEditText.getText().toString();
                if (!Helpers.isPasswordValid(userPassword)) {
                    Globals.displayShortToast(context, "La contraseña debe tener al menos ocho carácteres");
                    return;
                }

                User registeredUser = RegisterAccountActivity.getRegisteredUser();
                registeredUser.setPassword(userPassword);
                RegisterAccountActivity.setRegisteredUser(registeredUser);

                getFragmentManager().beginTransaction().replace(R.id.register_account_fragment, new RegisterBirthdayFragment()).addToBackStack(null).commit();
            }
        });

        showOrHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordShowing) {
                    passwordEditText.setTransformationMethod(null);
                    showOrHidePassword.setImageResource(R.drawable.hide_password_icon);
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showOrHidePassword.setImageResource(R.drawable.show_password_icon);
                }
                isPasswordShowing = !isPasswordShowing;

            }
        });
    }
}