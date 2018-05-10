package taras.clientwebsocketapp.managers.file_sender_manager;

import taras.clientwebsocketapp.model.FileSendStatePackage;

public interface FileSenderRequestCallback {
    void getFileSendResponse(FileSendStatePackage fileSendStatePackage);
    void errorRequest(Throwable throwable);
}
