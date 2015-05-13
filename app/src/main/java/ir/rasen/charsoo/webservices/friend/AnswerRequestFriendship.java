package ir.rasen.charsoo.webservices.friend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;

/**
 * Created by android on 12/16/2014.
 */
public class AnswerRequestFriendship extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "AnswerRequestFriendship";


    private int applicatorUserID;
    private int requestedUserID;
    private boolean answer;
    private ServerAnswer serverAnswer;
    private Context context;

    public AnswerRequestFriendship(Context context, int requestedUserID, int applicatorUserID, boolean answer) {
        this.applicatorUserID = applicatorUserID;
        this.requestedUserID = requestedUserID;
        this.answer = answer;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.ANSWER_REQUEST_FRIENDSHIP, new ArrayList<>(
                Arrays.asList(String.valueOf(requestedUserID),
                        String.valueOf(applicatorUserID)
                        , String.valueOf(answer))));


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

    }
}
