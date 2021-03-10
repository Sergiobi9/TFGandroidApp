package com.example.tfgapp.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.tfgapp.API.ApiClient;
import com.example.tfgapp.API.ApiInterface;

public class Globals {

    private SharedPreferences sharedPreferences;
    private static Globals instance;
    private static Context context;

    public static void displayShortToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void displayLongToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static synchronized Globals getInstance(Context contextParam) {
        if (instance == null) {
            instance = new Globals();
            context = contextParam;
        }
        return instance;
    }
}
