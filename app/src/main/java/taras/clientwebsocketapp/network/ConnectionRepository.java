package taras.clientwebsocketapp.network;

import java.io.IOException;

import taras.clientwebsocketapp.model.PermissionPackageFirst;

/**
 * Created by Taras on 08.02.2018.
 */

public interface ConnectionRepository {
    void scanNetwork(RequestServiceInterface requestServiceInterface, String networkIP) throws IOException;
    void getPermission(RequestServiceInterface scanningInterface, PermissionPackageFirst permissionPackageFirst) throws IOException;
}
