package ir.rasen.charsoo.model.business;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.helper.Hashtag;
import ir.rasen.charsoo.controller.helper.Location_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.controller.helper.WorkTime;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetBusinessProfileInfo extends AsyncTask<Void, Void, Business> {
    private static final String TAG = "GetBusinessProfileInfo";
    private IWebserviceResponse delegate = null;
    private int businessID;
    private ServerAnswer serverAnswer;
    private Context context;


    public GetBusinessProfileInfo(Context context,int businessID,IWebserviceResponse delegate) {
        this.delegate = delegate;
        this.businessID = businessID;
        this.context = context;
    }

    @Override
    protected Business doInBackground(Void... voids) {
        Business business = new Business();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_BUSINESS_PROFILE_INFO, new ArrayList<>(
                Arrays.asList(String.valueOf(businessID))));

        try {

            serverAnswer = webserviceGET.execute(context);

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                business.id = businessID;
                business.name = jsonObject.getString(Params.NAME);
                try{
                    business.profilePictureId = jsonObject.getInt(Params.PROFILE_PICTURE);
                }
                catch (Exception e){
                    business.profilePictureId = jsonObject.getInt(Params.COVER_PICTURE);
                }
                business.coverPicture = jsonObject.getString(Params.COVER_PICTURE);
                business.category = jsonObject.getString(Params.CATEGORY);
                business.subcategory = jsonObject.getString(Params.SUBCATEGORY);
                business.description = jsonObject.getString(Params.DESCRIPTION_STRING);
                WorkTime workTime = new WorkTime();
                workTime.setWorkDaysFromString(jsonObject.getString(Params.WORK_DAYS));
                workTime.setTimeWorkOpenFromString(jsonObject.getString(Params.WORK_TIME_OPEN));
                workTime.setTimeWorkCloseFromString(jsonObject.getString(Params.WORK_TIME_CLOSE));
                business.workTime = workTime;
                business.phone = jsonObject.getString(Params.PHONE);
                business.state = jsonObject.getString(Params.STATE);
                business.city = jsonObject.getString(Params.CITY);
                business.address = jsonObject.getString(Params.ADDRESS);
                business.location_m = new Location_M(jsonObject.getString(Params.LOCATION_LATITUDE),
                        jsonObject.getString(Params.LOCATION_LONGITUDE));
                business.email = jsonObject.getString(Params.EMAIL);
                //business.webSite = jsonObject.getString(Params.WEBSITE);
                business.mobile = jsonObject.getString(Params.MOBILE);
                business.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));

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
