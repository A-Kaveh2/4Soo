package ir.rasen.charsoo.model.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class Login extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "Login";

    public IWebserviceResponse delegate = null;
    private Context context;
    private String email;
    private String password;
    private ServerAnswer serverAnswer;


    public Login(Context context, String email, String password, IWebserviceResponse delegate) {
        this.context = context;
        this.email = email;
        this.password = password;
        this.delegate = delegate;


    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {

        //params: email,password
        WebserviceGET webserviceGET = new WebserviceGET(URLs.LOGIN, new ArrayList<>(
                Arrays.asList(email, password)));

        try {
            serverAnswer = webserviceGET.execute(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();

                //save user_id and access token
                int user_id = 0, profilePictureId = 2022;
                String access_token = null, userIdentifier = null;

                if (jsonObject != null) {
                    user_id = jsonObject.getInt(Params.USER_ID_INT_FOR_LOGIN);
                    if (!TestUnit.isTestingCommentActivity)
                        profilePictureId = jsonObject.getInt(Params.PROFILE_PICTURE_ID);
                    access_token = jsonObject.getString(Params.ACCESS_TOKEN);

                    //get userIdentifier
                    userIdentifier = "UserIdentifier";
                    LoginInfo.login(context, user_id, access_token, profilePictureId, userIdentifier);
                }


                return new ResultStatus(true, ServerAnswer.NONE_ERROR);
            } else
                return new ResultStatus(false, serverAnswer.getErrorCode());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR,TAG);
            return;
        }
        if (result.success)
            delegate.getResult(result);
        else
            delegate.getError(result.errorCode,TAG);
    }
}
