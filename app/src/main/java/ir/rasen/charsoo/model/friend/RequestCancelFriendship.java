package ir.rasen.charsoo.model.friend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.ICancelFriendship;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class RequestCancelFriendship extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "RequestCancelFriendship";

    //private IWebserviceResponse delegate = null;
    private int applicatorUserID;
    private int requestedUserID;
    private ServerAnswer serverAnswer;
    private ICancelFriendship iCancelFriendship;
    private Context context;

    public RequestCancelFriendship(Context context,int applicatorUserID, int requestedUserID,ICancelFriendship iCancelFriendship) {
        this.applicatorUserID = applicatorUserID;
        this.requestedUserID = requestedUserID;
        //this.delegate = delegate;
        this.iCancelFriendship = iCancelFriendship;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.REQUEST_CANCEL_FRIENDSHIP, new ArrayList<>(
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
        if (serverAnswer.getSuccessStatus()) {
            iCancelFriendship.notifyDeleteFriend(requestedUserID);
        }
        /*if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus()) {
            delegate.getResult(result);
            iCancelFriendship.notifyDeleteFriend(requestedUserID);
        }
        else
            delegate.getError(serverAnswer.getErrorCode());*/
    }
}
