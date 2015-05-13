package ir.rasen.charsoo.webservices.business;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.charsoo.classes.Category;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessGategories extends AsyncTask<Void, Void, ArrayList<Category>> {
    private static final String TAG = "GetBusinessGategories";
    private IWebserviceResponse delegate = null;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetBusinessGategories(Context context,IWebserviceResponse delegate){
        this.delegate = delegate;
        this.context = context;
    }
    @Override
    protected ArrayList<Category> doInBackground(Void... voids) {
        ArrayList<Category> list = new ArrayList<Category>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BUSINESS_CATEGORIES,null);
        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    //TODO remove test part
                    //list.add(new Category(jsonObject.getString(Params.ID),jsonObject.getString(Params.CATEGORY)));

                    //for the test. getBusinessCategory doesn't return category.id by the now!
                    list.add(new Category(Integer.valueOf(jsonObject.getString(Params.CATEGORY_ID)),jsonObject.getString(Params.CATEGORY)));
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
    protected void onPostExecute(ArrayList<Category> result) {
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (!serverAnswer.getSuccessStatus())
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
    }
}
