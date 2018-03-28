package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;

/**
 * Created by Taras on 08.02.2018.
 */

public class ScannerPackage extends Package {

    @SerializedName("clientData")
    @Expose
    private ClientData clientData;
    @SerializedName("serverData")
    @Expose
    private ServerData serverData;

    public ScannerPackage(ClientData clientData, ServerData serverData) {
        super(Constants.PACKAGE_TYPE_SCANNING);
        this.clientData = clientData;
        this.serverData = serverData;
    }

    public void setDescription(int description){
        this.setDescription(description);
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

    public String toJson(){
        return GsonUtils.createJsonScannerPackage(this);
    }
    public static ScannerPackage parse(String json){
        return GsonUtils.parseScannerPackage(json);
    }
}
