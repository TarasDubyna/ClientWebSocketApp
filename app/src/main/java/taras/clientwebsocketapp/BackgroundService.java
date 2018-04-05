package taras.clientwebsocketapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.managers.NotificationsManager;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.model.ServerStatePackage;
import taras.clientwebsocketapp.network.NetworkConnection;
import taras.clientwebsocketapp.network.ScanningInterface;
import taras.clientwebsocketapp.server.Server;
import taras.clientwebsocketapp.utils.BusUtil;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.EventBusMsg;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 04.02.2018.
 */

public class BackgroundService extends Service implements ScanningInterface {

    private static final String LOG_TAG = "myLogs";

    public static final String MY_ACTION = "MY_ACTION";

    public static final String TYPE = "TYPE";
    public static final String SCAN_NETWORK = "SCAN_NETWORK";
    public static final String DEVICE_IP = "DEVICE_IP";

    public static final String SEND_MESSAGE = "SEND_MESSAGE";
    public static final String SEND_DATA = "SEND_DATA";

    private Server server;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        server = Server.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceUtils.saveServerState(false);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }



    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDataFromApp(EventBusMsg<Object> ebMessage) {
        if (ebMessage.getCodeDirection() == EventBusMsg.TO_APP){
            switch (ebMessage.getCodeType()){
                case EventBusMsg.PACKAGE_SCANNER:
                    try {
                        NetworkConnection
                                .getConnectionRepository()
                                .scanNetwork(this, AppApplication.networkIp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case EventBusMsg.PACKAGE_PERMISSION:
                    Log.d(LOG_TAG, "permissionPackage: " + ((PermissionPackage)ebMessage.getModel()).toJson());
                    try {
                        NetworkConnection.getConnectionRepository().getPermission(this, (PermissionPackage) ebMessage.getModel());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                case EventBusMsg.SERVER_START:
                startForeground(NotificationsManager.ID_FOREGROUND_SERVICE,
                            NotificationsManager.createServerNotification(this));
                server.startServer();
                case EventBusMsg.SERVER_STOP:
                    server.stopServer();
                    NotificationsManager.removeServerNotification(this);
                    stopForeground(true);
                    break;

                case EventBusMsg.PACKAGE_SERVER_STATE:
                    server.stopServer();
                    NotificationsManager.removeServerNotification(this);
                    stopForeground(true);
                    break;
            }
        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
    }

    @Override
    public void errorResponse(Throwable throwable) {

    }
    @Override
    public void successfulScanningResponse(ScannerPackage scannerPackage) {
        Log.d(LOG_TAG, "successfulResponse: " + scannerPackage);
        if (applicationInForeground()){
            if (!scannerPackage.getServerIp().equals(AppApplication.deviceIp)){
                Log.d(LOG_TAG, "successfulScanningResponse, send data to activity");
                EventBusMsg<ScannerPackage> message =
                        new EventBusMsg<ScannerPackage>(EventBusMsg.TO_APP, EventBusMsg.PACKAGE_SCANNER, scannerPackage);
                EventBus.getDefault().post(message);
            }
        }
    }

    // get permission
    @Override
    public void successfulGetPermission(PermissionPackage permissionPackage) {

    }

    @Override
    public void errorGetPermission(PermissionPackage permissionPackage) {

    }
    //-----------------------


    private boolean applicationInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
        boolean isActivityFound = false;

        if (services.get(0).processName
                .equalsIgnoreCase(getPackageName())) {
            isActivityFound = true;
        }

        return isActivityFound;
    }


}
