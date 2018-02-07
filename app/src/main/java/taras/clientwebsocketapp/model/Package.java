package taras.clientwebsocketapp.model;

/**
 * Created by Taras on 06.02.2018.
 */

public class Package {

    private SystemInfo systemInfo;
    private Data data;

    private String key;
    private String error;
    private String message;


    public Package() {
    }

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }
    public void setSystemInfo(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
