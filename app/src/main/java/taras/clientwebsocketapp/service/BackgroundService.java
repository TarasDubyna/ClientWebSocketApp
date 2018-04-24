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
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.network.NetworkConnection;
import taras.clientwebsocketapp.network.RequestServiceInterface;
import taras.clientwebsocketapp.server.Server;
import taras.clientwebsocketapp.utils.EventBusMsg;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 04.02.2018.
 */

public class BackgroundService extends Service {

    private static final String LOG_TAG = "myLogs";
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
        EventBus.getDefault().register(this);
        server = Server.getInstance(mainHandler);

        EventBusMsg<Boolean> message = new EventBusMsg<Boolean>(EventBusMsg.TO_APP, EventBusMsg.CHECK_IS_SERVER_WORK, server.isServerIsRun());
        EventBus.getDefault().postSticky(message);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        server.stopServer();
        PreferenceUtils.saveRunningServerState(false);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDataFromApp(EventBusMsg<Object> ebMessage) {
        if (ebMessage.getCodeDirection() == EventBusMsg.TO_SERVICE){
            RequestServiceManager requestServiceManager = new RequestServiceManager(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        requestServiceManager
                                .getMessage(ebMessage)
                                .setServer(server)
                                .takeRequest(requestServiceCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "RequestServiceManager, error: " + e.getMessage());
                    }
                }
            }).start();

        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
    }

    private RequestServiceInterface requestServiceCallback = new RequestServiceInterface() {
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
            Log.d(LOG_TAG, "Error: " + throwable.getMessage());
            throwable.printStackTrace();
        }

        @Override
        public void errorResponse(Throwable throwable) {

        }

        @Override
        public void successfulGetPermission(PermissionPackage permissionPackage) {
            Log.d(LOG_TAG, "successfulGetPermission, permissionPackage.getIsAllowed(): " + permissionPackage.getIsAllowed());
            if (permissionPackage.getIsAllowed() == null){
                try {
                    Thread.sleep(1000);
                    Log.d(LOG_TAG, "Did not got permission");
                    NetworkConnection.getConnectionRepository().getPermission(requestServiceCallback, permissionPackage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(LOG_TAG, "Got permission");
                EventBusMsg<PermissionPackage> message =
                        new EventBusMsg<PermissionPackage>(EventBusMsg.TO_APP, EventBusMsg.PACKAGE_PERMISSION, permissionPackage);
                EventBus.getDefault().postSticky(message);
            }
        }

        @Override
        public void errorGetPermission(PermissionPackage permissionPackage) {

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
