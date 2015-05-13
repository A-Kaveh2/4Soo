package ir.rasen.charsoo.controller.helper;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;

import ir.rasen.charsoo.R;


/**
 * Created by Tandis on 11/29/2014.
 */
public class PersianDate {

    private int day;
    private int month;
    private int year;

    private static int DAY_HOURS = 24;
    private static int WEEK_HOURS = 7 * DAY_HOURS;
    private static int MONTH_HOURS = 30 * DAY_HOURS;
    private static int YEAR_HOURS = 365 * DAY_HOURS;


    private static int MINUTE_IN_SECOND = 60;
    private static int HOUR_IN_SECOND = 60 * MINUTE_IN_SECOND;
    private static int DAY_IN_SECOND = 24 * HOUR_IN_SECOND;
    private static int WEEK_IN_SECOND = 7 * DAY_IN_SECOND;
    private static int MONTH_IN_SECOND = 30 * WEEK_IN_SECOND;
    private static int YEAR_IN_SECOND = 12 * MONTH_IN_SECOND;


    private Resources resources;


    public PersianDate(Context context) {
        this.resources = context.getResources();
    }

    public PersianDate(Context context, int day, int month, int year) throws Exception {
        this.resources = context.getResources();
        setDay(day);
        setMonth(month);
        setYear(year);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) throws Exception {
        if (day < 1 || day > 31)
            throw new Exception("Invalid day value");
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) throws Exception {
        if (month < 1 || month > 12)
            throw new Exception("Invalid month value");
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) throws Exception {
        if (year < 1250 || year > 1393)
            throw new Exception("Invalid year value");
        this.year = year;
    }


    public static boolean validateDayBaseOnMonth(int day, int month) {
        if (month < 7) {
            if (day > 31)
                return false;
        } else if (month > 6 && month < 12) {
            if (day > 30)
                return false;
        } else if (month == 12)
            if (day > 29)
                return false;
        return true;
    }

    public String getMonthString(int month) {
        String monthString = "";
        switch (month) {
            case 1:
                resources.getString(R.string.FARVARDIN);
                break;
            case 2:
                resources.getString(R.string.ORDIBEHESHT);
                break;
            case 3:
                resources.getString(R.string.KHORDAD);
                break;
            case 4:
                resources.getString(R.string.TIR);
                break;
            case 5:
                resources.getString(R.string.MORDAD);
                break;
            case 6:
                resources.getString(R.string.SHAHRIVAR);
                break;
            case 7:
                resources.getString(R.string.MEHR);
                break;
            case 8:
                resources.getString(R.string.ABAN);
                break;
            case 9:
                resources.getString(R.string.AZAR);
                break;
            case 10:
                resources.getString(R.string.DEY);
                break;
            case 11:
                resources.getString(R.string.BAHMAN);
                break;
            case 12:
                resources.getString(R.string.ESFAND);
                break;
        }
        return monthString;
    }

    public ArrayList<String> getMonthStringList() {
        //get list of persian month strings to display in spinner
        ArrayList<String> monthStringList = new ArrayList<String>();

        monthStringList.add(resources.getString(R.string.FARVARDIN));
        monthStringList.add(resources.getString(R.string.ORDIBEHESHT));
        monthStringList.add(resources.getString(R.string.KHORDAD));
        monthStringList.add(resources.getString(R.string.TIR));
        monthStringList.add(resources.getString(R.string.MORDAD));
        monthStringList.add(resources.getString(R.string.SHAHRIVAR));
        monthStringList.add(resources.getString(R.string.MEHR));
        monthStringList.add(resources.getString(R.string.ABAN));
        monthStringList.add(resources.getString(R.string.AZAR));
        monthStringList.add(resources.getString(R.string.DEY));
        monthStringList.add(resources.getString(R.string.BAHMAN));
        monthStringList.add(resources.getString(R.string.ESFAND));

        return monthStringList;
    }

