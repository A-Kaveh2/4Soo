package ir.rasen.charsoo.model.review;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.object.Review;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IReviewChange;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class DeleteReview extends AsyncTask<Void, Void, ResultStatus> {
    private static final String TAG = "DeleteReview";

    private IWebserviceResponse delegate = null;
    private Review review;
    private ServerAnswer serverAnswer;
    private IReviewChange iReviewChange;
    private Context context;

    public DeleteReview(Context context,Review review, IWebserviceResponse delegate, IReviewChange iReviewChange) {
        this.review = review;
        this.delegate = delegate;
        this.iReviewChange = iReviewChange;
        this.context = context;
    }

    @Override
    protected ResultStatus doInBackground(Void... voids) {
        WebserviceGET webserviceGET = new WebserviceGET(URLs.DELETE_REVIEW, new ArrayList<>(
                Arrays.asList(String.valueOf(review.userID), String.valueOf(review.id))));


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
            delegate.getError(ServerAnswer.EXECUTION_ERROR,TAG);
            return;
        }
        if (serverAnswer.getSuccessStatus()) {
            delegate.getResult(result);
            iReviewChange.notifyDeleteReview(review.id);
        } else
            delegate.getError(serverAnswer.getErrorCode(),TAG);
    }
}
