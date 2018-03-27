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
        scannerPackage.setServerData(new ScannerPackage.ServerData(AppApplication.deviceIp, PreferenceUtils.getDeviceName()));
        String scanningNetworkResponse = GsonUtils.createJsonScannerPackage(scannerPackage);
        Log.d(LOG_TAG, "Server stringResponse: " + scanningNetworkResponse);
        return scanningNetworkResponse;
    }

    public String createPermissionResponse(PermissionPackage permissionPackage){
        //заглушка
        permissionPackage.setAllowed("false");

        //-------
        String permissionResponse = GsonUtils.createPermissionPackage(permissionPackage);
        Log.d(LOG_TAG, "Server stringResponse: " + permissionResponse);
        return permissionResponse;
    }

}
