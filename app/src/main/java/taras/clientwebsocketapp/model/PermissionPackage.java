package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Taras on 13.03.2018.
 */

public class PermissionPackage {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("filesName")
    @Expose
    private List<String> filesName;

    @SerializedName("description")
    @Expose
    private int description;
    @SerializedName("clientDeviceName")
    @Expose
    private String clientDeviceName;
    @SerializedName("clientDeviceIp")
    @Expose
    private String clientDeviceIp;
    @SerializedName("serverDeviceName")
    @Expose
    private String serverDeviceName;
    @SerializedName("serverDeviceIp")
    @Expose
    private String serverDeviceIp;
    @SerializedName("isAllowed")
    @Expose
    private boolean isAllowed;

    public PermissionPackage(){
    }

    public PermissionPackage(int description, String type, List<String> filesName, String clientDeviceName, String clientDeviceIp, String serverDeviceIp, boolean isAllowed) {
        this.description = description;
        this.type = type;
        this.filesName = filesName;
        this.clientDeviceName = clientDeviceName;
        this.clientDeviceIp = clientDeviceIp;
        this.serverDeviceIp = serverDeviceIp;
        this.isAllowed = isAllowed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getFilesName() {
        return filesName;
    }

    public void setFilesName(List<String> filesName) {
        this.filesName = filesName;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public String getClientDeviceName() {
        return clientDeviceName;
    }

    public void setClientDeviceName(String clientDeviceName) {
        this.clientDeviceName = clientDeviceName;
    }

    public String getClientDeviceIp() {
        return clientDeviceIp;
    }

    public void setClientDeviceIp(String clientDeviceIp) {
        this.clientDeviceIp = clientDeviceIp;
    }

    public String getServerDeviceName() {
        return serverDeviceName;
    }

    public void setServerDeviceName(String serverDeviceName) {
        this.serverDeviceName = serverDeviceName;
    }

    public String getServerDeviceIp() {
        return serverDeviceIp;
    }

    public void setServerDeviceIp(String serverDeviceIp) {
        this.serverDeviceIp = serverDeviceIp;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }
}
