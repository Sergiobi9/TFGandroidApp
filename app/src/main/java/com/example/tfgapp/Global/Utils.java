package com.example.tfgapp.Global;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;

public class Utils {

    public static BitmapDescriptor vectorToBitmap(Drawable vectorDrawable, @ColorInt int color, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, width, height);
        if (color >= 0) {
            DrawableCompat.setTint(vectorDrawable, color);
        }
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static void responsiveViewWidth(View view, double appliedWidth, Activity activity){
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;

            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = (int) (screenWidth * appliedWidth);
            view.setLayoutParams(params);
        } catch (NullPointerException nullPointerException){}
    }

    public static String getMonthSimplified(int position){
        ArrayList<String> months = new ArrayList<String>();
        months.add("Ene");
        months.add("Feb");
        months.add("Mar");
        months.add("Abr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Ago");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dic");

        return months.get(position);
    }

    public static void responsiveView(View view, double appliedWidth, double appliedHeight, Activity activity){
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;

            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (int) (screenWidth * appliedHeight);
            params.width = (int) (screenWidth * appliedWidth);
            view.setLayoutParams(params);
        } catch (NullPointerException np){

        }
    }
}
