package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;

/**
 * Created by Taras on 05.04.2018.
 */

public class ServerStatePackage extends Package {
    @SerializedName("time_send")
    @Expose
    private String timeSend;
    @SerializedName("request_type")
    @Expose
    private String requestType;

    public ServerStatePackage(Package pack) {
        super(Constants.PACKAGE_TYPE_SERVER_STATE);
        this.setClientIp(pack.getClientIp());
        this.setClientName(pack.getClientName());
        this.setServerIp(pack.getServerIp());
        this.setServerName(pack.getServerName());
        this.timeSend = createTimeSend();
        this.requestType = pack.getType();
    }

    private String createTimeSend(){
        return String.valueOf(System.currentTimeMillis());
    }

    public String toJson(){
        return GsonUtils.createJsonServerStatePackage(this);
    }
    public static ServerStatePackage parse(String json){
        return GsonUtils.parseServerStatePackage(json);
    }
}
