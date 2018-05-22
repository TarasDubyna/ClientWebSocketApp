package taras.clientwebsocketapp.service;

import android.util.Log;

import java.io.IOException;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.managers.NotificationsManager;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.network.NetworkConnection;
import taras.clientwebsocketapp.network.callbacks.FileSenderRequestCallback;
import taras.clientwebsocketapp.network.callbacks.GetPermissionCallback;
import taras.clientwebsocketapp.network.callbacks.ScanningNetworkCallback;
import taras.clientwebsocketapp.server.Server;
import taras.clientwebsocketapp.utils.EventBusMsg;
import taras.clientwebsocketapp.utils.TimeUtils;

public class RequestServiceManager{

    private static final String LOG_TAG = "myLogs";

    private EventBusMsg message;
    private Server server;

    private BackgroundService service;

    private ScanningNetworkCallback scanningNetworkCallback = null;
    private GetPermissionCallback getPermissionCallback = null;
    private FileSenderRequestCallback fileSenderRequestCallback = null;

    public RequestServiceManager(BackgroundService service) {
        this.service = service;
    }

    public RequestServiceManager getMessage(EventBusMsg message){
        this.message = message;
        return this;
    }

    public RequestServiceManager setServer(Server server){
        this.server = server;
        return this;
    }

    public RequestServiceManager setScanningCallback(ScanningNetworkCallback callback){
        if (this.scanningNetworkCallback == null){
            this.scanningNetworkCallback = callback;
        }
        return this;
    }
    public RequestServiceManager setGetPermissionCallback(GetPermissionCallback callback){
        if (this.getPermissionCallback == null){
            this.getPermissionCallback = callback;
        }
        return this;
    }
    public RequestServiceManager setFileSenderCallback(FileSenderRequestCallback callback){
        if (this.fileSenderRequestCallback == null){
            this.fileSenderRequestCallback = callback;
        }
        return this;
    }

    public void takeRequest() throws IOException {
        switch (message.getCodeType()){
            case EventBusMsg.PACKAGE_SCANNER:
                scanningNetwork();
                break;
            case EventBusMsg.PACKAGE_PERMISSION:
                takePermission();
                break;
            case EventBusMsg.SERVER_START:
                startServer();
                break;
            case EventBusMsg.SERVER_STOP:
                stopServer();
                break;
            case EventBusMsg.PACKAGE_SERVER_STATE:
                break;
        }
    }

    private void startServer(){
        service.startForeground(NotificationsManager.ID_FOREGROUND_SERVICE,
                NotificationsManager.createServerStatusNotification(this.service));
        server.startServer();
    }
    private void stopServer(){
        server.stopServer();
        NotificationsManager.removeServerStatusNotification();
        service.stopForeground(true);
    }


    private void takePermission() throws IOException {
        Log.d(LOG_TAG, "permissionPackage: " + ((PermissionPackage)message.getModel()).toJson());
        PermissionPackage permissionPackage = (PermissionPackage) message.getModel();
        if (permissionPackage.getStartTimestamp() == 0){
            permissionPackage.setStartTimestamp(TimeUtils.getCurrentTime());
        }
        NetworkConnection.getConnectionRepository().getPermission(getPermissionCallback, permissionPackage);
    }

    private void scanningNetwork() throws IOException {
        NetworkConnection
                .getConnectionRepository()
                .scanNetwork(scanningNetworkCallback, AppApplication.networkIp);
    }
}
