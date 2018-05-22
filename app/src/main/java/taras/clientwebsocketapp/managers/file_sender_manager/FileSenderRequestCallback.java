package taras.clientwebsocketapp.managers.file_sender_manager;

import taras.clientwebsocketapp.model.FileSendStatePackage;
import taras.clientwebsocketapp.network.callbacks.NetworkCallback;

public interface FileSenderRequestCallback extends NetworkCallback {
    void getFileSendResponse(FileSendStatePackage fileSendStatePackage);
    void errorRequest(FileSendStatePackage fileSendStatePackage, Throwable throwable);
}
