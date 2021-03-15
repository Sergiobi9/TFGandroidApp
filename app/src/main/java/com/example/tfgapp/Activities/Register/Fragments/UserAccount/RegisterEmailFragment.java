package com.example.tfgapp.Activities.Register.Fragments.UserAccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tfgapp.Activities.Login.LoginActivity;
import com.example.tfgapp.Activities.Register.RegisterAccountActivity;
import com.example.tfgapp.Entities.User.User;
import com.example.tfgapp.Entities.User.UserExists;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.Constants;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterEmailFragment extends Fragment {

    private final String TAG = "RegisterEmailFragment";
    private Context context;
    private View view;
    private Button registerEmailBtn;
    private EditText emailEditText;

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

        RegisterAccountActivity.isRegisterFirstScreen(true);

        view = inflater.inflate(R.layout.fragment_register_email, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        registerEmailBtn = view.findViewById(R.id.register_email_btn);
        emailEditText = view.findViewById(R.id.register_email);

        registerEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = emailEditText.getText().toString().toLowerCase();
                if (!Helpers.isEmailValid(userEmail)) {
                    Globals.displayShortToast(context, "El correo electrónico no tiene el formato correcto");
                    return;
                }

                checkEmailExists(userEmail);
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

    private void checkEmailExists(String userEmail){
        Call<UserExists> call = Api.getInstance().getAPI().checkUserAlreadyExists(userEmail);
        call.enqueue(new Callback<UserExists>() {
            @Override
            public void onResponse(Call<UserExists> call, Response<UserExists> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "User exists by email success " + response.body());

                        if (response.body().getInfo().equals(Constants.USER_EXISTS)){
                            Globals.displayShortToast(context, "Ya existe una cuenta con este correo");
                        } else {
                            User registeredUser = RegisterAccountActivity.getRegisteredUser();
                            registeredUser.setEmail(userEmail);
                            RegisterAccountActivity.setRegisteredUser(registeredUser);
                            RegisterAccountActivity.isRegisterFirstScreen(false);

                            getFragmentManager().beginTransaction().replace(R.id.register_account_fragment, new RegisterPasswordFragment()).addToBackStack(null).commit();
                        }
                        break;
                    default:
                        Log.d(TAG, "User exists by email default " + response.code());
                        Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserExists> call, Throwable t) {
                Log.d(TAG, "User exists by email failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Something happened, please try again in a few minutes");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    private void goBackLoginScreen(){
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
}