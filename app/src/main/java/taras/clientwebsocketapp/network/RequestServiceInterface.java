package taras.clientwebsocketapp.network;

import taras.clientwebsocketapp.model.PermissionPackageFirst;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public interface RequestServiceInterface {


    void successfulGetPermissionFirstStage(PermissionPackageFirst permissionPackageFirst);
    void successfulGetPermissionSecondStage(PermissionPackageFirst permissionPackageFirst);


    void errorGetPermission(PermissionPackageFirst permissionPackageFirst);
    void errorResponse(Throwable throwable);


    void successfulScanningResponse(ScannerPackage scannerPackage);
    void scanningNetworkEnd();
    void errorScanning(Throwable throwable);
}
