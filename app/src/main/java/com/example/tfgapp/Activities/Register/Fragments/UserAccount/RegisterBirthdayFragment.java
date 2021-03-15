package com.example.tfgapp.Activities.Register.Fragments.UserAccount;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;

import com.example.tfgapp.Activities.Register.RegisterAccountActivity;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.R;

import java.util.Date;

public class RegisterBirthdayFragment extends Fragment {

    private Context context;
    private View view;
    private DatePicker registerBirthdayDatePicker;
    private String registerBirthday;
    private Button registerBirthdayBtn;

    public RegisterBirthdayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register_birthday, container, false);
        context = getContext();

        initView();
        return view;
    }
    private void initView(){
        registerBirthdayDatePicker = view.findViewById(R.id.register_birthday);
        registerBirthdayDatePicker.setMaxDate(new Date().getTime());
        registerBirthdayDatePicker.updateDate(1990, 6, 15);

        registerBirthday = "15/07/1990";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerBirthdayDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String day = String.valueOf(dayOfMonth);
                    String month = String.valueOf(monthOfYear + 1);

                    if (dayOfMonth < 10) { day = "0" + String.valueOf(dayOfMonth); }
                    if (monthOfYear + 1 < 10) { month = "0" + String.valueOf(monthOfYear + 1); }

                    registerBirthday = day + "/" + month + "/" + String.valueOf(year);
                }
            });
        }

        registerBirthdayBtn = view.findViewById(R.id.confirm_birthday_btn);
        registerBirthdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User registeredUser = RegisterAccountActivity.getRegisteredUser();
                registeredUser.setBirthday(registerBirthday);
                RegisterAccountActivity.setRegisteredUser(registeredUser);

                getFragmentManager().beginTransaction().replace(R.id.register_account_fragment, new RegisterGenderFragment()).addToBackStack(null).commit();
            }
        });
    }
}