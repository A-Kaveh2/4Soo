package ir.rasen.charsoo.webservices.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebservicePOST;


/**
 * Created by android on 12/16/2014.
 */
public class UpdatePassword extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdatePassword";

    private IWebserviceResponse delegate = null;
    private int userID;
    private String newPassword;
    private ServerAnswer serverAnswer;
    private Context context;

    public UpdatePassword(Context context,int userID, String newPassword,IWebserviceResponse delegate) {
        this.delegate = delegate;
        this.userID = userID;
        this.newPassword = newPassword;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_PASSWORD);

        try {
            webservicePOST.addParam(Params.USER_ID, String.valueOf(userID));
            webservicePOST.addParam(Params.PASSWORD_NEW, newPassword);

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

        //if webservice.execute() throws exception
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
