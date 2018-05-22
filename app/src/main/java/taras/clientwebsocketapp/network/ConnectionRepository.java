package taras.clientwebsocketapp.network;

import java.io.IOException;

import taras.clientwebsocketapp.managers.file_sender_manager.FileSenderRequestCallback;
import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.network.callbacks.RequestServiceInterface;

/**
 * Created by Taras on 08.02.2018.
 */

public interface ConnectionRepository {
    void scanNetwork(RequestServiceInterface requestServiceInterface, String networkIP) throws IOException;
    void getPermission(RequestServiceInterface scanningInterface, PermissionPackage permissionPackage) throws IOException;
    void sendFilePackage(FileSenderRequestCallback fileSenderRequestCallback, FileSendPackage fileSendPackage) throws IOException;
}
