package ir.rasen.charsoo.model.business;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.model.WebserviceGET;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessStrIdAvailability extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "GetBusinessStrIdAvailability";
    private IWebserviceResponse delegate = null;
    private String businessStrId;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetBusinessStrIdAvailability(Context context, String businessStringId, IWebserviceResponse delegate) {
        businessStrId=businessStringId;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.CHECK_BUSINESS_IDENTIFIER,new ArrayList<>(
                Arrays.asList(businessStrId)));

        try {
            serverAnswer = webserviceGET.execute(context);

            if (serverAnswer.getSuccessStatus()){
                return true;
                // TODO: BAD AZ ESLAHE WEB SERVICE AVAILABILITY BAR ASASE PASOKHE SERVER SET SHAVAD
//                return serverAnswer.getResult().getBoolean(Params.IS_STR_ID_AVAILABLE);
            }

        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //if webservice.execute() throws exception
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR,TAG);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode(),TAG);
    }
}
