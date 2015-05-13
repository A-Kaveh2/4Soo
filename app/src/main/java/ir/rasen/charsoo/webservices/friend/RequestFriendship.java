package ir.rasen.charsoo.webservices.friend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class RequestFriendship extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RequestFriendship";

    private IWebserviceResponse delegate = null;
    private int applicatorUserID;
    private int requestedUserID;
    private ServerAnswer serverAnswer;
    private Context context;

    public RequestFriendship(Context context,int applicatorUserID, int requestedUserID,IWebserviceResponse delegate) {
        this.applicatorUserID = applicatorUserID;
        this.requestedUserID = requestedUserID;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.REQUEST_FRIENDSHIP, new ArrayList<>(
                Arrays.asList(String.valueOf(applicatorUserID),
                        String.valueOf(requestedUserID))));

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
