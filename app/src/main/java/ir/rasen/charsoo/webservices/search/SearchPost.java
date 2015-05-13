package ir.rasen.charsoo.webservices.search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.SearchItemPost;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class SearchPost extends AsyncTask<Void, Void, ArrayList<SearchItemPost>> {
    private static final String TAG = "SearchPost";

    private IWebserviceResponse delegate = null;

    private String searchText;
    private ServerAnswer serverAnswer;
    private int beforeThisId;
    private int limitation;
    private Context context;

    public SearchPost(Context context,String searchText,int beforeThisId,int limitation,IWebserviceResponse delegate) {
        this.searchText = searchText;
        this.delegate = delegate;
        this.beforeThisId = beforeThisId;
        this.limitation = limitation;
        this.context = context;

    }

    @Override
    protected ArrayList<SearchItemPost> doInBackground(Void... voids) {
        ArrayList<SearchItemPost> list = new ArrayList<>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.SEARCH_POST,new ArrayList<>(
                Arrays.asList(searchText,String.valueOf(beforeThisId),String.valueOf(limitation))));


        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new SearchItemPost(jsonObject.getInt(Params.POST_ID),
                            jsonObject.getInt(Params.POST_PICTURE_ID),""));
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
    protected void onPostExecute(ArrayList<SearchItemPost> result) {

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
