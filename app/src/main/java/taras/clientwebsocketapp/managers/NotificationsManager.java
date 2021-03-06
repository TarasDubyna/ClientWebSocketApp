package taras.clientwebsocketapp.managers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
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

    public static void createFileGetNotification(int id, String fileName){
        PendingIntent pIntent = PendingIntent.getActivity(AppApplication.appContext, (int) System.currentTimeMillis(), new Intent(), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppApplication.appContext, BackgroundService.class.getSimpleName());
        builder.setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setSmallIcon(R.drawable.ic_phone_network)
                .setContentTitle(AppApplication.appContext.getString(R.string.app_name))
                .setContentText("Download: " + fileName)
                .setProgress(100, 0, true)
                .setContentIntent(pIntent)
                .setAutoCancel(false);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AppApplication.appContext);
        notificationManager.notify(id, builder.build());
    }
    public static void updateFileGetNotification(int id, int progress){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AppApplication.appContext);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppApplication.appContext, BackgroundService.class.getSimpleName());
        builder.setProgress(100, progress, true)
                .setSmallIcon(R.drawable.ic_phone_network);
        notificationManager.notify(id, builder.build());
    }
    public static void finishFileGetNotification(int id, String deviceFromName, String fileName){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AppApplication.appContext);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppApplication.appContext, BackgroundService.class.getSimpleName());
        builder.setAutoCancel(true);
        builder.setContentText("From: " + deviceFromName)
                .setSubText(fileName + " downloaded")
                .setSmallIcon(R.drawable.ic_phone_network)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setProgress(0,0,false);
        notificationManager.notify(id, builder.build());
    }
}
