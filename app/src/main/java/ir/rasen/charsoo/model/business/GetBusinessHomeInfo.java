package ir.rasen.charsoo.model.business;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessHomeInfo extends AsyncTask<Void, Void, Business> {
    private static final String TAG = "GetUserHomeInfo";
    private IWebserviceResponse delegate = null;
    private int businessID;
    private int userID;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetBusinessHomeInfo(Context context,int businessID,int userID,IWebserviceResponse delegate) {
        this.businessID = businessID;
        this.delegate = delegate;
        this.userID = userID;
        this.context = context;
    }

    @Override
    protected Business doInBackground(Void... voids) {
        Business business = new Business();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BUSINESS_HOME_INFO,new ArrayList<>(
                Arrays.asList(String.valueOf(businessID),String.valueOf(userID))));

        try {
            serverAnswer = webserviceGET.execute(context);

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                business.id = businessID;
                business.businessIdentifier = jsonObject.getString(Params.BUSINESS_USERNAME_STRING);
                business.userID = jsonObject.getInt(Params.USER_ID_INT);
                business.name = jsonObject.getString(Params.BUSINESS_NAME);//mhfathi
                business.profilePictureId = jsonObject.getInt(Params.PROFILE_PICTURE_ID);
                business.coverPictureId = jsonObject.getInt(Params.COVER_PICTURE_ID);
                business.category = jsonObject.getString(Params.CATEGORY);
                business.subcategory = jsonObject.getString(Params.SUBCATEGORY);
                business.description = jsonObject.getString(Params.DESCRIPTION_STRING);
                business.reviewsNumber = jsonObject.getInt(Params.REVIEWS_NUMBER);
                business.followersNumber = jsonObject.getInt(Params.FOLLOWERS_NUMBER);
                business.isFollowing = jsonObject.getBoolean(Params.IS_FOLLOWING);
                business.rate = jsonObject.getInt(Params.RATE);

                return business;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Business result) {
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
