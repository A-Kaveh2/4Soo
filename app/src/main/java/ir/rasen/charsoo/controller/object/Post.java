package ir.rasen.charsoo.controller.object;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.xml.transform.Result;

import ir.rasen.charsoo.view.activity.ActivityPost;
import ir.rasen.charsoo.controller.helper.Hashtag;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;


/**
 * Created by android on 12/17/2014.
 */
public class Post {

    public int id;
    public int businessID;
    public String businessUserName;
    public String businessIdentifier;
    public int businessProfilePictureId;
    public String businessName;

    public int friendIdInt;
    public String friendIdStr;

    //here is 3 types of post: ordinary post, follow announcement post and review annoucement post
    //follow announcement post fields: businessID,businessUserName,userId,userName,type
    //review announcement post fields: businessID,businessUserName,userId,userName,type,description(review json object that contains review text and rate)

    public int userId;//used when post is follow or review announcement
    public String userName;////used when post is follow or review announcement

    public Date creationDate;

    public String title;
    public String picture;
    public int pictureId;
    public String description;
    public String price;
    public String code;
    public boolean isLiked = false;
    public boolean isShared = false;
    public boolean isReported = false;

    public int likeNumber;
    public int commentNumber;
    public int shareNumber;

    public enum Type {CompleteBusiness, Follow, Review, CompleteFriend}

    public enum GetPostType {TIMELINE, SHARE, BUSINESS, SEARCH}

    public GetPostType getPostType;
    public Type type;
    public ArrayList<Comment> lastThreeComments = new ArrayList<Comment>();
    public ArrayList<String> hashtagList = new ArrayList<String>();

    //in case type equals review
    public int reviewRate;
    public String reviewText;


    public static Date setCreationDate(JSONObject object)throws Exception{
        String dateStr = object.getString(Params.CREATION_DATAE_DATE);
        dateStr = dateStr.replace("/Date(", "").replace(")/", "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        //Date date =  dateFormat.parse(dateStr);
        Date date =  new Date(Long.parseLong(dateStr));
        return date;
    }


    public static Type getType(int type) {
        switch (type) {
            case 1:
                return Type.CompleteBusiness;
            case 2:
                return Type.Follow;
            case 3:
                return Type.Review;
            case 4:
                return Type.CompleteFriend;
        }

        return Type.CompleteBusiness;
    }

    public static int getTypeCode(Type type) {
        switch (type) {
            case CompleteBusiness:
                return 1;
            case Follow:
                return 2;
            case Review:
                return 3;
            case CompleteFriend:
                return 4;
        }

        return 0;
    }


    public static Post getFromJSONObjectShare(JSONObject jsonObject) throws Exception {
        Post post = new Post();
        post.id = jsonObject.getInt(Params.POST_ID_INT);
        post.businessIdentifier = jsonObject.getString(Params.BUSINESS_ID_STRING);
        try{ //mhfathi
            post.businessID = jsonObject.getInt(Params.BUSINESS_ID_INT);
        }catch (Exception e){
            post.businessID = jsonObject.getInt(Params.BUSINESS_ID_STRING);
        }
        try{ //mhfathi
            post.businessUserName = jsonObject.getString(Params.BUSINESS_USERNAME_STRING);
        }catch (Exception e){

        }

        post.businessProfilePictureId = jsonObject.getInt(Params.BUSINESS_PROFILE_PICUTE_ID_INT);
        post.title = jsonObject.getString(Params.POST_TITLE_STRING);

        post.pictureId = jsonObject.getInt(Params.POST_PICTURE_ID_INT);
        post.description = jsonObject.getString(Params.POST_DESCRIPTION_STRING);
        post.code = jsonObject.getString(Params.POST_CODE_STRING);
        post.price = jsonObject.getString(Params.POST_PRICE_STRING);

        String comments = jsonObject.getString(Params.Post_COMMENTS_STRING);
        JSONArray jsonArrayComments = new JSONArray(comments);
        post.lastThreeComments = Comment.getFromJSONArray(jsonArrayComments);

        post.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));

