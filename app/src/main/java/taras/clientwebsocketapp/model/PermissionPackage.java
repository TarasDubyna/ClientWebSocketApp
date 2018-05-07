package taras.clientwebsocketapp.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;
import taras.clientwebsocketapp.utils.TimeUtils;

/**
 * Created by Taras on 13.03.2018.
 */

public class PermissionPackage extends Package{

    private static final long PERMISSION_TIMEOUT = 10500;

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
    private long startTimestamp = 0;

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

    public long getStartTimestamp() {
        return startTimestamp;
    }
    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String toJson(){
        return GsonUtils.createJsonPermissionPackageFirst(this);
    }
    public static PermissionPackage parse(String json){
        return GsonUtils.parsePermissionPackage(json);
    }

    public boolean isPermissionTimeout(){
        Log.d("myLogs", "TimeUtils.getCurrentTime(): " + TimeUtils.getCurrentTime() + " ,startTimestamp: " +startTimestamp);
        if (startTimestamp != 0){
            if (TimeUtils.getCurrentTime() - startTimestamp > PERMISSION_TIMEOUT){
                Log.d("myLogs", "PermissionTimeout");
               return true;
            } else return false;
        } else return false;
    }
}
