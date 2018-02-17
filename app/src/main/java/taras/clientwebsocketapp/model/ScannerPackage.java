package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Taras on 08.02.2018.
 */

public class ScannerPackage {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("clientData")
    @Expose
    private ClientData clientData;
    @SerializedName("serverData")
    @Expose
    private ServerData serverData;



    public ScannerPackage(String type, ClientData clientData) {
        this.type = type;
        this.clientData = clientData;
    }

    public ScannerPackage(String type, ServerData serverData) {
        this.type = type;
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
        @SerializedName("myOs")
        @Expose
        private String myOs;

        public ClientData(String myIp, String myOs) {
            this.myIp = myIp;
            this.myOs = myOs;
        }
    }
    public static class ServerData{
        @SerializedName("serverName")
        @Expose
        private String serverName;
        @SerializedName("serverIp")
        @Expose
        private String serverIp;

        public String getServerName() {
            return serverName;
        }
        public String getServerIp() {
            return serverIp;
        }


        public ServerData(String serverIp, String serverName) {
            this.serverIp = serverIp;
            this.serverName = serverName;
        }
    }
}