    public static String getCreationDate(Context context, int hours) {
        Resources resources = context.getResources();
        String creationDate = "";

        if (hours < 0) {
            //this is the future!
            creationDate = resources.getString(R.string.err_creation_date_invalid);
        } else if (hours == 0) {
            //it is completely fresh!
            creationDate = "1" + resources.getString(R.string.hour);

        } else if (hours > 0 && hours < DAY_HOURS) {
            //less than a day, creation date will display in hours
            creationDate = hours + resources.getString(R.string.hour);

        } else if (hours >= DAY_HOURS && hours < WEEK_HOURS) {
            //more than a day and less than a week, creation date will display in days
            creationDate = hours / DAY_HOURS + resources.getString(R.string.day);

        } else if (hours >= WEEK_HOURS && hours < MONTH_HOURS) {
            //more than a week and less than a month, creation date will display in weeks
            creationDate = hours / WEEK_HOURS + resources.getString(R.string.week);

        } else if (hours >= MONTH_HOURS && hours < YEAR_HOURS) {
            //more than a month and less than a year, creation date will display in months
            creationDate = hours / MONTH_HOURS + resources.getString(R.string.month);

        } else if (hours >= YEAR_HOURS) {
            //more than a year, creation date will display in years
            creationDate = hours / YEAR_HOURS + resources.getString(R.string.year);

        }

        return creationDate;
    }

    public static String getCreationDateInSecond(Context context, int seconds) {
        Resources resources = context.getResources();
        String creationDate = "";

        if (seconds < 0) {
            //this is the future!
            creationDate = resources.getString(R.string.err);
        } else if (seconds == 0) {
            //it is completely fresh!
            creationDate = "1" + resources.getString(R.string.second);

        } else if (seconds > 0 && seconds <= MINUTE_IN_SECOND) {
            //less than a minute, creation date will display in seconds
            creationDate = seconds + resources.getString(R.string.second);

        } else if (seconds > MINUTE_IN_SECOND && seconds <= HOUR_IN_SECOND) {
            //more than a minute and less than an hour, creation date will display in minutes
            creationDate = seconds / MINUTE_IN_SECOND + resources.getString(R.string.minute);

        } else if (seconds > HOUR_IN_SECOND && seconds <= DAY_IN_SECOND) {
            //more than an hour and less than a day, creation date will display in hours
            creationDate = seconds / HOUR_IN_SECOND + resources.getString(R.string.hour);

        } else if (seconds > DAY_IN_SECOND && seconds <= WEEK_IN_SECOND) {
            //more than a day and less than a week, creation date will display in days
            creationDate = seconds / DAY_IN_SECOND + resources.getString(R.string.day);

        } else if (seconds > WEEK_IN_SECOND && seconds <= MONTH_IN_SECOND) {
            //more than a week and less than a month, creation date will display in weeks
            creationDate = seconds / WEEK_IN_SECOND + resources.getString(R.string.week);

        } else if (seconds > MONTH_IN_SECOND && seconds <= YEAR_IN_SECOND) {
            //more than a month and less than a year, creation date will display in months
            creationDate = seconds / MONTH_IN_SECOND + resources.getString(R.string.month);

        } else if (seconds > YEAR_IN_SECOND) {
            //more than a year, creation date will display in years
            creationDate = seconds / YEAR_IN_SECOND + resources.getString(R.string.year);
        }

        return creationDate;
    }

    public static String getBirthDateString(String day, String month, String year) {
        return year + "/" + month + "/" + day;
    }

    public static String getDayFromBirthDateString(String birthDate) {
        return birthDate.substring(birthDate.lastIndexOf("/") + 1, birthDate.length());
    }

    public static String getMonthFromBirthDateString(String birthDate) {
        return birthDate.substring(birthDate.indexOf("/") + 1, birthDate.lastIndexOf("/"));
    }

    public static String getYearFromBirthDateString(String birthDate) {
        return birthDate.substring(0, birthDate.indexOf("/"));
    }
}
