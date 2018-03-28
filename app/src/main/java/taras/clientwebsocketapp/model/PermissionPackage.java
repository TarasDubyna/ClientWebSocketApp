package taras.clientwebsocketapp.model;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;

/**
 * Created by Taras on 13.03.2018.
 */

public class PermissionPackage extends Package{

    @SerializedName("filesName")
    @Expose
    private List<String> filesName;
    @SerializedName("clientData")
    @Expose
    private ClientData clientData;
    @SerializedName("serverData")
    @Expose
    private ServerData serverData;
    @Expose
    private String isAllowed;

    public PermissionPackage(List<String> filesName, ClientData clientData, ServerData serverData, String isAllowed) {
        super(Constants.PACKAGE_TYPE_PERMISSION);
        this.filesName = filesName;
        this.clientData = clientData;
        this.serverData = serverData;
        this.isAllowed = isAllowed;
    }

    public void setDescription(int description){
        this.setDescription(description);
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
