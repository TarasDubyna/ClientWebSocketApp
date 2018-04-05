package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import taras.clientwebsocketapp.AppApplication;

/**
 * Created by Taras on 28.03.2018.
 */

public class Package {
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("client_ip")
    @Expose
    private String clientIp;
    @SerializedName("client_name")
    @Expose
    private String clientName;

    @SerializedName("server_ip")
    @Expose
    private String serverIp;
    @SerializedName("server_name")
    @Expose
    private String serverName;


    private int description;

    public Package(String type) {
        this.type = type;
        this.clientIp = AppApplication.deviceIp;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getClientIp() {
        return clientIp;
    }
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    public String getServerIp() {
        return serverIp;
    }
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public int getDescription() {
        return description;
    }
    public void setDescription(int description) {
        this.description = description;
    }
}
