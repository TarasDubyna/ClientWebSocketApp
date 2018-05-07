package taras.clientwebsocketapp.server;

import android.os.Handler;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.managers.PermissionManager;
import taras.clientwebsocketapp.model.Package;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.ConstatsLogTag;
import taras.clientwebsocketapp.utils.EventBusMsg;
import taras.clientwebsocketapp.utils.GsonUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 17.02.2018.
 */

public class ServerManager {
    private Handler mainHandler;

    private String requestJson;

    private String packageType;
    private Package requestPackage;


    public ServerManager(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public ServerManager getRequest(String requestJson){
        this.requestJson = requestJson;
        Log.d(ConstatsLogTag.Socket, "server requestJson: " + requestJson);
        return this;
    }

    public String returnResponse(){
        packageType = getPackageType();
        requestPackage = getPackageFromJson();
        String response = createResponse();
        Log.d(ConstatsLogTag.Socket, "server returnResponse: " + response);
        return response;
    }


    private String getPackageType() {
        try {
            JSONObject requestJsonObject = new JSONObject(requestJson);
            String packageType = requestJsonObject.getString("type");
            return packageType;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Package getPackageFromJson(){
        Log.d(ConstatsLogTag.Server, "server listen: " + packageType);
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
                return createPackagePermissionResponse((PermissionPackage) requestPackage).toJson();

        }
        return null;
    }

    private ScannerPackage createScannerPackageResponse(ScannerPackage scannerPackage){
        scannerPackage.setServerIp(AppApplication.deviceIp);
        scannerPackage.setServerName(PreferenceUtils.getDeviceName());
        return scannerPackage;
    }
    private PermissionPackage createPackagePermissionResponse(PermissionPackage pack){
        if (!PermissionManager.getPermissionManager().isListPermissionConsist(PermissionManager.SERVER, pack)){
            PermissionManager.getPermissionManager().addToPermissionManager(PermissionManager.SERVER, pack);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    EventBusMsg<PermissionPackage> message =
                            new EventBusMsg<PermissionPackage>(EventBusMsg.TO_APP,
                                    EventBusMsg.PACKAGE_PERMISSION, pack);
                    EventBus.getDefault().postSticky(message);
                }
            });
            return pack;
        } else {
            return PermissionManager.getPermissionManager().getPermissionPackageFromManager(PermissionManager.SERVER, pack);
        }
    }

}
