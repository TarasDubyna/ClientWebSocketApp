package taras.clientwebsocketapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;

public class FileSendPackage extends Package{

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("current_part")
    @Expose
    private int currentPart;
    @SerializedName("all_part")
    @Expose
    private int allPart;

    @SerializedName("fileName")
    @Expose
    private String fileName;

    @SerializedName("data")
    @Expose
    private byte[] data;

    public FileSendPackage() {
        super(Constants.PACKAGE_FILE_SEND);
    }

    public String getToken() {
        return token;
    }
    public int getCurrentPart() {
        return currentPart;
    }
    public int getAllPart() {
        return allPart;
    }
    public String getFileName() {
        return fileName;
    }
    public byte[] getData() {
        return data;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void setCurrentPart(int currentPart) {
        this.currentPart = currentPart;
    }
    public void setAllPart(int allPart) {
        this.allPart = allPart;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String toJson(){
        return GsonUtils.createJsonFileSendPackage(this);
    }
    public static FileSendPackage parse(String json){
        return GsonUtils.parseFileSendPackage(json);
    }


    public void fillAfterPermission(PermissionPackage permissionPackage){
        this.setClientIp(permissionPackage.getClientIp());
        this.setClientName(permissionPackage.getClientName());
        this.setServerIp(permissionPackage.getServerIp());
        this.setServerName(permissionPackage.getServerName());

        this.setToken(permissionPackage.getToken());
    }

    public void createToSend(int currentPart, int allPart, byte[] data){
        this.setCurrentPart(currentPart);
        this.setAllPart(allPart);
        this.setData(data);
    }


}
