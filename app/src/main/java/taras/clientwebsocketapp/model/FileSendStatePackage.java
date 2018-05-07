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
}
