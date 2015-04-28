package ir.rasen.charsoo.webservices.search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebservicePOST;


/**
 * Created by android on 12/16/2014.
 */
public class SearchBusinessesNearBy extends AsyncTask<Void, Void, ArrayList<BaseAdapterItem>> {
    private static final String TAG = "SearchBusinessesNearBy";

    private IWebserviceResponse delegate = null;

    private int  userID;
    private String searchText;
    private String nearBy;
    private ServerAnswer serverAnswer;
    private Context context;

    public SearchBusinessesNearBy(Context context,int userID, String searchText, String nearBy,IWebserviceResponse delegate) {
        this.delegate = delegate;
        this.userID = userID;
        this.searchText = searchText;
        this.nearBy = nearBy;
        this.context = context;
    }

    @Override
    protected ArrayList<BaseAdapterItem> doInBackground(Void... voids) {
        ArrayList<BaseAdapterItem> list = new ArrayList();

        WebservicePOST webservicePOST = new WebservicePOST(URLs.SEARCH_BUSINESS_NEAR_BY);

        try {
            webservicePOST.addParam(Params.USER_ID,String.valueOf( userID));
            webservicePOST.addParam(Params.SEARCH_TEXT, searchText);
            webservicePOST.addParam(Params.NEAR_BY, nearBy);

            serverAnswer = webservicePOST.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new BaseAdapterItem(context.getResources(),jsonObject.getInt(Params.BUSINESS_ID),
                            jsonObject.getInt(Params.SEARCH_PICTURE_ID),
                            jsonObject.getString(Params.NAME)));
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
