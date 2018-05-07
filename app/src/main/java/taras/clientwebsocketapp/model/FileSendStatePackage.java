package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileSendStatePackage extends Package {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("current_part")
    @Expose
    private int currentPart;
    @SerializedName("all_part")
    @Expose
    private int allPart;

    @SerializedName("data")
    @Expose
    private byte[] data;

    public FileSendStatePackage(String type) {
        super(type);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCurrentPart() {
        return currentPart;
    }

    public void setCurrentPart(int currentPart) {
        this.currentPart = currentPart;
    }

    public int getAllPart() {
        return allPart;
    }

    public void setAllPart(int allPart) {
        this.allPart = allPart;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
