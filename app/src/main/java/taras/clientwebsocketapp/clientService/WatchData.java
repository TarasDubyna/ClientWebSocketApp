package taras.clientwebsocketapp.clientService;

import android.content.Context;

/**
 * Created by Taras on 04.02.2018.
 */

public class WatchData {

    public WatchData(String message, Context ctx, String ipAddress, String portAddress) {
        this.message = message;
        this.ctx = ctx;
        this.ipAddress = ipAddress;
        this.portAddress = portAddress;
    }

    public WatchData() {
    }

    String message; //data witch we want send to server
    Context ctx;
    String ipAddress;
    String portAddress;

    @Override
    public String toString() {
        return "message; " + message
                +"ctx: " + ctx
                +"ipAddress" + ipAddress
                +"portAddress" + portAddress;
    }
}
