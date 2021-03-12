package com.example.tfgapp.Global;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tfgapp.API.ApiClient;
import com.example.tfgapp.API.ApiInterface;
import com.example.tfgapp.Entities.User.UserSession;

public class CurrentUser {

    private static SharedPreferences sharedPreferences;
    private static CurrentUser instance;

    public static synchronized CurrentUser getInstance(Context context) {
        if (instance == null) {
            instance = new CurrentUser();
            sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        }
        return instance;
    }

    public static void setCurrentUser(UserSession userSession){
        if (userSession == null)
            return;

        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(Constants.CURRENT_USER_SESSION, userSession.toJson()).apply();
        }
    }

    public static UserSession getCurrentUser(){
        if (sharedPreferences != null)
            return UserSession.fromJson(sharedPreferences.getString(Constants.CURRENT_USER_SESSION, null));
        else return null;
    }

    public static boolean isUserLoggedIn(){
        boolean isUserLoggedIn = sharedPreferences.getBoolean(Constants.USER_LOGGED_IN, false);
        return isUserLoggedIn;
    }

    public static void setUserLogin(boolean isLogin){
        sharedPreferences.edit().putBoolean(Constants.USER_LOGGED_IN, isLogin).apply();
    }

    public static String getUserRole(){
        if (sharedPreferences != null) {
            UserSession userSession = UserSession.fromJson(sharedPreferences.getString(Constants.CURRENT_USER_SESSION, null));

            if (userSession == null)
                return null;

            return userSession.getUser().getUserRole();
        }

        return null;
    }

    public static void doUserLogout(){
        sharedPreferences.edit().putBoolean(Constants.USER_LOGGED_IN, false).apply();
        sharedPreferences.edit().putString(Constants.CURRENT_USER_SESSION, null).apply();
    }

}
