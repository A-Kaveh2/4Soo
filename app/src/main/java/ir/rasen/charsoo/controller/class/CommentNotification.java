package ir.rasen.charsoo.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.HashSet;
import java.util.Set;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;


/**
 * Created by android on 12/17/2014.
 */
public class CommentNotification {

    public int id;
    public int postID;
    public int userID;
    public String userName;

    public String postPicture;
    public String userPicture;
    public String text;
    public int intervalTime;//in millisecond.

    public CommentNotification() {

    }

    public CommentNotification(int id, int postID, String userName, String userPicture, String postPicture, String text, int intervalTime) {
        this.id = id;
        this.postID = postID;
        this.userPicture = userPicture;
        this.postPicture = postPicture;
        this.text = text;
        this.intervalTime = intervalTime;
        this.userName = userName;
    }

    public RemoteViews getCommentNotificationContentView(Context context) {
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.item_custom_notification);
        contentView.setImageViewBitmap(R.id.img_notification_user, Image_M.getBitmapFromString(userPicture));
        contentView.setImageViewBitmap(R.id.img_notification_post, Image_M.getBitmapFromString(postPicture));
        contentView.setTextViewText(R.id.txt_notification, text);

        return contentView;
    }

    public static boolean isDisplayed(Context context, int commendId) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        /*String lastCommentId = preferences.getString(Params.COMMENT_ID, "");
        if (lastCommentId != 0 && lastCommentId == commendId)
            return true;*/
         /*  String userId = lastCommentId.substring(0, lastCommentId.indexOf(":"));
        String commendIdStr = lastCommentId.substring(lastCommentId.indexOf(":") + 1, lastCommentId.length());
        return false;*/

        Set<String> set;
        set = preferences.getStringSet(Params.USERS_SEEN_NOTIFICATIONS, null);
        if (set == null)
            return  false;
        if(set.contains(LoginInfo.getUserId(context)+":"+commendId))
            return true;
        else
        return false;
    }

    public static void insertLastCommentId(Context context, int lastCommentId) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
       /* edit.putString(Params.COMMENT_ID, LoginInfo.getUserId(context) + ":" + lastCommentId);
        edit.commit();*/

        Set<String> set;
        set = preferences.getStringSet(Params.USERS_SEEN_NOTIFICATIONS, null);
        if (set == null)
            set = new HashSet<>();

        set.add(LoginInfo.getUserId(context) + ":" + lastCommentId);
        edit.putStringSet(Params.USERS_SEEN_NOTIFICATIONS, set);
        edit.commit();
    }
}
