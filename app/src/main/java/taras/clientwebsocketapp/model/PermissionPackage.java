package taras.clientwebsocketapp.model;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 13.03.2018.
 */

public class PermissionPackage extends Package{

    @SerializedName("files_name")
    @Expose
    private List<String> filesName;
    @SerializedName("is_allowed")
    @Expose
    private String isAllowed;

    public PermissionPackage() {
        super(Constants.PACKAGE_TYPE_PERMISSION);
        this.setClientIp(AppApplication.deviceIp);
        this.setClientName(PreferenceUtils.getDeviceName());
    }

    public PermissionPackage(List<String> filesName, String isAllowed) {
        super(Constants.PACKAGE_TYPE_PERMISSION);
        this.filesName = filesName;
        this.isAllowed = isAllowed;
    }

    public List<String> getFilesName() {
        return filesName;
    }

    public void setFilesName(List<String> filesName) {
        this.filesName = filesName;
    }

    public String isAllowed() {
        return isAllowed;
    }

    public void setAllowed(String allowed) {
        isAllowed = allowed;
    }


    public String toJson(){
        return GsonUtils.createJsonPermissionPackage(this);
    }

    public static PermissionPackage parse(String json){
        return GsonUtils.parsePermissionPackage(json);
    }
}
