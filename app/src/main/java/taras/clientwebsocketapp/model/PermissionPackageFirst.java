package taras.clientwebsocketapp.model;

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

public class PermissionPackageFirst extends Package{

    @SerializedName("files_name")
    @Expose
    private List<String> filesName;

    public PermissionPackageFirst() {
        super(Constants.PACKAGE_TYPE_PERMISSION_FIRST_STAGE);
        this.setClientIp(AppApplication.deviceIp);
        this.setClientName(PreferenceUtils.getDeviceName());
    }

    public PermissionPackageFirst(List<String> filesName) {
        super(Constants.PACKAGE_TYPE_PERMISSION_FIRST_STAGE);
        this.filesName = filesName;
    }

    public List<String> getFilesName() {
        return filesName;
    }
    public void setFilesName(List<String> filesName) {
        this.filesName = filesName;
    }



    public String toJson(){
        return GsonUtils.createJsonPermissionPackageFirst(this);
    }

    public static PermissionPackageFirst parse(String json){
        return GsonUtils.parsePermissionPackageFirst(json);
    }
}
