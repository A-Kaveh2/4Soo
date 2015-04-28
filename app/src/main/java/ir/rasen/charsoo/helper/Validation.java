package ir.rasen.charsoo.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;

import ir.rasen.charsoo.R;

/**
 * Created by android on 3/16/2015.
 */
public class Validation {
    static private boolean valid;
    static private String errorMessage;

    public static boolean isValid() {
        return valid;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }

    private Validation(boolean success, String errorMessage) {
        this.valid = success;
        this.errorMessage = errorMessage;
    }

    public static Validation validateEmail(Context context, String email) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return new Validation(false, context.getResources().getString(R.string.enter_valid_email));
        else
            return new Validation(true, "null");
    }

    public static Validation validatePassword(Context context, String password) {

        if (password.length() < Params.USER_PASSWORD_MIN_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_password_5_digits));
        else
            return new Validation(true, "null");
    }

    public static Validation validateIdentifier(Context context, String identifier) {
        if (!identifier.matches(Params.USER_USERNAME_VALIDATION) || identifier.length() < Params.USER_USERNAME_MIN_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_valid_business_id));
        else if (identifier.length() > Params.USER_USERNAME_MAX_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_is_too_long));
        else
            return new Validation(true, "null");
    }

    public static Validation validateName(Context context, String name) {

        if (!name.toString().matches(Params.USER_NAME_VALIDATION) || name.length() < Params.USER_NAME_MIN_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_valid_name));
        else if (name.length() > Params.USER_NAME_MAX_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_is_too_long));
        else
            return new Validation(true, "null");
    }

    public static Validation validateComment(Context context, String comment) {

        if (comment.length() < Params.USER_NAME_MIN_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_valid_comment_lenth));
        else if (comment.length() > Params.COMMENT_TEXT_MAX_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_is_too_long));
        else
            return new Validation(true, "null");
    }

    public static Validation validateTitle(Context context, String title) {

        if (!title.toString().matches(Params.USER_NAME_VALIDATION) || title.length() < Params.USER_NAME_MIN_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_valid_title));
        else if (title.length() > Params.USER_NAME_MAX_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_is_too_long));
        else
            return new Validation(true, "null");
    }

    public static Validation validateAboutMe(Context context, String aboutMe) {

        if (aboutMe.length() < Params.ABOUT_ME_MIN_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_valid_about_me));
        else if (aboutMe.length() > Params.ABOUT_ME_MAX_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_is_too_long));
        else
            return new Validation(true, "null");
    }

    public static Validation validateRepeatPassword(Context context, String password, String repeatePassword) {

        if (!password.equals(repeatePassword))
            return new Validation(false, context.getResources().getString(R.string.enter_same_passwords));
        else
            return new Validation(true, "null");
    }

    public static Validation validateDay(Context context, String day) {
        if(day.equals(""))
            return new Validation(false, context.getResources().getString(R.string.enter_non_empty_value));

        int d = Integer.valueOf(day);
        if (d < 0 || d > 31)
            return new Validation(false, context.getResources().getString(R.string.enter_correct_value));
        else
            return new Validation(true, "null");
    }

    public static Validation validateMonth(Context context, String month) {
        if(month.equals(""))
            return new Validation(false, context.getResources().getString(R.string.enter_non_empty_value));

        int m = Integer.valueOf(month);
        if (m < 0 || m > 12)
            return new Validation(false, context.getResources().getString(R.string.enter_correct_value));
        else
            return new Validation(true, "null");
    }

    public static Validation validateYear(Context context, String year) {
        if(year.equals(""))
            return new Validation(false, context.getResources().getString(R.string.enter_non_empty_value));


        SolarCalendar solarCalendar = new SolarCalendar(context);
        if (year.length() < 4)
            return new Validation(false, context.getResources().getString(R.string.enter_correct_year));
        else if (Integer.valueOf(year) < 1250 || Integer.valueOf(year) > solarCalendar.get_year())
            return new Validation(false, context.getResources().getString(R.string.enter_correct_value));
        else
            return new Validation(true, "null");
    }

    public static Validation validateHour(Context context, String hourStr) {
        if(hourStr.equals(""))
            return new Validation(false, context.getResources().getString(R.string.fill));

        int hour = Integer.valueOf(hourStr);
        if (hour < 1 || hour > 24)
            return new Validation(false, context.getResources().getString(R.string.enter_correct_hour));
        else
            return new Validation(true, "null");
    }

    public static Validation validateMinute(Context context, String minuteStr) {
        if(minuteStr.equals(""))
            return new Validation(false, context.getResources().getString(R.string.fill));

        int minute = Integer.valueOf(minuteStr);
        if (minute < 0 || minute > 59)
            return new Validation(false, context.getResources().getString(R.string.enter_correct_minute));
        else
            return new Validation(true, "null");
    }

    public static Validation validateMobile(Context context, String mobileNumber) {

        if (!mobileNumber.substring(0, 2).equals("09") || mobileNumber.length() != 11)
            return new Validation(false, context.getResources().getString(R.string.enter_correct_value));
        else
            return new Validation(true, "null");

    }

    public static Validation validateWebsite(Context context, String website) {
        if (!website.equals("")&& !Patterns.WEB_URL.matcher(website).matches())
            return new Validation(false, context.getResources().getString(R.string.enter_correct_value));
        else
            return new Validation(true, "null");
    }

    public static Validation validatePhone(Context context, String phone) {
        if (phone.length() < 7 )
            return new Validation(false, context.getResources().getString(R.string.enter_correct_value));
        else
            return new Validation(true, "null");
    }

    public static Validation validateCity(Context context, String city) {
        if (!city.toString().matches(Params.CITY_VALIDATION))
            return new Validation(false, context.getResources().getString(R.string.enter_valid_city));
        else if (city.length() > Params.USER_NAME_MAX_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_is_too_long));
        else
            return new Validation(true, "null");
    }

    public static Validation validateAddress(Context context, String address) {
        if (address.length() > Params.COMMENT_TEXT_MAX_LENGTH)
            return new Validation(false, context.getResources().getString(R.string.enter_is_too_long));
        else
            return new Validation(true, "null");
    }

}
