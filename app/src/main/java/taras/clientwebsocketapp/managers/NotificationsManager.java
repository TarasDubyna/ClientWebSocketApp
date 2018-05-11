package taras.clientwebsocketapp.managers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.service.BackgroundService;
import taras.clientwebsocketapp.utils.PreferenceUtils;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Taras on 06.03.2018.
 */

public class NotificationsManager {

    public static int ID_FOREGROUND_SERVICE = 101;

    private static final int ID_CHECK_PERMISSION = 1;

    private static NotificationsManager notificationManager;

    public static Notification createServerStatusNotification(Context context){
        PendingIntent pIntent = PendingIntent.getActivity(AppApplication.appContext, (int) System.currentTimeMillis(), new Intent(), 0);
        return new Notification.Builder(AppApplication.appContext)
                .setSmallIcon(R.drawable.ic_phone_network)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Device is visible in the network")
                .setContentIntent(pIntent)
                .setAutoCancel(false)
                .build();
    }
    public static void removeServerStatusNotification(){
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) AppApplication.appContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


    public static void createGetPermissionNotification(){
        PendingIntent pIntent = PendingIntent.getActivity(AppApplication.appContext, (int) System.currentTimeMillis(), new Intent(AppApplication.appContext, MainActivity.class), Intent.FILL_IN_ACTION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppApplication.appContext, BackgroundService.class.getSimpleName());
        builder.setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setSmallIcon(R.drawable.ic_phone_network)
                .setContentTitle(AppApplication.appContext.getString(R.string.app_name))
                .setContentText("Check permission")
                .setContentIntent(pIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AppApplication.appContext);
        notificationManager.notify(ID_CHECK_PERMISSION, builder.build());
    }






/*
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
mBuilder.setContentTitle("Picture Download")
        .setContentText("Download in progress")
        .setSmallIcon(R.drawable.ic_notification)
        .setPriority(NotificationCompat.PRIORITY_LOW);

    // Issue the initial notification with zero progress
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
notificationManager.notify(notificationId, mBuilder.build());

// Do the job here that tracks the progress.
// Usually, this should be in a worker thread
// To show progress, update PROGRESS_CURRENT and update the notification with:
// mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
// notificationManager.notify(notificationId, mBuilder.build());

// When done, update the notification one more time to remove the progress bar
mBuilder.setContentText("Download complete")
        .setProgress(0,0,false);
notificationManager.notify(notificationId, mBuilder.build());*/
















}
