package taras.clientwebsocketapp.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

import taras.clientwebsocketapp.client.TCPClient.ShutdownAsyncTask;
import taras.clientwebsocketapp.managers.file_sender_manager.FileSenderRequestCallback;
import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.network.callbacks.RequestServiceInterface;
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
                ShutdownAsyncTask shutdownAsyncTask = new ShutdownAsyncTask(address, new ScannerPackage());
                shutdownAsyncTask.execute();
                shutdownAsyncTask.getStatus() ==
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
    public void getPermission(RequestServiceInterface scanningInterface, PermissionPackage permissionPackage){
        ShutdownAsyncTask getPermissionAsync = new ShutdownAsyncTask(permissionPackage.getServerIp(), permissionPackage);
        getPermissionAsync.execute();
    }

    @Override
    public void sendFilePackage(FileSenderRequestCallback fileSenderRequestCallback, FileSendPackage fileSendPackage) throws IOException {

    }

    /*@Override
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
    public void getPermission(RequestServiceInterface requestServiceInterface, PermissionPackage permissionPackage) throws IOException {
        new Thread(() -> NetworkOperations.getPermission(permissionPackage, requestServiceInterface)).start();
    }

    @Override
    public void sendFilePackage(FileSenderRequestCallback filePreparatorCallback, FileSendPackage fileSendPackage) throws IOException {
        new Thread(() -> NetworkOperations.sendFile(fileSendPackage, filePreparatorCallback)).start();
    }*/
}
