package ir.rasen.charsoo.model.search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.SearchItemBusiness;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class SearchBusinessesLocation extends AsyncTask<Void, Void, ArrayList<SearchItemBusiness>> {
    private static final String TAG = "SearchBusinessesLocation";

    private IWebserviceResponse delegate = null;

    //private int id;
    private String searchText;
    private String location_latitude;
    private String location_longitude;
    private int subcategoryId;
    private ServerAnswer serverAnswer;
    private int beforThisId;
    private int limitataion;
    private Context context;


    public SearchBusinessesLocation(Context context, String searchText,int subcategoryId, String location_latitude,String location_longitude,int beforThisId,int limitation,IWebserviceResponse delegate) {
        //this.id = id;
        this.searchText = searchText;
        this.location_latitude = location_latitude;
        this.location_longitude = location_longitude;
        this.delegate = delegate;
        this.subcategoryId = subcategoryId;
        this.beforThisId = beforThisId;
        this.limitataion = limitation;
        this.context = context;
    }

    @Override
    protected ArrayList<SearchItemBusiness> doInBackground(Void... voids) {
        ArrayList<SearchItemBusiness> list = new ArrayList<>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.SEARCH_BUSINESS_LOCATION,new ArrayList<>(
                Arrays.asList( searchText,String.valueOf(subcategoryId),
                        location_latitude,location_longitude,
                        String.valueOf(beforThisId),String.valueOf(limitataion))));

        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new SearchItemBusiness(context.getResources(),jsonObject.getInt(Params.BUSINESS_ID),
                            jsonObject.getInt(Params.BUSINESS_PROFILE_PICUTE_ID),
                            jsonObject.getString(Params.BUSINESS_USER_NAME),
                            jsonObject.getDouble(Params.DISTANCE)));
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
    protected void onPostExecute(ArrayList<SearchItemBusiness> result) {


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
