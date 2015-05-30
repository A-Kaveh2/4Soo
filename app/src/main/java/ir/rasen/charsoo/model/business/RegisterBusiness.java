package ir.rasen.charsoo.model.business;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.helper.Hashtag;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebservicePOST;


/**
 * Created by android on 12/16/2014.
 */
public class RegisterBusiness extends AsyncTask<Void, Void, Integer> {
    private static final String TAG = "RegisterBusiness";

    private IWebserviceResponse delegate = null;
    private Business business;
    private ServerAnswer serverAnswer;
    private Context context;

    public RegisterBusiness(Context context,Business business, IWebserviceResponse delegate) {
        this.business = business;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.REGISTER_BUSINESS);



        try {
            //TODO remove test part
            //for the test
            webservicePOST.addParam(Params.USER_ID, String.valueOf(LoginInfo.getUserId(context)));

            //webservicePOST.addParam(Params.USER_ID, String.valueOf(business.userID));
            webservicePOST.addParam(Params.BUSINESS_ID, business.businessIdentifier);
            webservicePOST.addParam(Params.NAME, business.name);
            webservicePOST.addParam(Params.EMAIL, business.email);
            if (business.coverPicture != null)
                webservicePOST.addParam(Params.COVER_PICTURE, business.coverPicture);
            else
                webservicePOST.addParam(Params.COVER_PICTURE, "");
            if (business.profilePicture != null)
                webservicePOST.addParam(Params.PROFILE_PICTURE, business.profilePicture);
            else
                webservicePOST.addParam(Params.PROFILE_PICTURE, "");
            webservicePOST.addParam(Params.CATEGORY_ID, String.valueOf(business.categoryID));
            webservicePOST.addParam(Params.SUB_CATEGORY_ID, String.valueOf(business.subCategoryID));
            webservicePOST.addParam(Params.DESCRIPTION, business.description);
            webservicePOST.addParam(Params.WORK_DAYS, business.workTime.getWorkDaysString());
            webservicePOST.addParam(Params.WORK_TIME_OPEN, business.workTime.getTimeWorkOpenWebservice());
            webservicePOST.addParam(Params.WORK_TIME_CLOSE, business.workTime.getTimeWorkCloseWebservice());
            webservicePOST.addParam(Params.PHONE, business.phone);
            webservicePOST.addParam(Params.STATE, business.state);
            webservicePOST.addParam(Params.CITY, business.city);
            webservicePOST.addParam(Params.ADDRESS, business.address);
            webservicePOST.addParam(Params.LOCATION_LATITUDE, business.location_m.getLatitude());
            webservicePOST.addParam(Params.LOCATION_LONGITUDE, business.location_m.getLongitude());
            webservicePOST.addParam(Params.EMAIL, business.email);
            webservicePOST.addParam(Params.MOBILE, business.mobile);
            webservicePOST.addParam(Params.WEBSITE, business.webSite);
            webservicePOST.addParam(Params.HASHTAG_LIST, Hashtag.getStringFromList(business.hashtagList));


            serverAnswer = webservicePOST.execute(context);
            if (serverAnswer.getSuccessStatus()) {
                int busienssId = 0;
                JSONObject jsonObject = serverAnswer.getResult();
                if (jsonObject != null) {
                    busienssId = jsonObject.getInt(Params.BUSINESS_ID);
                }
                return busienssId;

            } else
                return 0;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {

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
