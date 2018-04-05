package taras.clientwebsocketapp.network;

import java.io.IOException;
import java.util.List;

import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.NetworkUtils;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkDataRepository implements ConnectionRepository {
    @Override
    public void scanNetwork(ScanningInterface scanningInterface, String networkIP) throws IOException {
        for (String address: NetworkUtils.getAllNetworkAddresses()){
            new Thread(() -> NetworkOperations.scanNetwork(address, scanningInterface)).start();
        }
    }

    @Override
    public void getPermission(ScanningInterface scanningInterface, PermissionPackage permissionPackage) throws IOException {
        new Thread(() -> NetworkOperations.getPermission(permissionPackage, scanningInterface)).start();
    }

}
