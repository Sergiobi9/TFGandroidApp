package com.example.tfgapp.Global;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.tfgapp.Entities.User.UserSession;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static String capitalizeString(String content){
        return content != null ? content.substring(0, 1).toUpperCase() + content.substring(1) : "";
    }

    public static boolean isPasswordValid(String password){
        int passwordLength = password.length();

        return passwordLength >= 8;
    }

    public static Calendar getDateAsCalendar(String dateAsString) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        Date date = null;
        try {
            date = parser.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    public static String getTimeStamp(){
        TimeZone tz = Calendar.getInstance().getTimeZone();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static String getDateWithPattern(Date dateAsString){
        TimeZone tz = Calendar.getInstance().getTimeZone();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(dateAsString);
    }
}
