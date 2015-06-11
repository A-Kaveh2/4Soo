package ir.rasen.charsoo.model.comment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetPostAllComments extends AsyncTask<Void, Void, ArrayList<Comment>> {
    private static final String TAG = "GetPostAllComments";
    private IWebserviceResponse delegate = null;
    private int postID;
    private int beforThisId;
    private int limitaion;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetPostAllComments(Context context,int postID, int beforThisId, int limitaion, IWebserviceResponse delegate) {
        this.delegate = delegate;
        this.postID = postID;
        this.beforThisId = beforThisId;
        this.limitaion = limitaion;
        this.context = context;
    }

    @Override
    protected ArrayList<Comment> doInBackground(Void... voids) {
        ArrayList<Comment> list = new ArrayList<Comment>();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_COMMENTS, new ArrayList<>(
                Arrays.asList(String.valueOf(postID), String.valueOf(beforThisId),String.valueOf(limitaion))));

        try {
              serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Comment comment = new Comment();
                    comment.id = jsonObject.getInt(Params.COMMENT_ID);
                    comment.userID = jsonObject.getInt(Params.USER_ID_INT_FOR_GETPOSTALLCOMMENTS);
                    comment.username = jsonObject.getString(Params.USER_NAME_STRING);
                    comment.userProfilePictureID = jsonObject.getInt(Params.USER_PROFILE_PICTURE_ID);
                    comment.text = jsonObject.getString(Params.COMMENT);

                    list.add(comment);
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
    protected void onPostExecute(ArrayList<Comment> result) {


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