        post.isLiked = jsonObject.getBoolean(Params.POST_IS_LIKED);

        try{
            post.isShared = jsonObject.getBoolean(Params.POST_IS_SHARED);
        }catch(Exception ee){
            post.isShared = true;
        }

        //post.likeNumber = jsonObject.getInt(Params.LIKE_NUMBER);
        //post.commentNumber = jsonObject.getInt(Params.COMMENT_NUMBER);
        //post.shareNumber = jsonObject.getInt(Params.SHARE_NUMBER);

        post.creationDate = setCreationDate(jsonObject);
        post.isReported = jsonObject.getBoolean(Params.POST_IS_REPORTED);
        return post;
    }


    public static Post getFromJSONObjectBusiness(int businessID, JSONObject jsonObject) throws Exception {
        Post post = new Post();
        post.id = jsonObject.getInt(Params.POST_ID_INT);
        post.businessID = businessID;
        post.title = jsonObject.getString(Params.POST_TITLE_STRING);
        post.creationDate = setCreationDate(jsonObject);
        post.pictureId = jsonObject.getInt(Params.POST_PICTURE_ID_INT);
        post.description = jsonObject.getString(Params.POST_DESCRIPTION_STRING);
        post.code = jsonObject.getString(Params.POST_CODE_STRING);
        post.price = jsonObject.getString(Params.POST_PRICE_STRING);

        String comments = jsonObject.getString(Params.Post_COMMENTS_STRING);
        JSONArray jsonArrayComments = new JSONArray(comments);


        post.lastThreeComments = Comment.getFromJSONArray(jsonArrayComments);

        post.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));

        post.isLiked = jsonObject.getBoolean(Params.POST_IS_LIKED);
        try {
            post.isShared = jsonObject.getBoolean(Params.POST_IS_SHARED);
        } catch (Exception e) {

        }
        //post.likeNumber = jsonObject.getInt(Params.LIKE_NUMBER);
        //post.commentNumber = jsonObject.getInt(Params.COMMENT_NUMBER);
        //post.shareNumber = jsonObject.getInt(Params.SHARE_NUMBER);

        return post;
    }

    public static Post getFromJSONObjectSearch(JSONObject jsonObject) throws Exception {
        Post post = new Post();
        post.id = jsonObject.getInt(Params.POST_ID_INT);
        post.businessID = jsonObject.getInt(Params.BUSINESS_ID_STRING);
        post.title = jsonObject.getString(Params.POST_TITLE_STRING);
        post.creationDate = setCreationDate(jsonObject);
        post.pictureId = jsonObject.getInt(Params.POST_PICTURE_ID_INT);
        post.description = jsonObject.getString(Params.POST_DESCRIPTION_STRING);
        post.code = jsonObject.getString(Params.POST_CODE_STRING);
        post.price = jsonObject.getString(Params.POST_PRICE_STRING);

        String comments = jsonObject.getString(Params.Post_COMMENTS_STRING);
        JSONArray jsonArrayComments = new JSONArray(comments);


        post.lastThreeComments = Comment.getFromJSONArray(jsonArrayComments);

        post.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));

        post.isLiked = jsonObject.getBoolean(Params.POST_IS_LIKED);
        post.isShared = jsonObject.getBoolean(Params.POST_IS_SHARED);
        post.isReported = jsonObject.getBoolean(Params.POST_IS_REPORTED);
        //post.likeNumber = jsonObject.getInt(Params.LIKE_NUMBER);
        //post.commentNumber = jsonObject.getInt(Params.COMMENT_NUMBER);
        //post.shareNumber = jsonObject.getInt(Params.SHARE_NUMBER);

        return post;
    }

    public static Post getFromJSONObjectTimeLine(JSONObject jsonObject) throws Exception {
        Post post = new Post();
        post.id = jsonObject.getInt(Params.POST_ID_INT);
        post.businessID = jsonObject.getInt(Params.BUSINESS_ID_STRING_TIMELINE);
        post.businessUserName = jsonObject.getString(Params.BUSINESS_USERNAME_STRING);
        post.type = getType(jsonObject.getInt(Params.TYPE));
        post.businessProfilePictureId = jsonObject.getInt(Params.BUSINESS_PROFILE_PICUTE_ID_INT);
        if (post.type == Type.CompleteBusiness) {
            //post.businessProfilePictureId = jsonObject.getInt(Params.BUSINESS_PROFILE_PICUTE_ID_INT);
            post.title = jsonObject.getString(Params.POST_TITLE_STRING);
            post.creationDate = setCreationDate(jsonObject);
            post.pictureId = jsonObject.getInt(Params.POST_PICTURE_ID_INT);
            post.description = jsonObject.getString(Params.POST_DESCRIPTION_STRING);
            post.code = jsonObject.getString(Params.POST_CODE_STRING);
            post.price = jsonObject.getString(Params.POST_PRICE_STRING);
            post.isLiked = jsonObject.getBoolean(Params.POST_IS_LIKED);
            post.isReported = jsonObject.getBoolean(Params.POST_IS_REPORTED);
            post.isShared = jsonObject.getBoolean(Params.POST_IS_SHARED);
            String comments = jsonObject.getString(Params.Post_COMMENTS_STRING);
            JSONArray jsonArrayComments = new JSONArray(comments);

            //
            post.lastThreeComments = Comment.getFromJSONArray(jsonArrayComments);
            post.likeNumber = jsonObject.getInt(Params.LIKE_NUMBER);
            post.commentNumber = jsonObject.getInt(Params.COMMENT_NUMBER);
            post.shareNumber = jsonObject.getInt(Params.SHARE_NUMBER);


            post.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));
        } else if (post.type == Type.Follow) {
            post.friendIdInt=jsonObject.getInt(Params.FRIEND_ID_INT);
            post.friendIdStr=jsonObject.getString(Params.FRIEND_ID_STR);
//            post.userId = jsonObject.getInt(Params.USER_ID_INT);//business owner' user.id
//            post.userName = jsonObject.getString(Params.USER_NAME_STRING);
        } else if (post.type == Type.Review) {
//            String description = jsonObject.getString(Params.POST_DESCRIPTION_STRING);
            post.friendIdInt=jsonObject.getInt(Params.FRIEND_ID_INT);
            post.friendIdStr=jsonObject.getString(Params.FRIEND_ID_STR);
        }
        else if (post.type ==Type.CompleteFriend){
            post.title = jsonObject.getString(Params.POST_TITLE_STRING);
            post.creationDate = setCreationDate(jsonObject);
            post.pictureId = jsonObject.getInt(Params.POST_PICTURE_ID_INT);
            post.description = jsonObject.getString(Params.POST_DESCRIPTION_STRING);
            post.code = jsonObject.getString(Params.POST_CODE_STRING);
            post.price = jsonObject.getString(Params.POST_PRICE_STRING);
            post.isLiked = jsonObject.getBoolean(Params.POST_IS_LIKED);
            post.isReported = jsonObject.getBoolean(Params.POST_IS_REPORTED);
            post.isShared = jsonObject.getBoolean(Params.POST_IS_SHARED);
            String comments = jsonObject.getString(Params.Post_COMMENTS_STRING);
            JSONArray jsonArrayComments = new JSONArray(comments);

            //
            post.hashtagList = Hashtag.getListFromString(jsonObject.getString(Params.HASHTAG_LIST));
            post.lastThreeComments = Comment.getFromJSONArray(jsonArrayComments);
            post.likeNumber = jsonObject.getInt(Params.LIKE_NUMBER);
            post.commentNumber = jsonObject.getInt(Params.COMMENT_NUMBER);
            post.shareNumber = jsonObject.getInt(Params.SHARE_NUMBER);

            post.friendIdInt=jsonObject.getInt(Params.FRIEND_ID_INT);
            post.friendIdStr=jsonObject.getString(Params.FRIEND_ID_STR);
        }
        return post;
    }

    public static String getReviewText(String jsonObjectString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonObjectString);
        return jsonObject.getString(Params.REVIEW);
    }

    public static int getReviewRate(String jsonObjectString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonObjectString);
        return jsonObject.getInt(Params.RATE);
    }

    public boolean isMine(Context context) {
        int ownerId = LoginInfo.getUserId(context);
        if (ownerId == this.userId)
            return true;
        return false;
    }

    public static void goPostPage(Context context, int postId, int businessIdForBusinessType, GetPostType getPostType) {
        Intent intent = new Intent(context, ActivityPost.class);
        intent.putExtra(Params.POST_ID_INT, postId);
        intent.putExtra(Params.BUSINESS_ID_STRING, businessIdForBusinessType);
        intent.putExtra(Params.POST_TYPE, getPostType.name());
        context.startActivity(intent);
    }

    public static void goPostPageFromUserHome(Activity activity, int postId, int businessIdForBusinessType, GetPostType getPostType) {
        Intent intent = new Intent(activity, ActivityPost.class);
        intent.putExtra(Params.POST_ID_INT, postId);
        intent.putExtra(Params.BUSINESS_ID_STRING, businessIdForBusinessType);
        intent.putExtra(Params.POST_TYPE, getPostType.name());
        activity.startActivityForResult(intent, Params.ACTION_ACTIVITY_POST);
    }

    public static int getIndexOfPost(ArrayList<Post> posts, int postId) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).id == postId)
                return i;
        }


        //there is not such a post in the posts
        return -1;
    }


    public static Hashtable<String,Integer> getLastLoadedTimelineItems(ArrayList<Post> loadedItems){
        Hashtable<String,Integer> result=new Hashtable<>();
        for (int i = loadedItems.size()-1; i >=0 ; i++) {
            Post p=loadedItems.get(i);
            if ((!result.containsKey(Params.BUSINESS_POST_FOR_TIMELINE))&&(p.type == Type.CompleteBusiness)){
                result.put(Params.BUSINESS_POST_FOR_TIMELINE,p.id);
            }
            else if ((!result.containsKey(Params.FRIEND_SHARED_POST_FOR_TIMELINE))&&(p.type == Type.CompleteFriend)){
                result.put(Params.FRIEND_SHARED_POST_FOR_TIMELINE,p.id);
            }
            else if ((!result.containsKey(Params.FRIEND_FOLLOW_ANNOUNCE_FOR_TIMELINE))&&(p.type == Type.Follow)){
                result.put(Params.FRIEND_FOLLOW_ANNOUNCE_FOR_TIMELINE,p.id);
            }
            else if ((!result.containsKey(Params.FRIEND_REVIEW_ANNOUNCE_FOR_TIMELINE))&&(p.type == Type.Review)){
                result.put(Params.FRIEND_REVIEW_ANNOUNCE_FOR_TIMELINE,p.id);
            }
            else if (result.size() == 4)
                break;
        }
        if (!result.containsKey(Params.BUSINESS_POST_FOR_TIMELINE))
            result.put(Params.BUSINESS_POST_FOR_TIMELINE,0);

        if (!result.containsKey(Params.FRIEND_SHARED_POST_FOR_TIMELINE))
            result.put(Params.FRIEND_SHARED_POST_FOR_TIMELINE,0);

        if (!result.containsKey(Params.FRIEND_FOLLOW_ANNOUNCE_FOR_TIMELINE))
            result.put(Params.FRIEND_FOLLOW_ANNOUNCE_FOR_TIMELINE,0);

        if (!result.containsKey(Params.FRIEND_REVIEW_ANNOUNCE_FOR_TIMELINE))
            result.put(Params.FRIEND_REVIEW_ANNOUNCE_FOR_TIMELINE,0);

        return result;
    }

}
