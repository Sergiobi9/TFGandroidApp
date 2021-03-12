package com.example.tfgapp.Global;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserLocationInformation {

    public static String getUserCity(Context context, double userLatitude, double userLongitude){
        String city = "";
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            if (gcd != null) {
                addresses = gcd.getFromLocation(userLatitude, userLongitude, 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            if (addresses.size() > 0) {
                city = addresses.get(0).getLocality();
            }
        }
        return city == null? "" : city;
    }

    public static String getZipCode(Context context, double userLatitude, double userLongitude){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        String zipCodeString = "";
        try {
            addresses = geocoder.getFromLocation(userLatitude, userLongitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            zipCodeString = addresses.get(0).getPostalCode();
            if (zipCodeString == null)
                zipCodeString = "";
        }

        return zipCodeString == null? "" : zipCodeString;
    }

    public static String getCountryCode(Context context){
        String countryCode = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if ((!telephonyManager.getSimCountryIso().isEmpty()) || telephonyManager.getSimCountryIso() != null) {
            countryCode = telephonyManager.getSimCountryIso().toUpperCase();
        }

        return countryCode;
    }
}
