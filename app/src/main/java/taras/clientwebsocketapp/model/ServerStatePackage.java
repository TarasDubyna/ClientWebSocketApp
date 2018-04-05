package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;
import taras.clientwebsocketapp.utils.TimeUtils;

/**
 * Created by Taras on 05.04.2018.
 */

public class ServerStatePackage extends Package {
    @SerializedName("time_send")
    @Expose
    private long timeSend;
    @SerializedName("request_type")
    @Expose
    private String requestType;

    public ServerStatePackage(Package pack) {
        super(Constants.PACKAGE_TYPE_SERVER_STATE);
        this.setClientIp(pack.getClientIp());
        this.setClientName(pack.getClientName());
        this.setServerIp(pack.getServerIp());
        this.setServerName(pack.getServerName());
        this.timeSend = TimeUtils.getCurrentTime();
        this.requestType = pack.getType();
    }

    public String toJson(){
        return GsonUtils.createJsonServerStatePackage(this);
    }
    public static ServerStatePackage parse(String json){
        return GsonUtils.parseServerStatePackage(json);
    }
}
