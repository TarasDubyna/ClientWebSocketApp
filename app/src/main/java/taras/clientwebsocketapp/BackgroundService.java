package taras.clientwebsocketapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.managers.NotificationsManager;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.network.NetworkConnection;
import taras.clientwebsocketapp.network.ScanningInterface;
import taras.clientwebsocketapp.server.Server;
import taras.clientwebsocketapp.utils.BusUtil;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GlobalBus;

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

    private String type;
    private String serverIp;
    private String messageText;

    private Server server = Server.getInstance();


    public class LocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Subscribe
    public void getMessage(String messageText){
        switch (messageText){
            case Constants.START_SCANNING:
                try {
                    NetworkConnection.getConnectionRepository()
                            .scanNetwork(this, AppApplication.networkIp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.SERVER_START:
                startForeground(NotificationsManager.ID_FOREGROUND_SERVICE,
                        NotificationsManager.createServerNotification(this));
                server.startServer();
                break;
            case Constants.SERVER_STOP:
                server.stopServer();
                NotificationsManager.removeServerNotification(this);
                stopForeground(true);
                break;

        }
    }

    @Subscribe
    public void getFilesToSend(ArrayList<File> filesList){
        Log.d(LOG_TAG, "service, getFilesToSend, filesList.size: " + filesList.size());
    }

    @Subscribe
    public void getPermissionSend(PermissionPackage permissionPackage){
        Log.d(LOG_TAG, "service, getPermissionSend");
        try {
            NetworkConnection.getConnectionRepository().getPermission(this, permissionPackage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void checkPermissionToSend(PermissionPackage permission){
        if (permission.getDescription() == GlobalBus.TO_SERVICE){
            try {
                NetworkConnection.getConnectionRepository().scanNetwork(this, AppApplication.networkIp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void successfulResponse(String scannerPackage) {
        Log.d(LOG_TAG, "successfulResponse: " + scannerPackage);
        GlobalBus.getBus().post(scannerPackage);
    }
    @Override
    public void errorResponse(Throwable throwable) {

    }
    @Override
    public void successfulScanningResponse(ScannerPackage scannerPackage) {
        Log.d(LOG_TAG, "successfulScanningResponse: " + scannerPackage.toString());
        if (applicationInForeground()){
            if (!scannerPackage.getServerData().getServerIp().equals(AppApplication.deviceIp)){
                Log.d(LOG_TAG, "successfulScanningResponse, send data to activity");
                BusUtil.postOnMain(GlobalBus.getBus(), scannerPackage);
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
