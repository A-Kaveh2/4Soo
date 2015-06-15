package ir.rasen.charsoo.model.review;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.object.Review;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessReviews extends AsyncTask<Void, Void, ArrayList<Review>> {
    private static final String TAG = "GetBusinessReviews";
    private IWebserviceResponse delegate = null;
    private int userIntId;
    private int businessID;
    private int afterThisID;
    private int limitation;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetBusinessReviews(Context context,int userIntId,int businessID, int afterThisID, int limitation, IWebserviceResponse delegate) {
        this.userIntId=userIntId;
        this.businessID = businessID;
        this.afterThisID = afterThisID;
        this.limitation = limitation;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... voids) {
        ArrayList<Review> list = new ArrayList<Review>();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BUSINESS_REVIEWS, new ArrayList<>(
                Arrays.asList(String.valueOf(userIntId), String.valueOf(businessID),
                        String.valueOf(afterThisID), String.valueOf(limitation))));

        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                     JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Review review = new Review();
                    review.id = jsonObject.getInt(Params.REVIEW_ID);
                    review.userID = jsonObject.getInt(Params.USER_ID_INT);
                    review.userName = jsonObject.getString(Params.USER_NAME_STRING);
                    review.rate = jsonObject.getInt(Params.RATE);
                    review.userPicutreId = jsonObject.getInt(Params.USER_PROFILE_PICTURE_ID);
                    review.text = jsonObject.getString(Params.TEXT);
                    list.add(review);
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
    protected void onPostExecute(ArrayList<Review> result) {
        /*if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);*/

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
