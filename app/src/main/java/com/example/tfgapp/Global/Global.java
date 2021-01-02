package com.example.tfgapp.Global;

import android.content.Context;
import android.widget.Toast;

public class Global {

    public static void displayShortToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void displayLongToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
