package ir.rasen.charsoo.model.post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.model.WebserviceGET;
import ir.rasen.charsoo.view.interface_m.IGetNewTimeLinePost;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetTimeLinePost extends AsyncTask<Void, Void, Post> {
    private static final String TAG = "GetTimeLinePosts";
    private IGetNewTimeLinePost iGetNewTimeLinePost = null;
    private ServerAnswer serverAnswer;
    private Context context;
    private  int postId;
    private ArrayList<Post> currentPosts;

    public GetTimeLinePost(Context context, int postId, ArrayList<Post> currentPosts, IGetNewTimeLinePost iGetNewTimeLinePost) {
        this.iGetNewTimeLinePost = iGetNewTimeLinePost;
        this.context = context;
        this.postId = postId;
        this.currentPosts = currentPosts;
    }

    @Override
    protected Post doInBackground(Void... voids) {
        Post targetPost = new Post();
        //currentPosts.get(Post.getIndexOfPost(currentPosts,postId)-1) returns the id of the post before the target post in time line
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_TIME_LINE_POSTS,new ArrayList<>(
                Arrays.asList(String.valueOf(LoginInfo.getUserId(context)),
                        String.valueOf(currentPosts.get(Post.getIndexOfPost(currentPosts,postId)-1).id),
                        String.valueOf(1))));


        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    targetPost = Post.getFromJSONObjectTimeLine(jsonObject);
                    if(targetPost.id == postId)
                        return targetPost;
                }
                return null;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Post result) {
        if (serverAnswer != null && serverAnswer.getSuccessStatus()) {
            iGetNewTimeLinePost.notifyGetNewPost(result);
            return;
        }
    }
}
