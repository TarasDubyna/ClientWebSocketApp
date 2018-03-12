package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Taras on 13.03.2018.
 */

public class GetPermissionPackage {

    @SerializedName("description")
    @Expose
    private int description;
    @SerializedName("clientDeviceName")
    @Expose
    private String clientDeviceName;
    @SerializedName("serverDeviceName")
    @Expose
    private String serverDeviceName;
    @SerializedName("serverDeviceIp")
    @Expose
    private String serverDeviceIp;
    @SerializedName("isAllowed")
    @Expose
    private boolean isAllowed;

    public GetPermissionPackage(int description, String clientDeviceName, String serverDeviceName, String serverDeviceIp, boolean isAllowed) {
        this.description = description;
        this.clientDeviceName = clientDeviceName;
        this.serverDeviceName = serverDeviceName;
        this.serverDeviceIp = serverDeviceIp;
        this.isAllowed = isAllowed;
    }

    public String getClientDeviceName() {
        return clientDeviceName;
    }

    public void setClientDeviceName(String clientDeviceName) {
        this.clientDeviceName = clientDeviceName;
    }

    public String getServerDeviceName() {
        return serverDeviceName;
    }

    public void setServerDeviceName(String serverDeviceName) {
        this.serverDeviceName = serverDeviceName;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public String getServerDeviceIp() {
        return serverDeviceIp;
    }

    public void setServerDeviceIp(String serverDeviceIp) {
        this.serverDeviceIp = serverDeviceIp;
    }
}
