package com.example.tfgapp.Global;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.tfgapp.Entities.User.UserSession;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    private static Helpers instance;
    public static SimpleDateFormat timePattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
    public static SimpleDateFormat birthdayPattern = new SimpleDateFormat("dd/MM/yyyy");

    public static synchronized Helpers getInstance(Context contextParam) {
        if (instance == null) {
            instance = new Helpers();
        }
        return instance;
    }

    public static String getIntervalDay(Context context, UserSession userSession){

        boolean isUserBirthday = isUserBirthday(userSession);

        if (isUserBirthday) return "Happy birthday";

        String welcomeText = getCurrentIntervalDay(context);
        return welcomeText;
    }

    private static String getCurrentIntervalDay(Context context){
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
        if (currentHourIn24Format >= 21 || currentHourIn24Format < 6)
            return "Buenas noches";
        else if (currentHourIn24Format < 12)
            return "Buenos días";
        else
            return "Buenas tardes";
    }

    private static boolean isUserBirthday(UserSession userSession) {
        String userBirthday = userSession.getUser().getBirthday();

        if (userBirthday == null) return false;

        Calendar today = Calendar.getInstance();
        Calendar specifiedDate  = Calendar.getInstance();

        Date userBirthdayDate = null;

        try {
            userBirthdayDate = birthdayPattern.parse(userBirthday);
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

    public static String getDateFinalStringFromLong(Long date){
        Date dateToDate = new Date(date);
        dateToDate.setHours(23);
        dateToDate.setMinutes(59);
        return timePattern.format(dateToDate);
    }

    public static String getDateStartStringFromLong(Long date){
        Date dateToDate = new Date(date);
        return timePattern.format(dateToDate);
    }


    public static boolean isPasswordValid(String password){
        int passwordLength = password.length();

        return passwordLength >= 8;
    }

    public static Calendar getDateAsCalendar(String dateAsString) {
        Date date = null;
        try {
            date = timePattern.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    public static Date getBirthdayAsDate(String dateAsString) {
        Date date = null;
        try {
            date = birthdayPattern.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date getDateStringAsDate(String dateAsString) {
        Date date = null;
        try {
            date = timePattern.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getTimeStamp(){
        TimeZone tz = Calendar.getInstance().getTimeZone();
        timePattern.setTimeZone(tz);
        return timePattern.format(new Date());
    }

    public static String getDateWithPattern(Date dateAsString){
        TimeZone tz = Calendar.getInstance().getTimeZone();
        timePattern.setTimeZone(tz);
        return timePattern.format(dateAsString);
    }

    public static String getDateSince(String sinceDate, Context context){
        Calendar sinceDateCalendar = getDateAsCalendar(sinceDate);
        int year = sinceDateCalendar.get(Calendar.YEAR);
        String month = getMonth(sinceDateCalendar.get(Calendar.MONTH), context);

        return month + " " + year;
    }

    private static String getMonth(int month, Context context){
        String[] monts = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dec"};

        return monts[month];
    }

    public static String addYearToTimestamp(){
        Calendar c = Calendar.getInstance();
        c.setTime(getDateStringAsDate(getTimeStamp()));
        c.add(Calendar.YEAR, 5);
        return getDateWithPattern(c.getTime());
    }

    public static Calendar getCalendarFromDate(Date birthdayCalendarDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthdayCalendarDate);
        return calendar;
    }
}
