package taras.clientwebsocketapp.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.IOException;
import java.util.List;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.managers.NotificationsManager;
import taras.clientwebsocketapp.managers.PermissionManager;
import taras.clientwebsocketapp.managers.file_sender_manager.FileSenderManager;
import taras.clientwebsocketapp.model.FileSendStatePackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.network.NetworkConnection;
import taras.clientwebsocketapp.network.callbacks.FileSenderRequestCallback;
import taras.clientwebsocketapp.network.callbacks.GetPermissionCallback;
import taras.clientwebsocketapp.network.callbacks.ScanningNetworkCallback;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.server.Server;
import taras.clientwebsocketapp.utils.ConstatsLogTag;
import taras.clientwebsocketapp.utils.EventBusMsg;

/**
 * Created by Taras on 04.02.2018.
 */

public class BackgroundService extends Service {

    private static final String LOG_TAG = BackgroundService.class.getSimpleName();
    public static final String SERVICE_ACTION = "SERVICE_ACTION";

    private Server server;

    final Handler mainHandler = new Handler();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service onCreate");
        EventBus.getDefault().register(this);
        server = Server.getInstance(mainHandler, getApplicationContext());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        server.stopServer();
        EventBus.getDefault().unregister(this);
        NotificationsManager.removeServerStatusNotification();
        Log.d(LOG_TAG, "Service onDestroy");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDataFromApp(EventBusMsg<Object> ebMessage) {
        if (ebMessage.getCodeDirection() == EventBusMsg.TO_SERVICE){
            if (ebMessage.getCodeType() == EventBusMsg.CHECK_IS_SERVER_WORK){
                Log.d(LOG_TAG, "CHECK_IS_SERVER_WORK");
                EventBusMsg<Boolean> message = new EventBusMsg<Boolean>(EventBusMsg.TO_APP, EventBusMsg.CHECK_IS_SERVER_WORK, server.isServerIsRun());
                EventBus.getDefault().postSticky(message);
            } else {
                RequestServiceManager requestServiceManager = new RequestServiceManager(this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            requestServiceManager
                                    .getMessage(ebMessage)
                                    .setServer(server)
                                    .setScanningCallback(scanningNetworkCallback)
                                    .setGetPermissionCallback(getPermissionCallback)
                                    .setFileSenderCallback(fileSenderRequestCallback)
                                    .takeRequest();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "RequestServiceManager, error: " + e.getMessage());
                        }
                    }
                }).start();
            }
        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
    }

    ScanningNetworkCallback scanningNetworkCallback = new ScanningNetworkCallback() {
        @Override
        public void successfulScanningResponse(ScannerPackage scannerPackage) {
            Log.d(LOG_TAG, "successfulResponse: " + scannerPackage);
            if (applicationInForeground()){
                if (!scannerPackage.getServerIp().equals(AppApplication.deviceIp)){
                    Log.d(LOG_TAG, "successfulScanningResponse, send data to activity");
                    EventBusMsg<ScannerPackage> message =
                            new EventBusMsg<ScannerPackage>(EventBusMsg.TO_APP, EventBusMsg.PACKAGE_SCANNER, scannerPackage);
                    EventBus.getDefault().postSticky(message);
                }
            }
        }

        @Override
        public void scanningNetworkEnd() {
            Log.d(LOG_TAG, "scanningNetworkEnd");
            EventBusMsg<String> message =
                    new EventBusMsg<String>(EventBusMsg.TO_APP, EventBusMsg.SCANNING_NETWORK_END, null);
            EventBus.getDefault().postSticky(message);
        }

        @Override
        public void errorScanning(Throwable throwable) {
            Log.d(ConstatsLogTag.Scanning, "Error: " + throwable.getMessage());
            throwable.printStackTrace();
        }
    };
    GetPermissionCallback getPermissionCallback = new GetPermissionCallback() {
        @Override
        public void successfulGetPermission(PermissionPackage permissionPackage) {
            Log.d(LOG_TAG, "Get permission, is allowed: " + permissionPackage.getIsAllowed());
            Log.d(LOG_TAG, "successfulGetPermission, permissionPackage.getIsAllowed(): " + permissionPackage.getIsAllowed());
            if (permissionPackage.isPermissionTimeout()){
                Log.d(LOG_TAG, "successful permission timeout");
                PermissionManager.getPermissionManager().setAcceptPermission(PermissionManager.CLIENT, permissionPackage, false);
                EventBusMsg<PermissionPackage> message =
                        new EventBusMsg<PermissionPackage>(EventBusMsg.TO_APP, EventBusMsg.PACKAGE_PERMISSION, permissionPackage);
                EventBus.getDefault().postSticky(message);
            } else {
                if (permissionPackage.getIsAllowed() == null){
                    try {
                        Thread.sleep(1000);
                        Log.d(ConstatsLogTag.CheckPermission, "Did not got permission");
                        NetworkConnection.getConnectionRepository().getPermission(getPermissionCallback, permissionPackage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (permissionPackage.getIsAllowed().equals("true")){
                    Log.d(ConstatsLogTag.CheckPermission, "successful permission for client");
                    PermissionManager.getPermissionManager().setAcceptPermission(PermissionManager.CLIENT, permissionPackage, true);
                    //todo successful permission, start send file

                    FileSenderManager.getFileSenderManager().addFileToSend(permissionPackage);
                    EventBusMsg<PermissionPackage> message =
                            new EventBusMsg<PermissionPackage>(EventBusMsg.TO_APP, EventBusMsg.PACKAGE_PERMISSION, permissionPackage);
                    EventBus.getDefault().postSticky(message);
                }

                if (permissionPackage.getIsAllowed().equals("false")){
                    //todo not successful permission
                    Log.d(ConstatsLogTag.CheckPermission, "not successful permission for client");
                    PermissionManager.getPermissionManager().setAcceptPermission(PermissionManager.CLIENT, permissionPackage, false);
                    EventBusMsg<PermissionPackage> message =
                            new EventBusMsg<PermissionPackage>(EventBusMsg.TO_APP, EventBusMsg.PACKAGE_PERMISSION, permissionPackage);
                    EventBus.getDefault().postSticky(message);
                }
            }
        }

        @Override
        public void errorGetPermissionResponse(Throwable throwable) {

        }
    };
    FileSenderRequestCallback fileSenderRequestCallback = new FileSenderRequestCallback() {
        @Override
        public void getFileSendResponse(FileSendStatePackage fileSendStatePackage) {

        }

        @Override
        public void errorRequest(FileSendStatePackage fileSendStatePackage, Throwable throwable) {

        }
    };

    public boolean applicationInForeground() {
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
