package ir.rasen.charsoo.webservices.user;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.Sex;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetUserProfileInfo extends AsyncTask<Void, Void, User> {
    private static final String TAG = "GetUserProfileInfo";
    private IWebserviceResponse delegate = null;
    private int userID;
    private ServerAnswer serverAnswer;
    private ArrayList<String> params;
    private Context context;

    public GetUserProfileInfo(Context context,int userID, IWebserviceResponse delegate) {
        this.userID = userID;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected User doInBackground(Void... voids) {
        User user = new User();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_PROFILE_INFO,
                new ArrayList<>(Arrays.asList(String.valueOf(userID))));

        try {
            serverAnswer = webserviceGET.execute(context);

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                user.id = userID;
                user.name = jsonObject.getString(Params.NAME);
                user.email = jsonObject.getString(Params.EMAIL);
                user.password = jsonObject.getString(Params.PASSWORD);
                user.aboutMe = jsonObject.getString(Params.ABOUT_ME);
                user.sex = Sex.getSex(jsonObject.getString(Params.SEX));
                user.birthDate = jsonObject.getString(Params.BIRTH_DATE);
                user.profilePictureId = jsonObject.getInt(Params.PROFILE_PICTURE_ID);
                user.isEmailConfirmed = jsonObject.getBoolean(Params.IS_EMAIL_COMFIRMED);

                return user;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;

        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {

        //if webserviceGET.execute() throws exception
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());
    }
}
