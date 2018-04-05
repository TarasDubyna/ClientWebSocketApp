package taras.clientwebsocketapp.network;

import java.io.IOException;

import taras.clientwebsocketapp.model.PermissionPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public interface ConnectionRepository {
    void scanNetwork(ScanningInterface scanningInterface, String networkIP) throws IOException;
    void getPermission(ScanningInterface scanningInterface, PermissionPackage permissionPackage) throws IOException;
}
