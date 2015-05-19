package ir.rasen.charsoo.controller.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ir.rasen.charsoo.view.activity.ActivityCommentsNotifications;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.CommentNotification;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.model.comment.GetLastCommentNotification;


/**
 * Created by android on 12/29/2014.
 */
public class AlarmReciever extends BroadcastReceiver implements IWebserviceResponse {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        new GetLastCommentNotification(context, LoginInfo.getUserId(context), AlarmReciever.this).execute();
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof CommentNotification) {
            CommentNotification commentNotification = (CommentNotification) result;



            if (CommentNotification.isDisplayed(context, commentNotification.id))
                return;
            else{
                Toast.makeText(context,CommentNotification.shareStatus(context, commentNotification.id),Toast.LENGTH_LONG).show();
            }


            //save comment.id in sharePreferences storage to check isDisplayed before
            CommentNotification.insertLastCommentId(context, commentNotification.id);


            Toast.makeText(context,"After commit:"+CommentNotification.shareStatus(context, commentNotification.id),Toast.LENGTH_LONG).show();

            //TODO check if activity is on top
            Intent intent = new Intent(context, ActivityCommentsNotifications.class);
            intent.putExtra(Params.NOTIFICATION, true);
            MyNotification.displayNotificationCustomView(context, intent, commentNotification.getCommentNotificationContentView(context), R.drawable.ic_launcher);
        }
    }

    @Override
    public void getError(Integer errorCode) {

    }


}
