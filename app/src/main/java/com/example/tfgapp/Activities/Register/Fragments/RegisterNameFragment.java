package com.example.tfgapp.Activities.Register.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.tfgapp.Activities.Register.RegisterAccountActivity;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

public class RegisterNameFragment extends Fragment {

    private Context context;
    private View view;
    private EditText registerName;
    private Button registerNameBtn;

    public RegisterNameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_name, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        registerName = view.findViewById(R.id.register_name);
        registerNameBtn = view.findViewById(R.id.register_name_btn);

        registerNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = registerName.getText().toString();

                if (nameStr.isEmpty()){
                    Globals.displayShortToast(context, "El nombre no es válido");
                    return;
                }

                String nameCapitalized = Helpers.capitalizeString(nameStr);
                User registeredUser = RegisterAccountActivity.getRegisteredUser();
                registeredUser.setName(nameCapitalized);
                RegisterAccountActivity.setRegisteredUser(registeredUser);

                RegisterAccountActivity.doUserFinalRegister(context);
            }
        });
    }
}