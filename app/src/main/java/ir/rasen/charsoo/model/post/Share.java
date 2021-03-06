package ir.rasen.charsoo.model.post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IUpdateTimeLine;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;

/**
 * Created by android on 12/16/2014.
 */
public class Share extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "Share";

    private IWebserviceResponse delegate = null;
    private int userID;
    private int postID;
    private ServerAnswer serverAnswer;
    private Context context;
    private IUpdateTimeLine iUpdateTimeLine;

    public Share(Context context, int userID, int postID, IUpdateTimeLine iUpdateTimeLine) {
        //this.delegate = delegate;
        this.userID = userID;
        this.postID = postID;
        this.context = context;
        this.iUpdateTimeLine = iUpdateTimeLine;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {

        WebserviceGET webserviceGET = new WebserviceGET(URLs.SHARE_POST, new ArrayList<>(
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
        if (serverAnswer != null && serverAnswer.getSuccessStatus() && iUpdateTimeLine!= null)
            iUpdateTimeLine.notifyUpdateTimeLineShare(postID);

        //if webservice.execute() throws exception
        /*if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());*/
    }
}
