package ir.rasen.charsoo.controller.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashSet;
import java.util.Set;

import ir.rasen.charsoo.controller.object.MyApplication;

public class LoginInfo {


    public int userID;
    public String accessToken;

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        if (preferences.getInt(Params.USER_ID_INT, 0) != 0)
            return true;
        return false;
    }

    public static void logout(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(
                activity.getPackageName(), Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putInt(Params.USER_ID_INT, 0);
        edit.commit();

        ((MyApplication) activity.getApplication()).isHomeCreated = false;
        ((MyApplication) activity.getApplication()).isSearchCreated = false;
        ((MyApplication) activity.getApplication()).isUserCreated = false;

        //go to the welcome activity
    }

    public static void login(Context context, int userID, String userIdentifier, int profilePictureId, String accessToken) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putInt(Params.USER_ID_INT, userID);
        edit.putInt(Params.PROFILE_PICTURE_ID, profilePictureId);
        edit.putString(Params.ACCESS_TOKEN, accessToken);
        edit.putString(Params.USER_IDENTIFIER, userIdentifier);

        edit.commit();

    }

    public static void submitBusiness(Context context) {
        //if the user has at least one business
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        Set<String> set = preferences.getStringSet(Params.USERS_BUSINESS, null);
        if(set == null)
            set = new HashSet<>();
        set.add(String.valueOf(LoginInfo.getUserId(context)));
        edit.putStringSet(Params.USERS_BUSINESS, set);
        edit.commit();
    }

    public static void removeBusiness(Context context) {
        //if the user has at least one business
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        Set<String> set = preferences.getStringSet(Params.USERS_BUSINESS, null);
        if (set.contains(String.valueOf(LoginInfo.getUserId(context))))
            set.remove(String.valueOf(LoginInfo.getUserId(context)));

        edit.putStringSet(Params.USERS_BUSINESS, set);
        edit.commit();
    }

    public static boolean hasBusiness(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        Set<String> set = preferences.getStringSet(Params.USERS_BUSINESS, null);
        if (set == null || set.size() == 0)
            return false;
        for (String str : set)
            if (str.equals(String.valueOf(LoginInfo.getUserId(context))))
                return true;
        return false;
    }


    public static int getUserId(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        int i = preferences.getInt(Params.USER_ID_INT, 0);
        return preferences.getInt(Params.USER_ID_INT, 0);

    }

    public static int getUserProfilePictureId(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        return preferences.getInt(Params.USER_PROFILE_PICTURE_ID, 2022);
    }

    public static String getAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        return preferences.getString(Params.ACCESS_TOKEN, "");
    }

    public static String getAccessUserIdentifier(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        return preferences.getString(Params.USER_IDENTIFIER, "userIdentifier");
    }


}
