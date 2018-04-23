package taras.clientwebsocketapp.service;

import android.util.Log;

import java.io.IOException;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.managers.NotificationsManager;
import taras.clientwebsocketapp.model.PermissionPackageFirst;
import taras.clientwebsocketapp.network.NetworkConnection;
import taras.clientwebsocketapp.network.RequestServiceInterface;
import taras.clientwebsocketapp.server.Server;
import taras.clientwebsocketapp.utils.EventBusMsg;
import taras.clientwebsocketapp.utils.PreferenceUtils;

public class RequestServiceManager implements Runnable {

    private static final String LOG_TAG = "myLogs";

    private EventBusMsg message;
    private Server server;

    private BackgroundService service;
    private RequestServiceInterface requestServiceInterface;

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

    public void takeRequest(RequestServiceInterface requestServiceInterface) throws IOException {
        this.requestServiceInterface = requestServiceInterface;
        switch (message.getCodeType()){
            case EventBusMsg.PACKAGE_SCANNER:
                scanningNetwork();
                break;
            case EventBusMsg.PACKAGE_PERMISSION_FIRST:
                takePermissionFirstStage();



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
                NotificationsManager.createServerNotification(this.service));
        PreferenceUtils.saveRunningServerState(true);
        server.startServer();
    }
    private void stopServer(){
        server.stopServer();
        NotificationsManager.removeServerNotification(this.service);
        PreferenceUtils.saveRunningServerState(false);
        service.stopForeground(true);
    }


    private void takePermissionFirstStage() throws IOException {
        Log.d(LOG_TAG, "permissionPackage: " + ((PermissionPackageFirst)message.getModel()).toJson());
        NetworkConnection.getConnectionRepository().getPermission(requestServiceInterface, (PermissionPackageFirst) message.getModel());
    }

    private void scanningNetwork() throws IOException {
        NetworkConnection
                .getConnectionRepository()
                .scanNetwork(requestServiceInterface, AppApplication.networkIp);
    }


    @Override
    public void run() {

    }
}
