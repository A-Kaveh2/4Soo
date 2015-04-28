package ir.rasen.charsoo.webservices.post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetPost extends AsyncTask<Void, Void, Post> {
    private static final String TAG = "GetPosts";
    private IWebserviceResponse delegate = null;
    private int postId;
    private int userId;
    private Context context;
    private Post.GetPostType getPostType;
    private ServerAnswer serverAnswer;
    private int businessId;

    public GetPost(Context context,int userId,int businessId,int postId,Post.GetPostType getPostType, IWebserviceResponse delegate) {
        this.postId = postId;
        this.delegate = delegate;
        this.userId = userId;
        this.context = context;
        this.getPostType = getPostType;
        //businessId is needed if you want run GetPost for getFromJSONObjectBusiness
        this.businessId = businessId;
    }

    @Override
    protected Post doInBackground(Void... voids) {
        Post post = new Post();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_USER_POST, new ArrayList<>(
                Arrays.asList(String.valueOf(userId),String.valueOf(postId))));


        try {
            serverAnswer = webserviceGET.execute(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                switch (getPostType){
                    case BUSINESS:
                        post = Post.getFromJSONObjectBusiness(businessId,jsonObject);
                        break;
                    case SHARE:
                        post = Post.getFromJSONObjectShare(jsonObject);
                        break;
                    case TIMELINE:
                        post = Post.getFromJSONObjectTimeLine(jsonObject);
                        break;
                    case SEARCH:
                        post = Post.getFromJSONObjectSearch(jsonObject);
                        break;
                }

                return post;
            }

        } catch (Exception e) {

            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Post result) {

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
