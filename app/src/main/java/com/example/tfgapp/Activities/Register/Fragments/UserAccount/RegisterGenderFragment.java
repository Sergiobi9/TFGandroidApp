package com.example.tfgapp.Activities.Register.Fragments.UserAccount;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.tfgapp.Activities.Register.RegisterAccountActivity;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Global.Constants;
import com.example.tfgapp.R;

public class RegisterGenderFragment extends Fragment {

    private Context context;
    private View view;

    private Button femaleBtn, maleBtn, noBinaryBtn;

    public RegisterGenderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register_gender, container, false);
        context = getContext();

        initView();
        return view;
    }
    private void initView(){
        femaleBtn = view.findViewById(R.id.gender_female_btn);
        maleBtn = view.findViewById(R.id.gender_male_btn);
        noBinaryBtn = view.findViewById(R.id.gender_no_binary_btn);

        femaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGender(Constants.FEMALE);
            }
        });

        maleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGender(Constants.MALE);
            }
        });

        noBinaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGender(Constants.NON_BINARY);
            }
        });
    }

    private void chooseGender(int gender){
        User registeredUser = RegisterAccountActivity.getRegisteredUser();
        registeredUser.setGender(gender);
        RegisterAccountActivity.setRegisteredUser(registeredUser);

        getFragmentManager().beginTransaction().replace(R.id.register_account_fragment, new RegisterNameFragment()).addToBackStack(null).commit();
    }
}