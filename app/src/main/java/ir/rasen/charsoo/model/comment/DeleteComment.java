package ir.rasen.charsoo.model.comment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.ICommentChange;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class DeleteComment extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "DeleteComment ";
    private ICommentChange iCommentChange = null;
    private int businessID;
    private int commentID;
    private ServerAnswer serverAnswer;
    private Context context;

    //if userID = comment.userID delete the comment which write the user with id = userID
    //if userID != comment.userID (user is not the writer) and userID == comment.businessID delete the comment which user with id=userID is owner of the business which is owner of the post

    public DeleteComment(Context context, int businessID, int commentID, ICommentChange iCommentChange) {
        this.businessID = businessID;
        this.commentID = commentID;
        this.iCommentChange = iCommentChange;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.DELETE_COMMENT, new ArrayList<>(
                Arrays.asList(String.valueOf(businessID), String.valueOf(commentID))));

        try {
            serverAnswer = webserviceGET.execute(context);
            if (serverAnswer.getSuccessStatus())
                return ResultStatus.getResultStatus(serverAnswer);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultStatus result) {
        //if webservice.execute() throws exception
        if (serverAnswer != null && serverAnswer.getSuccessStatus())
            iCommentChange.notifyDeleteComment(commentID);
    }
}
