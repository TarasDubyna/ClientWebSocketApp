package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Taras on 28.03.2018.
 */

public class ClientData{
    @SerializedName("clientName")
    @Expose
    private String clientName;

    public ClientData(String clientName) {
        this.clientName = clientName;
    }
}
