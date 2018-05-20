package taras.clientwebsocketapp.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;

public class FileSendStatePackage extends Package {

    private static final String LOG_TAG = FileSendStatePackage.class.getSimpleName();

    @SerializedName("token") @Expose private String token;
    @SerializedName("current_part") @Expose private int currentPart;
    @SerializedName("all_part") @Expose private int allPart;
    //@SerializedName("state") @Expose private boolean state;
    @SerializedName("file_name") @Expose private String fileName;

    public FileSendStatePackage() {
        super(Constants.PACKAGE_FILE_SEND_STATE);
    }

    public FileSendStatePackage(FileSendPackage fileSendPackage) {
        super(Constants.PACKAGE_FILE_SEND_STATE);
        this.token = fileSendPackage.getToken();
        this.currentPart = fileSendPackage.getCurrentPart();
        this.allPart = fileSendPackage.getAllPart();
        this.fileName = fileSendPackage.getFileName();
        Log.d(LOG_TAG, "Create FileSendStatePackage: " + this.toJson());
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

    /*public boolean isState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }*/

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String toJson(){
        return GsonUtils.createJsonFileSendStatePackage(this);
    }
    public static FileSendStatePackage parse(String json){
        return GsonUtils.parseFileSendStatePackage(json);
    }
}
