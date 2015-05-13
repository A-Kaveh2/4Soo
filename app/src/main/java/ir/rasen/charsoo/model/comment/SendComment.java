package ir.rasen.charsoo.model.comment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TestUnit;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class SendComment extends AsyncTask<Void, Void, Integer> {
    private static final String TAG = "SendComment";

    private IWebserviceResponse delegate = null;
    private int userID;
    private int postID;
    private String comment;
    private ServerAnswer serverAnswer;
    private Context context;

    public SendComment(Context context, int userID, int postID, String comment, IWebserviceResponse delegate) {
        this.userID = userID;
        this.postID = postID;
        this.comment = comment;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        WebserviceGET webserviceGET = null;
        try {
            ArrayList arrayList = new ArrayList<>(
                    Arrays.asList(String.valueOf(userID), String.valueOf(postID), comment));
            webserviceGET = new WebserviceGET(URLs.SEND_COMMENT,arrayList );
        }
        catch (Exception e){

            String s = e.getMessage();
            return  null;
        }


        try {
            serverAnswer = webserviceGET.execute(context);
            if (serverAnswer.getSuccessStatus()) {
                if (TestUnit.isTestingCommentActivity)
                    return 1032;
                else {
                    JSONObject jsonObject = serverAnswer.getResult();
                    return jsonObject.getInt(Params.COMMENT_ID);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {


        //if webservice.execute() throws exception
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
