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

public class PermissionPackage extends Package{

    @SerializedName("files_name")
    @Expose
    private List<String> filesName;
    @SerializedName("is_allowed")
    @Expose
    private String isAllowed;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("pass")
    @Expose
    private String pass;
    @SerializedName("start_timestamp")
    @Expose
    private String startTimestamp;

    public PermissionPackage() {
        super(Constants.PACKAGE_TYPE_PERMISSION);
        this.setClientIp(AppApplication.deviceIp);
        this.setClientName(PreferenceUtils.getDeviceName());
    }

    public PermissionPackage(List<String> filesName) {
        super(Constants.PACKAGE_TYPE_PERMISSION);
        this.filesName = filesName;
    }

    public List<String> getFilesName() {
        return filesName;
    }
    public void setFilesName(List<String> filesName) {
        this.filesName = filesName;
    }

    public String getIsAllowed() {
        return isAllowed;
    }
    public void setIsAllowed(String isAllowed) {
        this.isAllowed = isAllowed;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }
    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String toJson(){
        return GsonUtils.createJsonPermissionPackageFirst(this);
    }
    public static PermissionPackage parse(String json){
        return GsonUtils.parsePermissionPackageFirst(json);
    }
}
