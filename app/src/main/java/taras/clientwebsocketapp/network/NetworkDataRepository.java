package taras.clientwebsocketapp.network;

import java.io.IOException;
import java.util.List;

import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.NetworkUtils;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkDataRepository implements ConnectionRepository {

    private ScanningInterface scanningInterface;

    @Override
    public void scanNetwork(ScanningInterface scanningInterface, String networkIP) throws IOException {
        this.scanningInterface = scanningInterface;
        List<String> consumer = NetworkUtils.getAllNetworkAddresses();
        for (String address: consumer){
            new Thread(() -> NetworkOperations.takeRequest(address, this.scanningInterface)).start();
        }
    }

    @Override
    public void sendMessage(ScanningInterface scanningInterface, String message, String ip) throws IOException {
        this.scanningInterface = scanningInterface;
        new Thread(() -> NetworkOperations.takeRequest(ip, message, this.scanningInterface)).start();
    }

    @Override
    public void getPermission(ScanningInterface scanningInterface, PermissionPackage permissionPackage) throws IOException {
        this.scanningInterface = scanningInterface;
        new Thread(() -> NetworkOperations.checkPermission(permissionPackage, this.scanningInterface)).start();
    }

}
