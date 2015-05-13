package ir.rasen.charsoo.webservices.comment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;



import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.classes.Comment;
import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.view.interface_m.ICommentChange;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class UpdateComment extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "UpdateComment";

    private IWebserviceResponse delegate = null;
    private Comment comment;
    private ServerAnswer serverAnswer;
    private ICommentChange iCommentChange= null;
    private Context context;

    public UpdateComment(Context context,Comment comment,IWebserviceResponse delegate,ICommentChange iCommentChange) {
        this.delegate = delegate;
        this.comment = comment;
        this.iCommentChange = iCommentChange;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.UPDATE_COMMENT,new ArrayList<>(
                Arrays.asList(String.valueOf(comment.userID), String.valueOf(comment.id),comment.text)));

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
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus()) {
            delegate.getResult(result);
            iCommentChange.notifyUpdateComment(comment);
        }
        else
            delegate.getError(serverAnswer.getErrorCode());
    }
}
