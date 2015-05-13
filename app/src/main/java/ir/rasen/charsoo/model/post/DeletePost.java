package ir.rasen.charsoo.webservices.post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IDeletePost;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class DeletePost extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "DeletePost";

    private IWebserviceResponse delegate = null;
    private int businessID;
    private int postID;
    private ServerAnswer serverAnswer;
    private Context context;
    private IDeletePost iDeletePost;

    public DeletePost(Context context,int businessID, int postID,IDeletePost iDeletePost) {
        this.businessID = businessID;
        this.postID = postID;
        this.context = context;
        this.iDeletePost = iDeletePost;
        //this.delegate = delegate;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.DELETE_POST,new ArrayList<>(
                Arrays.asList(String.valueOf(businessID),String.valueOf( postID))));

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

        if (serverAnswer.getSuccessStatus())
            iDeletePost.notifyDeletePost(postID);

        //if webservice.execute() throws exception
       /* if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());*/
    }
}
