package ir.rasen.charsoo.model;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.controller.object.SubCategory;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetStateCities extends AsyncTask<Void, Void, ArrayList<String>> {
    private static final String TAG = "GetStateCities";
    private IWebserviceResponse delegate = null;
    private ServerAnswer serverAnswer;
    private Context context;
    private String stateName;

    public GetStateCities(Context context, String currentState, IWebserviceResponse delegate) {
        stateName=currentState;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<SubCategory> list = new ArrayList<>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_STATE_CITIES,new ArrayList<>(
                Arrays.asList(stateName)));

        try {


            // TODO fetch list from server
//            serverAnswer = webserviceGET.executeList(context);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        ArrayList<String> list=new ArrayList<>();
        list.addAll(Arrays.asList(context.getResources().getStringArray(R.array.default_CityList)));
        delegate.getResult(list);


        // TODO fetch list from server
        /*if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR,TAG);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode(),TAG);*/
    }
}
