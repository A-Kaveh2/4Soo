package ir.rasen.charsoo.controller.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;


public class MyNotification {

    private static int notifyId = 1001;
    private static int notifyCustomeViewId = 1002;

    public static void displayNotification(Context context, Intent resultIntent, String contextTitle, String contentText, int icon) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(contextTitle)
                        .setContentText(contentText)
                        .setContentIntent(pendingIntent);

        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, mBuilder.build());
    }

    public static void displayNotificationCustomView(Context context, Intent resultIntent, RemoteViews contentView, int icon) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentIntent(pendingIntent)
                        .setContent(contentView);

        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyCustomeViewId, mBuilder.build());
    }


}
