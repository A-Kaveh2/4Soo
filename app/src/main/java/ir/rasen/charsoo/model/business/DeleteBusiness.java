package ir.rasen.charsoo.model.business;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IChangeBusiness;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class DeleteBusiness extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "DeleteBusiness";

    private IWebserviceResponse delegate = null;
    private int businessID;
    private int userID;
    private ServerAnswer serverAnswer;
    private Context context;
    private IChangeBusiness iChangeBusiness;

    public DeleteBusiness(Context context,int userID, int businessID,IChangeBusiness iChangeBusiness) {
        //this.delegate = delegate;
        this.businessID = businessID;
        this.userID = userID;
        this.context = context;
        this.iChangeBusiness = iChangeBusiness;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.DELETE_BUSINESS, new ArrayList<>(
                Arrays.asList(String.valueOf(businessID),String.valueOf( userID))));

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
        if (serverAnswer != null && serverAnswer.getSuccessStatus())
            iChangeBusiness.notifyDeleteBusiness(businessID);

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
