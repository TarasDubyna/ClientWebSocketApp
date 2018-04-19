package taras.clientwebsocketapp.network;

import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public interface RequestServiceInterface {


    void successfulGetPermissionFirstStage(PermissionPackage permissionPackage);
    void successfulGetPermissionSecondStage(PermissionPackage permissionPackage);


    void errorGetPermission(PermissionPackage permissionPackage);
    void errorResponse(Throwable throwable);


    void successfulScanningResponse(ScannerPackage scannerPackage);
    void scanningNetworkEnd();
    void errorScanning(Throwable throwable);
}
