package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

public class PermissionPackageSecond extends Package{

    @SerializedName("pass")
    @Expose
    private String pass;

    public PermissionPackageSecond() {
        super(Constants.PACKAGE_TYPE_PERMISSION_SECOND_STAGE);
        this.setClientIp(AppApplication.deviceIp);
        this.setClientName(PreferenceUtils.getDeviceName());
    }

    public PermissionPackageSecond(String pass) {
        super(Constants.PACKAGE_TYPE_PERMISSION_SECOND_STAGE);
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String toJson(){
        return GsonUtils.createJsonPermissionPackageSecond(this);
    }

    public static PermissionPackageSecond parse(String json){
        return GsonUtils.parsePermissionPackageSecond(json);
    }
}