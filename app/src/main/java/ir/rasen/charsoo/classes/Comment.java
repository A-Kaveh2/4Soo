package ir.rasen.charsoo.classes;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.charsoo.ActivityComments;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;


/**
 * Created by android on 12/17/2014.
 */
public class Comment {
    public int id;
    public int businessID;
    public int postID;
    public int userID;
    public String username;
    public int userProfilePictureID;
    public String text;


    public Comment(int userID, String userIdentifier, String text) {
        //just for the test
        this.userID = userID;
        this.username = userIdentifier;
        this.text = text;
    }

    public Comment(int id,int userID, String userIdentifier, String text) {
        //just for the test
        this.id = id;
        this.userID = userID;
        this.username = userIdentifier;
        this.text = text;
    }

    public Comment() {

    }

    public static ArrayList<Comment> getFromJSONArray(JSONArray jsonArray) throws Exception {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonObjectComment = jsonArray.getJSONObject(j);

            Comment comment = new Comment();
            comment.id = jsonObjectComment.getInt(Params.COMMENT_ID);
            comment.userID = jsonObjectComment.getInt(Params.USER_ID);
            comment.username = jsonObjectComment.getString(Params.USER_NAME);
            comment.userProfilePictureID = jsonObjectComment.getInt(Params.PROFILE_PICTURE_ID);
            comment.text = jsonObjectComment.getString(Params.TEXT);
            comments.add(comment);
        }

        return comments;
    }

    private static boolean isCommentOwner(int commentUserId, Context context) {
        if (commentUserId == LoginInfo.getUserId(context))
            return true;
        else
            return false;
    }

    public static void openCommentActivity(Context context,boolean isOwner, int postId, int postOwnerBusinessId) {
        Intent intent = new Intent(context, ActivityComments.class);
        intent.putExtra(Params.POST_ID, postId);
        intent.putExtra(Params.POST_OWNER_BUSINESS_ID, postOwnerBusinessId);
        intent.putExtra(Params.IS_OWNER, isOwner);
        context.startActivity(intent);
    }

}
