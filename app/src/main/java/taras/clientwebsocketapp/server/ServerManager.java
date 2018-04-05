package taras.clientwebsocketapp.server;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.internal.android.JsonUtils;
import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.model.Package;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.model.ServerState;
import taras.clientwebsocketapp.model.ServerStatePackage;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GlobalBus;
import taras.clientwebsocketapp.utils.GsonUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 17.02.2018.
 */

public class ServerManager {
    private static final String LOG_TAG = ServerManager.class.getName();


    private String requestJson;

    private String packageType;
    private Package requestPackage;


    public ServerManager() {

    }

    public ServerManager getRequest(String requestJson){
        this.requestJson = requestJson;
        Log.d(LOG_TAG, "requestJson: " + requestJson);
        return this;
    }

    public String returnResponse(){
        packageType = getPackageType();
        requestPackage = getPackageFromJson();
    }


    private String getPackageType() {
        try {
            JSONObject requestJsonObject = new JSONObject(requestJson);
            String packageType = requestJsonObject.getString("type");
            Log.d(LOG_TAG, "packageType: " + packageType);
            return packageType;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Package getPackageFromJson(){
        switch (packageType){
            case Constants.PACKAGE_TYPE_PERMISSION:
                return GsonUtils.parsePermissionPackage(requestJson);
            case Constants.PACKAGE_TYPE_SCANNING:
                return GsonUtils.parseScannerPackage(requestJson);
        }
        return null;
    }

    private String createResponse(){
        switch (packageType){
            case Constants.PACKAGE_TYPE_SCANNING:
                return createScannerPackageResponse((ScannerPackage) requestPackage).toJson();
            case Constants.PACKAGE_TYPE_PERMISSION:
                ServerStatePackage serverStatePackage = new ServerStatePackage(requestPackage);
                GlobalBus.getBus().post(serverStatePackage);
                return createPermissionPackageResponse((PermissionPackage) requestPackage).toJson();
        }
        return null;
    }

    private ScannerPackage createScannerPackageResponse(ScannerPackage scannerPackage){
        scannerPackage.setServerIp(AppApplication.deviceIp);
        scannerPackage.setServerName(AppApplication.deviceName);
        return scannerPackage;
    }

    private ServerStatePackage createPermissionPackageResponse(PermissionPackage pack){
        ServerStatePackage serverState = new ServerStatePackage(pack);
        GlobalBus.getBus().post(serverState);
        return serverState;
    }



    /*
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
    */

}
