package ir.rasen.charsoo.model.comment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.object.CommentNotification;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetAllCommentNotifications extends AsyncTask<Void, Void, ArrayList<CommentNotification>> {
    private static final String TAG = "GetAllCommentNotifications";

    private IWebserviceResponse delegate = null;
    private int userId;
    private int beforeThisId;
    private int limitation;
    private ServerAnswer serverAnswer;
    private Context context;


    public GetAllCommentNotifications(Context context,int userId, int beforeThisId, int limitation, IWebserviceResponse delegate) {
        this.delegate = delegate;
        this.userId = userId;
        this.beforeThisId = beforeThisId;
        this.limitation = limitation;
        this.context = context;
    }

    @Override
    protected ArrayList<CommentNotification> doInBackground(Void... voids) {
        ArrayList<CommentNotification> list = new ArrayList<CommentNotification>();

        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_ALL_COMMENT_NOTIFICATIONS, new ArrayList<>(
                Arrays.asList(String.valueOf(userId), String.valueOf(beforeThisId), String.valueOf(limitation))));


        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String userPicture = jsonObject.getString(Params.USER_PROFILE_PICTURE);
                    JSONObject userJson = new JSONObject(userPicture);
                    userPicture = userJson.getString(Params.IMAGE);

                    String postPicture = jsonObject.getString(Params.POST_PICUTE);
                    JSONObject postJson = new JSONObject(postPicture);
                    postPicture = postJson.getString(Params.IMAGE);

                    list.add(new CommentNotification(
                            jsonObject.getInt(Params.COMMENT_ID),
                            jsonObject.getInt(Params.POST_ID_INT),
                            jsonObject.getString(Params.USER_NAME_STRING),
                            userPicture,
                            postPicture,
                            jsonObject.getString(Params.TEXT),
                            jsonObject.getInt(Params.INTERVAL_TIME)));

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
    protected void onPostExecute(ArrayList<CommentNotification> result) {
       /* if (result == null)
            delegate.getError(serverAnswer.getErrorCode());
        else
            delegate.getResult(result);
*/
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
