package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Taras on 08.02.2018.
 */

public class ScannerPackage {

    @SerializedName("type")
    private String type;

    @SerializedName("clientData")
    @Expose
    private ClientData clientData;
    @SerializedName("serverData")
    @Expose
    private ServerData serverData;


    public ScannerPackage(ClientData clientData) {
        this.clientData = clientData;
    }

    public ScannerPackage(ServerData serverData) {
        this.serverData = serverData;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public ClientData getClientData() {
        return clientData;
    }
    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    public ServerData getServerData() {
        return serverData;
    }
    public void setServerData(ServerData serverData) {
        this.serverData = serverData;
    }

    public static class ClientData{
        @SerializedName("myIp")
        @Expose
        private String myIp;
        @SerializedName("myMac")
        @Expose
        private String myMac;

        public ClientData(String myIp, String myMac) {
            this.myIp = myIp;
            this.myMac = myMac;
        }
    }
    public class ServerData{
        @SerializedName("serverIp")
        @Expose
        private String serverIp;
        @SerializedName("serverMac")
        @Expose
        private String serverMac;

        public ServerData(String serverIp, String serverMac) {
            this.serverIp = serverIp;
            this.serverMac = serverMac;
        }
    }
}
