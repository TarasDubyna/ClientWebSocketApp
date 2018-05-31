package taras.clientwebsocketapp.network;

import java.io.IOException;

import taras.clientwebsocketapp.network.callbacks.FileSenderRequestCallback;
import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.network.callbacks.GetPermissionCallback;
import taras.clientwebsocketapp.network.callbacks.ScanningNetworkCallback;

/**
 * Created by Taras on 08.02.2018.
 */

public interface ConnectionRepository {
    void scanNetwork(ScanningNetworkCallback callback, String networkIP) throws IOException;
    void getPermission(GetPermissionCallback callback, PermissionPackage permissionPackage) throws IOException;
    void sendFilePackage(FileSenderRequestCallback fileSenderRequestCallback, FileSendPackage fileSendPackage) throws IOException;
}
