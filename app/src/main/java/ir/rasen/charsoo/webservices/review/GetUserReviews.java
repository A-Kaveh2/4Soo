package ir.rasen.charsoo.webservices.review;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.internal.id;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.classes.Review;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetUserReviews extends AsyncTask<Void, Void, ArrayList<Review>> {
    private static final String TAG = "GetUserReviews";
    private IWebserviceResponse delegate = null;
    private int userID;
    private int afterThisIndex;
    private int limitation;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetUserReviews(Context context,int userID, int afterThisIndex, int limitation, IWebserviceResponse delegate) {
        this.userID = userID;
        this.afterThisIndex = afterThisIndex;
        this.limitation = limitation;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... voids) {
        ArrayList<Review> list = new ArrayList<Review>();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_USER_REVIEWS, new ArrayList<>(
                Arrays.asList(String.valueOf(userID), String.valueOf(afterThisIndex), String.valueOf(limitation))));


        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Review review = new Review();
                    review.id = jsonObject.getInt(Params.REVIEW_ID);
                    review.userID = userID;
                    review.businessID = jsonObject.getInt(Params.BUSINESS_ID);
                    review.businessUserName = jsonObject.getString(Params.BUSINESS_USER_NAME);
                    review.businessPicutreId = jsonObject.getInt(Params.BUSINESS_PROFILE_PICUTE_ID);
                    review.text = jsonObject.getString(Params.TEXT);
                    review.rate = jsonObject.getInt(Params.RATE);
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
       /* if (result == null)
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
