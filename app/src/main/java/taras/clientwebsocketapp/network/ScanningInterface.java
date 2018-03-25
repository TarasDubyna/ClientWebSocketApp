package taras.clientwebsocketapp.network;

import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public interface ScanningInterface {
    void successfulResponse(String stringJson);
    void errorResponse(Throwable throwable);
    void successfulScanningResponse(ScannerPackage scannerPackage);

    void successfulGetPermission(PermissionPackage permissionPackage);
    void errorGetPermission(PermissionPackage permissionPackage);
}
