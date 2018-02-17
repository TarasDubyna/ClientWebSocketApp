package taras.clientwebsocketapp.server;

import android.util.Log;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.model.ScannerPackage;
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
}
