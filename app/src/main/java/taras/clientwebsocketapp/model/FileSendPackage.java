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

    public FileSendPackage(String type) {
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

    public String toJson(){
        return GsonUtils.createJsonFileSendPackage(this);
    }
    public static FileSendPackage parse(String json){
        return GsonUtils.parseFileSendPackage(json);
    }


}
