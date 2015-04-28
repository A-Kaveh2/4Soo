package ir.rasen.charsoo.webservices.post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IUpdateTimeLine;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class CancelShare extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "CancelShare";

    private IWebserviceResponse delegate = null;
    private int userID;
    private int postID;
    private ServerAnswer serverAnswer;
    private Context context;
    private IUpdateTimeLine iUpdateTimeLine;

    public CancelShare(Context context,int userID, int postID,IUpdateTimeLine iUpdateTimeLine) {
        //this.delegate = delegate;
        this.userID = userID;
        this.postID = postID;
        this.context = context;
        this.iUpdateTimeLine = iUpdateTimeLine;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.CANCEL_SHARE, new ArrayList<>(
                Arrays.asList(String.valueOf(userID), String.valueOf(postID))));


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
        if (iUpdateTimeLine != null && serverAnswer != null && serverAnswer.getSuccessStatus()){
            iUpdateTimeLine.notifyUpdateTimeLineCancelShare(postID);
        }

        //if webservice.execute() throws exception
       /* if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());*/
    }
}
