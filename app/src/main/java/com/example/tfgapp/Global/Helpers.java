package com.example.tfgapp.Global;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.tfgapp.Entities.User.UserSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Helpers {

    private static Helpers instance;

    public static synchronized Helpers getInstance(Context contextParam) {
        if (instance == null) {
            instance = new Helpers();
        }
        return instance;
    }

    public static String getIntervalDay(Context context, UserSession userSession){

        boolean isUserBirthday = isUserBirthday(context, userSession);

        if (isUserBirthday) return "Happy birthday";

        String welcomeText = getCurrentIntervalDay(context);
        return welcomeText;
    }

    private static String getCurrentIntervalDay(Context context){
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
        if (currentHourIn24Format >= 21 || currentHourIn24Format < 6)
            return "Good night";
        else if (currentHourIn24Format < 12)
            return "Good morning";
        else
            return "Good afternoon";
    }

    private static boolean isUserBirthday(Context context, UserSession userSession) {
        String userBirthday = userSession.getUser().getBirthday();

        if (userBirthday == null) return false;

        Calendar today = Calendar.getInstance();
        Calendar specifiedDate  = Calendar.getInstance();

        Date userBirthdayDate = null;

        try {
            userBirthdayDate = new SimpleDateFormat("dd/MM/yyyy").parse(userBirthday);
            specifiedDate.setTime(userBirthdayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return today.get(Calendar.DAY_OF_MONTH) == specifiedDate.get(Calendar.DAY_OF_MONTH)
                &&  today.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH);
    }
}
