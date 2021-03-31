package com.example.tfgapp.Fragments.Navigation.User.EditProfile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tfgapp.Entities.Booking.Booking;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Fragments.Navigation.User.Tickets.TicketsFragment;
import com.example.tfgapp.Fragments.Navigation.User.UserProfileFragment;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditUserProfileFragment extends Fragment {

    private View view;
    private Context context;
    private User user;
    private final String TAG = "EditUserProfileFragment";
    private TextView update;

    private EditText nameEditText, lastNameEditText, emailEditText, birthdayEditText, genderEditText,
    phoneEditText, cityEditText, countryEditText;

    private String birthday = "";

    public EditUserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);
        user = (User) getArguments().getSerializable("user");
        context = getContext();

        initView();
        return view;
    }

    private void dialogGender(){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_gender, null);
        dialog.setContentView(view);

        CheckBox female = view.findViewById(R.id.female);
        CheckBox male = view.findViewById(R.id.male);
        CheckBox noBinary= view.findViewById(R.id.no_binary);

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    male.setChecked(false);
                    noBinary.setChecked(false);
                }
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    female.setChecked(false);
                    noBinary.setChecked(false);
                }
            }
        });
        noBinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        noBinary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    male.setChecked(false);
                    female.setChecked(false);
                }
            }
        });

        if (user.getGender() == 1){
            male.setChecked(true);
        } else if (user.getGender() == 0){
            female.setChecked(true);
        } else {
            noBinary.setChecked(true);
        }

        Button next = view.findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!male.isChecked() && !female.isChecked() && !noBinary.isChecked()){
                    user.setGender(-1);
                    genderEditText.setText("No binario");
                } else if (male.isChecked()){
                    user.setGender(1);
                    genderEditText.setText("Hombre");
                } else if (female.isChecked()){
                    user.setGender(0);
                    genderEditText.setText("Mujer");
                } else if (noBinary.isChecked()){
                    user.setGender(-1);
                    genderEditText.setText("No binario");
                }
                dialog.cancel();
            }
        });

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
    }

    private void dialogUserBirthday(){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_date, null);
        dialog.setContentView(view);

        DatePicker datePicker = view.findViewById(R.id.date_picker);
        datePicker.setMaxDate(new Date().getTime());

        if (user.getBirthday() != null){
            Date birthdayCalendarDate = Helpers.getBirthdayAsDate(user.getBirthday());
            Calendar calendar = Helpers.getCalendarFromDate(birthdayCalendarDate);
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            birthday = user.getBirthday();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String day = String.valueOf(dayOfMonth);
                    String month = String.valueOf(monthOfYear + 1);

                    if (dayOfMonth < 10) { day = "0" + String.valueOf(dayOfMonth); }
                    if (monthOfYear + 1 < 10) { month = "0" + String.valueOf(monthOfYear + 1); }

                    birthday = day + "/" + month + "/" + String.valueOf(year);
                }
            });
        }

        Button next = view.findViewById(R.id.date_next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setBirthday(birthday);
                birthdayEditText.setText(birthday);
                dialog.cancel();
            }
        });

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        dialog.show();
    }

    private void initView(){
        nameEditText = view.findViewById(R.id.name);
        lastNameEditText = view.findViewById(R.id.last_name);
        emailEditText = view.findViewById(R.id.email);
        birthdayEditText = view.findViewById(R.id.birthday);
        genderEditText = view.findViewById(R.id.gender);
        phoneEditText = view.findViewById(R.id.phone);
        cityEditText = view.findViewById(R.id.city);
        countryEditText = view.findViewById(R.id.country);

        nameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        emailEditText.setText(user.getEmail());
        birthdayEditText.setText(user.getBirthday());
        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUserBirthday();
            }
        });

        if (user.getGender() == 1){
            genderEditText.setText("Hombre");
        } else if (user.getGender() == 0){
            genderEditText.setText("Mujer");
        } else {
            genderEditText.setText("No binario");
        }

        genderEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGender();
            }
        });

        phoneEditText.setText(user.getPhoneNumber() == null? "" : user.getPhoneNumber());
        cityEditText.setText(user.getCity());
        countryEditText.setText(user.getCountry());

        update = view.findViewById(R.id.save);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
    }

    private void updateUserProfile(){
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, new UserProfileFragment()).addToBackStack(null).commit();
    }
}