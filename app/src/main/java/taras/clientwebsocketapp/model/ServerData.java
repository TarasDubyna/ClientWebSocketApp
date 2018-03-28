package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Taras on 28.03.2018.
 */

public class ServerData {
    @SerializedName("serverName")
    @Expose
    private String serverName;

    public ServerData(String serverName) {
        this.serverName = serverName;
    }
}
