package ir.rasen.charsoo.webservices.business;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessFollowers extends AsyncTask<Void, Void, ArrayList<BaseAdapterItem>> {
    private static final String TAG = "GetBusinessFollowers";

    private IWebserviceResponse delegate = null;
    private int businessID;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetBusinessFollowers(Context context,int businessID,IWebserviceResponse delegate) {
        this.businessID = businessID;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ArrayList<BaseAdapterItem> doInBackground(Void... voids) {
        ArrayList<BaseAdapterItem> list = new ArrayList<BaseAdapterItem>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BUSINESS_FOLLOWERS,new ArrayList<>(
                Arrays.asList(String.valueOf(businessID))));


        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new BaseAdapterItem(context.getResources(),jsonObject.getInt(Params.USER_ID),
                            jsonObject.getInt(Params.USER_PROFILE_PICTURE_ID),
                            jsonObject.getString(Params.USER_NAME)));
                }
                return list;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<BaseAdapterItem> result) {

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
