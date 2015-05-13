package ir.rasen.charsoo.webservices.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IUnfollowBusiness;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class UnFollowBusiness extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UnFollowBusiness";

    //private IWebserviceResponse delegate = null;
    private int userID;
    private int businessID;
    private ServerAnswer serverAnswer;
    private IUnfollowBusiness iUnfollowBusiness;
    private Context context;

    public UnFollowBusiness(Context context,int userID, int businessID,IUnfollowBusiness iUnfollowBusiness) {
        this.userID = userID;
        this.businessID = businessID;
        //this.delegate = delegate;
        this.iUnfollowBusiness = iUnfollowBusiness;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.UN_FOLLOW_BUSINESS, new ArrayList<>(
                Arrays.asList(String.valueOf(userID), String.valueOf(businessID))));


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
        iUnfollowBusiness.notifyUnfollowBusiness(businessID);
      /*  if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus()) {
            delegate.getResult(result);
            iUnfollowBusiness.notifyUnfollowBusiness(businessID);
        }
        else
            delegate.getError(serverAnswer.getErrorCode());*/
    }
}
