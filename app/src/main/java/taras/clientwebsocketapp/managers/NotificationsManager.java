package taras.clientwebsocketapp.managers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.utils.PreferenceUtils;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Taras on 06.03.2018.
 */

public class NotificationsManager {

    public static int ID_FOREGROUND_SERVICE = 101;

    private static NotificationsManager notificationManager;

    public static Notification createServerNotification(Context context){
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), new Intent(), 0);
        return new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_phone_network)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Device is visible in the network")
                .setContentIntent(pIntent)
                .setAutoCancel(false)
                .build();
    }
    public static void removeServerNotification(Context context){
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


}
