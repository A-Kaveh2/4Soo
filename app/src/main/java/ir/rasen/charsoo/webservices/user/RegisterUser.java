package ir.rasen.charsoo.webservices.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebservicePOST;


/**
 * Created by android on 12/16/2014.
 */
public class RegisterUser extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RegisterUser";

    private IWebserviceResponse delegate = null;
    private User user;
    private ServerAnswer serverAnswer;
    private Context context;

    public RegisterUser(Context context, User user, IWebserviceResponse delegate) {
        this.user = user;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.REGISTER_USER);

        try {
            webservicePOST.addParam(Params.USER_ID, user.userIdentifier);
            webservicePOST.addParam(Params.NAME, user.name);
            webservicePOST.addParam(Params.EMAIL, user.email);
            webservicePOST.addParam(Params.PASSWORD, user.password);
            if (user.profilePicture != null)
                webservicePOST.addParam(Params.PROFILE_PICTURE, user.profilePicture);
            else
                webservicePOST.addParam(Params.PROFILE_PICTURE, "");

            serverAnswer = webservicePOST.execute(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();

                //save user_id and access token

                //TODO uncomment after modifying webservice
                int user_id = 0, profilePictureId = 2022;
                String access_token = null;
                if (jsonObject != null) {
                    user_id = jsonObject.getInt(Params.USER_ID);
                    if (!TestUnit.isTestingCommentActivity)
                        profilePictureId = jsonObject.getInt(Params.PROFILE_PICTURE_ID);

                    access_token = jsonObject.getString(Params.ACCESS_TOKEN);
                    LoginInfo.login(context, user_id, access_token,profilePictureId,user.userIdentifier);
                }
            }
            return ResultStatus.getResultStatus(serverAnswer);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (result.success)
            delegate.getResult(result);
        else
            delegate.getError(result.errorCode);
    }
}
