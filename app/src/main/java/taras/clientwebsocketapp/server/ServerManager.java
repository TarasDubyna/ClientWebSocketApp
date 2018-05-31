package taras.clientwebsocketapp.server;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.managers.NotificationsManager;
import taras.clientwebsocketapp.managers.PermissionManager;
import taras.clientwebsocketapp.managers.file_getter_manager.FileGetterManager;
import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.FileSendStatePackage;
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

public class  ServerManager {
    private Handler mainHandler;
    private Context context;

    private String requestJson;

    private String packageType;
    private Package requestPackage;


    public ServerManager(Handler mainHandler, Context context) {
        this.mainHandler = mainHandler;
        this.context = context;
    }

    public ServerManager getRequest(String requestJson){
        this.requestJson = requestJson;
        Log.d(ConstatsLogTag.Socket, "server requestJson: " + requestJson);
        return this;
    }

    public String returnResponse(String requestJson){
        Log.d("testtesttest", "server requestJson: " + requestJson);
        String packageType = getPackageType(requestJson);
        String response = createResponse(packageType, getPackageFromJson(packageType, requestJson));
        Log.d(ConstatsLogTag.Socket, "server returnResponse: " + response);
        return response;
    }


    private String getPackageType(String requestJson) {
        try {
            JSONObject requestJsonObject = new JSONObject(requestJson);
            String packageType = requestJsonObject.getString("type");
            Log.d("ServerManager", "packageType: " + packageType);
            return packageType;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Package getPackageFromJson(String packageType, String requestJson){
        Log.d(ConstatsLogTag.Server, "Parse request: " + packageType);
        try {
            switch (packageType){
                case Constants.PACKAGE_TYPE_PERMISSION:
                    return PermissionPackage.parse(requestJson);
                case Constants.PACKAGE_TYPE_SCANNING:
                    return ScannerPackage.parse(requestJson);
                case Constants.PACKAGE_FILE_SEND:
                    return FileSendPackage.parse(requestJson);
            }
        } catch (Exception ex){
            Log.d("testtesttest", "Exception: " + ex.getMessage());
        }

        return null;
    }

    private String createResponse(String packageType, Package requestPackage){
        switch (packageType){
            case Constants.PACKAGE_TYPE_SCANNING:
                packageType = null;
                return createScannerPackageResponse((ScannerPackage) requestPackage).toJson();
            case Constants.PACKAGE_TYPE_PERMISSION:
                packageType = null;
                return createPackagePermissionResponse((PermissionPackage) requestPackage).toJson();
            case Constants.PACKAGE_FILE_SEND:
                packageType = null;
                return createFileSendStatePackageResponse((FileSendPackage) requestPackage).toJson();

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
    private FileSendStatePackage createFileSendStatePackageResponse(FileSendPackage fileSendPackage){
        FileGetterManager.getFileGetterManager().addGettedFileSandPackage(fileSendPackage);
        return new FileSendStatePackage(fileSendPackage);
    }

}
