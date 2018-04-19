package taras.clientwebsocketapp.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.NetworkUtils;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkDataRepository implements ConnectionRepository {
    @Override
    public void scanNetwork(RequestServiceInterface requestServiceInterface, String networkIP) throws IOException {
        try {
            ArrayList<Thread> arrThreads = new ArrayList<Thread>();
            for (String address: NetworkUtils.getAllNetworkAddresses()){
                Thread scanningThread = new Thread(() -> NetworkOperations.scanNetwork(address, requestServiceInterface));
                scanningThread.start();
                arrThreads.add(scanningThread);
            }
            for (Thread scanningThread: arrThreads){
                scanningThread.join();
            }
            requestServiceInterface.scanningNetworkEnd();
        } catch (Exception  ex) {
            ex.printStackTrace();
            requestServiceInterface.errorScanning(ex);
        }

    }

    @Override
    public void getPermission(RequestServiceInterface requestServiceInterface, PermissionPackage permissionPackage) throws IOException {
        new Thread(() -> NetworkOperations.getPermission(permissionPackage, requestServiceInterface)).start();
    }

}
