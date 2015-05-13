package ir.rasen.charsoo.model.business;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class BlockUser extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "BlockUser";

    private IWebserviceResponse delegate = null;
    private int businessID;
    private int userID;
    private ServerAnswer serverAnswer;
    private Context context;

    public BlockUser(Context context,int businessID, int userID,IWebserviceResponse delegate) {
        this.businessID = businessID;
        this.userID = userID;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.BLOCK_USER, new ArrayList<>(
                Arrays.asList(String.valueOf(businessID)
                        ,String.valueOf(userID))));


        try {
            serverAnswer = webserviceGET.execute(context);
            if (serverAnswer.getSuccessStatus()) {
                return ResultStatus.getResultStatus(serverAnswer);
            }
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
