package ir.rasen.charsoo.model.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.Sex;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebservicePOST;

/**
 * Created by android on 12/16/2014.
 */
public class UpdateUserProfile extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateUserProfile";

    private IWebserviceResponse delegate = null;
    private User user;
    private ServerAnswer serverAnswer;
    private Context context;

    public UpdateUserProfile(Context context,User user,IWebserviceResponse delegate) {
        this.delegate = delegate;
        this.user = user;
        this.context =context;

    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {

        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_PROFILE_USER);

        try {
            webservicePOST.addParam(Params.USER_ID_INT, String.valueOf(user.id));
            webservicePOST.addParam(Params.NAME, user.name);
            webservicePOST.addParam(Params.EMAIL, user.email);
            webservicePOST.addParam(Params.PASSWORD,user.password);
            webservicePOST.addParam(Params.ABOUT_ME, user.aboutMe);
            webservicePOST.addParam(Params.SEX, Sex.getName(user.sex));
            webservicePOST.addParam(Params.BIRTH_DATE, user.birthDate);
            webservicePOST.addParam(Params.PROFILE_PICTURE, user.profilePicture);
            webservicePOST.addParam(Params.COVER_PICTURE, user.coverPicture);

            serverAnswer = webservicePOST.execute(context);
            if (serverAnswer.getSuccessStatus())
                return ResultStatus.getResultStatus(serverAnswer);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {
       /* if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);*/

        //if webservice.execute() throws exception
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR,TAG);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode(),TAG);
    }
}
