package ir.rasen.charsoo.model.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.Permission;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class UpdateSetting extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateSetting";

    private IWebserviceResponse delegate = null;
    private int userID;
    private Permission permission;
    private ServerAnswer serverAnswer;
    private Context context;

    public UpdateSetting(Context context,int userID, Permission permission,IWebserviceResponse delegate) {
        this.userID = userID;
        this.permission = permission;
        this.delegate = delegate;
        this.context = context;

    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.UPDATE_SETTING,new ArrayList<>(
                Arrays.asList(String.valueOf(userID),
                        String.valueOf(permission.followedBusiness),
                        String.valueOf(permission.friends),
                        String.valueOf(permission.reviews))));

        try {
            serverAnswer = webserviceGET.execute(context);
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

        if (result == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());
    }
}
