package ir.rasen.charsoo.model.post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.helper.Hashtag;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.WebservicePOST;

/**
 * Created by android on 12/16/2014.
 */
public class AddPost extends AsyncTask<Void, Void, Post> {
    private static final String TAG = "AddPost";

    private IWebserviceResponse delegate = null;
    private Post post;
    private ServerAnswer serverAnswer;
    private Context context;

    public AddPost(Context context,Post post,IWebserviceResponse delegate) {
        this.post = post;
        this.delegate = delegate;
        this.context= context;
    }

    @Override
    protected Post doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.ADD_POST);

        try {
            webservicePOST.addParam(Params.BUSINESS_ID, String.valueOf(post.businessID));
            webservicePOST.addParam(Params.TITLE, post.title);
            webservicePOST.addParam(Params.PICTURE, post.picture);
            webservicePOST.addParam(Params.DESCRIPTION, post.description);
            webservicePOST.addParam(Params.PRICE, post.price);
            webservicePOST.addParam(Params.CODE, post.code);
            webservicePOST.addParam(Params.HASHTAG_LIST, Hashtag.getStringFromList(post.hashtagList));

            serverAnswer = webservicePOST.execute(context);
            if (serverAnswer.getSuccessStatus()) {
                post.id = Integer.valueOf(serverAnswer.getResult().getString(Params.POST_ID));
                return post;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Post result) {


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
