package com.example.onlinequiz.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Helper {

    // TEST DATE STRING
    public static String getTestDate(Date date) {
        String result;

        //
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //
        Date curDate = addHoursToJavaUtilDate(getCurrentUtcTime(), 7);
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(curDate);

        long time = cal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        long distance = currentTime - time;
        long oneDayTime = 24 * 60 * 60 * 1000;
        long twoDayTime = oneDayTime * 2;

        if (distance < twoDayTime) {
            String dayString = (distance < oneDayTime) ? "today" : "yesterday";
            String hourAndMinute = getDateStringByFormat(date, "HH:mm");
            result = hourAndMinute + " " + dayString;
        } else result = getDateStringByFormat(date, "HH:mm dd/MM/yyy ");
        return result;
    }

    //
    public static String getCurrentISODateString() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    public static Date convertISODateStringToDate(String string) {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = df1.parse(string);
            date = addHoursToJavaUtilDate(date, 7);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateStringByFormat(Date date, String stringFormat) {
        if (date != null) {
            DateFormat parseFormat = new SimpleDateFormat(stringFormat);
            String stringDate = parseFormat.format(date);
            return stringDate;
        }
        return null;
    }

    private static Date getCurrentUtcTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        try {
            return localDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static String ucFirst(String string) {
        String result = null;
        if (string != null) {
            if (string.length() > 0) {
                result = string.substring(0, 1).toUpperCase() + string.substring(1);
            }
        }
        return result;
    }

    public static String getPureString(String string) {
        String result = null;
        if (string != null) {
            result = string.replaceAll("[^\\w\\s]", "").toLowerCase().trim();
        }
        return result;
    }

}
