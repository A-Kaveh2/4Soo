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
public class UpdatePost extends AsyncTask<Void, Void, Post> {
    private static final String TAG = "UpdatePost";

    private IWebserviceResponse delegate = null;
    private Post post;
    private ServerAnswer serverAnswer;
    private Context context;

    public UpdatePost(Context context,Post post,IWebserviceResponse delegate) {
        this.post = post;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected Post doInBackground(Void... voids) {
        WebservicePOST webservicePOST = new WebservicePOST(URLs.UPDATE_POST);

        try {
            webservicePOST.addParam(Params.POST_ID_INT, String.valueOf(post.id));
            webservicePOST.addParam(Params.POST_TITLE_STRING, post.title);
            webservicePOST.addParam(Params.POST_PICTURE_STRING, post.picture);
            webservicePOST.addParam(Params.POST_DESCRIPTION_STRING, post.description);
            webservicePOST.addParam(Params.POST_PRICE_STRING, post.price);
            webservicePOST.addParam(Params.POST_CODE_STRING, post.code);
            webservicePOST.addParam(Params.HASHTAG_LIST, Hashtag.getStringFromList(post.hashtagList));
            //discount will be added  next phase
            webservicePOST.addParam(Params.DISCOUNT,"0");

            serverAnswer = webservicePOST.execute(context);
            if (serverAnswer.getSuccessStatus())
                return post;
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
            delegate.getError(ServerAnswer.EXECUTION_ERROR,TAG);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode(),TAG);
    }
}
