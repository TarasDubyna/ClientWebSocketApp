package taras.clientwebsocketapp.server;

import android.os.Handler;
import android.util.Log;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.utils.GlobalBus;
import taras.clientwebsocketapp.utils.GsonUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 17.02.2018.
 */

public class ServerResponse {
    private static final String LOG_TAG = "myLogs";


    public ServerResponse() {
    }

    public String createScanningNetworkResponse(ScannerPackage scannerPackage){
        scannerPackage.setServerIp(AppApplication.deviceIp);
        scannerPackage.setServerName(AppApplication.deviceName);
        Log.d(LOG_TAG, "Server stringResponse: " + scannerPackage.toJson());
        return scannerPackage.toJson();
    }

    public String createPermissionResponse(PermissionPackage permissionPackage){
        //заглушка
        permissionPackage.setAllowed("false");
        //-------
        Log.d(LOG_TAG, "Server stringResponse: " + permissionPackage.toJson());
        return permissionPackage.toJson();
    }

}
