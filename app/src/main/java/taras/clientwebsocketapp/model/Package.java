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
    @SerializedName("clientIp")
    @Expose
    private String clientIp;
    @SerializedName("serverIp")
    @Expose
    private String serverIp;

    private String description;

    public Package(String type) {
        this.type = type;
        this.description = description;
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
}
