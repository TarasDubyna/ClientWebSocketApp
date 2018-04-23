package taras.clientwebsocketapp.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

import taras.clientwebsocketapp.model.PermissionPackageFirst;
import taras.clientwebsocketapp.utils.NetworkUtils;
import taras.clientwebsocketapp.utils.TimeUtils;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkDataRepository implements ConnectionRepository {

    private static final int CONNECTION_TIMEOUT = 5000;// 5sec
    boolean isScanningTimeout = false;

    @Override
    public void scanNetwork(RequestServiceInterface requestServiceInterface, String networkIP) throws IOException {
        try {
            TimeUtils.startTimer(CONNECTION_TIMEOUT, new TimerTask() {
                @Override
                public void run() {
                    isScanningTimeout = true;
                    requestServiceInterface.scanningNetworkEnd();
                }
            });



            ArrayList<Thread> arrThreads = new ArrayList<Thread>();
            for (String address: NetworkUtils.getAllNetworkAddresses()){
                Thread scanningThread = new Thread(() -> NetworkOperations.scanNetwork(address, requestServiceInterface));
                scanningThread.start();
                arrThreads.add(scanningThread);
            }

            for (Thread scanningThread: arrThreads){
                scanningThread.join();
            }
            if (!isScanningTimeout){
                requestServiceInterface.scanningNetworkEnd();
            }
        } catch (Exception  ex) {
            ex.printStackTrace();
            requestServiceInterface.errorScanning(ex);
        }

    }

    @Override
    public void getPermission(RequestServiceInterface requestServiceInterface, PermissionPackageFirst permissionPackageFirst) throws IOException {
        new Thread(() -> NetworkOperations.getPermission(permissionPackageFirst, requestServiceInterface)).start();
    }
}
