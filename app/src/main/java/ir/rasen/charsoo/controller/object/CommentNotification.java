package ir.rasen.charsoo.controller.object;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;


/**
 * Created by android on 12/17/2014.
 */
public class CommentNotification {

    public int id;
    public int postID;
    public int userID;
    public String userName;//who wrote the comment
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

    private static String getVariableName(Context context){
        String v = Params.USER_ID_INT +":"+String.valueOf(LoginInfo.getUserId(context));;
        return v;
    }
    public static boolean isDisplayed(Context context, int commentId) {
        SharedPreferences settings = context.getSharedPreferences(Params.USERS_SEEN_NOTIFICATIONS_PREFERENCE_NAME, 0);
        int userLastSeenNotificationId = settings.getInt(getVariableName(context),0);

        if(userLastSeenNotificationId == 0 || userLastSeenNotificationId < commentId) {
            //if the user didn't see any notification before or the user didn't see this notification before
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(getVariableName(context),commentId);
            editor.commit();
            return false;
        }
        else
            return true;
    }

}
