package ir.rasen.charsoo.model.business;

import android.content.Context;
import android.os.AsyncTask;

import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.helper.Hashtag;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebservicePOST;


/**
 * Created by android on 12/16/2014.
 */
public class UpdateBusinessProfileInfo extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateBusinessProfileInfo";

    private IWebserviceResponse delegate = null;
    private Business business;
    private ServerAnswer serverAnswer;
    private Context context;

    public UpdateBusinessProfileInfo(Context context,Business business,IWebserviceResponse delegate) {
        this.business = business;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_PROFILE_BUSINESS);

        try {
            webservicePOST.addParam(Params.BUSINESS_ID_STRING,String.valueOf(business.id));
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
            webservicePOST.addParam(Params.DESCRIPTION_STRING, business.description);
            webservicePOST.addParam(Params.WORK_DAYS, business.workTime.getWorkDaysString());
            webservicePOST.addParam(Params.WORK_TIME_OPEN, String.valueOf(business.workTime.getTimeWorkOpenWebservice()));
            webservicePOST.addParam(Params.WORK_TIME_CLOSE, String.valueOf(business.workTime.getTimeWorkCloseWebservice()));
            webservicePOST.addParam(Params.PHONE, business.phone);
            webservicePOST.addParam(Params.STATE, business.state);
            webservicePOST.addParam(Params.CITY, business.city);
            webservicePOST.addParam(Params.ADDRESS, business.address);
            webservicePOST.addParam(Params.LOCATION_LATITUDE, business.location_m.getLatitude());
            webservicePOST.addParam(Params.LOCATION_LONGITUDE, business.location_m.getLongitude());
            webservicePOST.addParam(Params.EMAIL, business.email);
            webservicePOST.addParam(Params.MOBILE, business.mobile);
            webservicePOST.addParam(Params.HASHTAG_LIST, Hashtag.getStringFromList(business.hashtagList));

            serverAnswer = webservicePOST.execute(context);
            if (serverAnswer.getSuccessStatus())
                return ResultStatus.getResultStatus(serverAnswer);
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {


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
