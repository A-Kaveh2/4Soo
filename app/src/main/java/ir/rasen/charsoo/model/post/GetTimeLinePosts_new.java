package ir.rasen.charsoo.model.post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.model.WebserviceGET;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;

/**
 * Created by android on 12/16/2014.
 */
public class GetTimeLinePosts_new extends AsyncTask<Void, Void, ArrayList<Post>> {
    private static final String TAG = "GetTimeLinePosts";
    private IWebserviceResponse delegate = null;
    private int userId;
    private int fromBusinessPost,fromFriendPost,fromFriendFollow,fromFriendReview;
    private int limitation;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetTimeLinePosts_new(Context context, int userId, int fromBusinessPostId,int fromFriendPostId
            , int fromFriendFollowId,int fromFriendReviewId, int limitation, IWebserviceResponse delegate) {
        this.userId = userId;
        fromBusinessPost=fromBusinessPostId;
        fromFriendPost=fromFriendPostId;
        fromFriendFollow=fromFriendFollowId;
        fromFriendReview=fromFriendReviewId;
        this.limitation = limitation;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ArrayList<Post> doInBackground(Void... voids) {
        ArrayList<Post> list = new ArrayList<Post>();
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_TIME_LINE_POSTS_NEW,new ArrayList<>(
                Arrays.asList(String.valueOf(userId),
                        String.valueOf(fromBusinessPost),String.valueOf(fromFriendPost),
                        String.valueOf(fromFriendReview), String.valueOf(fromFriendFollow),
                        String.valueOf(limitation))));


        try {
            serverAnswer = webserviceGET.executeList(context);
            if (serverAnswer.getSuccessStatus()) {
                JSONArray jsonArray = serverAnswer.getResultList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    try {
                        list.add(Post.getFromJSONObjectTimeLine(jsonObject));
                    }
                    catch (Exception e){
                        Log.e(TAG, e.getMessage());
                    }
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
    protected void onPostExecute(ArrayList<Post> result) {

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
