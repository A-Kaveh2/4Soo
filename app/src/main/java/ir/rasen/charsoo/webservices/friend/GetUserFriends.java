package ir.rasen.charsoo.webservices.friend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetUserFriends extends AsyncTask<Void, Void, ArrayList<BaseAdapterItem>> {
    private static final String TAG = "GetUserFriends";

    private IWebserviceResponse delegate = null;
    private int userID;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetUserFriends(Context context,int userID, IWebserviceResponse delegate) {
        this.userID = userID;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ArrayList<BaseAdapterItem> doInBackground(Void... voids) {
        ArrayList<BaseAdapterItem> list = new ArrayList();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_USER_FRIENDS, new ArrayList<>(
                Arrays.asList(String.valueOf(userID))));

        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new BaseAdapterItem(context.getResources(),jsonObject.getInt(Params.ID),
                            jsonObject.getInt(Params.USER_PROFILE_PICTURE_ID),
                            jsonObject.getString(Params.USER_ID)));
                }
                //this webservice has bug and returns the own user as friend.
                //we should remove the user from friend list
                for (int i = 0;i<list.size();i++){
                    if (list.get(i).getId() == LoginInfo.getUserId(context))
                        list.remove(i);
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
    protected void onPostExecute(ArrayList<BaseAdapterItem> result) {
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
