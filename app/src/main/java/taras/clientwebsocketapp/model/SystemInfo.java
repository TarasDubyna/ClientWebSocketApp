package taras.clientwebsocketapp.model;

/**
 * Created by Taras on 06.02.2018.
 */

public class SystemInfo {

    private String clientIp;
    private String infoOS;

    public SystemInfo() {
    }

    public String getClientIp() {
        return clientIp;
    }
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getInfoOS() {
        return infoOS;
    }
    public void setInfoOS(String infoOS) {
        this.infoOS = infoOS;
    }
}
