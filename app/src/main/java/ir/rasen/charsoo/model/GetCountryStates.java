package ir.rasen.charsoo.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.controller.object.Category;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class GetCountryStates extends AsyncTask<Void, Void, ArrayList<String>> {
    private static final String TAG = "GetCountryStates";
    private IWebserviceResponse delegate = null;
    private ServerAnswer serverAnswer;
    private Context context;


    public GetCountryStates(Context context, IWebserviceResponse delegate){
        this.delegate = delegate;
        this.context = context;
    }
    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> list=new ArrayList<>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_COUNTRY_STATES,null);

        try {


            // TODO Fetch States List From Server
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
        list.addAll(Arrays.asList(context.getResources().getStringArray(R.array.states)));
        delegate.getResult(list);

        // TODO Fetch From Server
//        if (serverAnswer == null) {
//            delegate.getError(ServerAnswer.EXECUTION_ERROR,TAG);
//            return;
//        }
//        if (!serverAnswer.getSuccessStatus())
//            delegate.getError(serverAnswer.getErrorCode(),TAG);
//        else
//            delegate.getResult(result);
    }
}
