package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 08.02.2018.
 */

public class ScannerPackage extends Package {

    public ScannerPackage() {
        super(Constants.PACKAGE_TYPE_SCANNING);
        this.setClientIp(AppApplication.deviceIp);
        this.setClientName(PreferenceUtils.getDeviceName());
    }

    public String toJson(){
        return GsonUtils.createJsonScannerPackage(this);
    }
    public static ScannerPackage parse(String json){
        return GsonUtils.parseScannerPackage(json);
    }
}
