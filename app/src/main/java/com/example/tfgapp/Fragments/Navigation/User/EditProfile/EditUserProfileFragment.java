package com.example.tfgapp.Fragments.Navigation.User.EditProfile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tfgapp.Entities.Booking.Booking;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Fragments.Navigation.User.Tickets.TicketsFragment;
import com.example.tfgapp.Fragments.Navigation.User.UserProfileFragment;
import com.example.tfgapp.R;

import java.util.ArrayList;

public class EditUserProfileFragment extends Fragment {

    private View view;
    private User user;
    private final String TAG = "EditUserProfileFragment";
    private TextView update;

    private EditText nameEditText, lastNameEditText, emailEditText, birthdayEditText, genderEditText,
    phoneEditText, cityEditText, countryEditText;

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

        user = (User) getArguments().getSerializable("user");
        Log.d(TAG, user.toString());

        view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);

        initView();
        return view;
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
        genderEditText.setText(String.valueOf(user.getGender()));
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