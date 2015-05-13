package ir.rasen.charsoo.model.business;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetBlockedUsers extends AsyncTask<Void, Void, ArrayList<BaseAdapterItem>> {
    private static final String TAG = "GetBlockedUsers";

    private IWebserviceResponse delegate = null;
    private int businessID;
    private ServerAnswer serverAnswer;
    private Context context;
    private int beforeThisId,limitation;

    public GetBlockedUsers(Context context,int businessID,int beforeThisId,int limitation,IWebserviceResponse delegate) {
        this.businessID = businessID;
        this.delegate = delegate;
        this.context = context;
        this.beforeThisId = beforeThisId;
        this.limitation = limitation;
    }

    @Override
    protected ArrayList<BaseAdapterItem> doInBackground(Void... voids) {
        ArrayList<BaseAdapterItem> list = new ArrayList();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BLOCKED_USERS, new ArrayList<>(
                Arrays.asList(String.valueOf(businessID),
                        String.valueOf(beforeThisId),
                        String.valueOf(limitation))));

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
      /*  if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);*/

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
