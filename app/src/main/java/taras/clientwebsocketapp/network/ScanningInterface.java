package taras.clientwebsocketapp.network;

import taras.clientwebsocketapp.model.GetPermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public interface ScanningInterface {
    void successfulResponse(String stringJson);
    void errorResponse(Throwable throwable);
    void successfulScanningResponse(ScannerPackage scannerPackage);

    void successfulGetPermission(GetPermissionPackage getPermissionPackage);
    void errorGetPermission(GetPermissionPackage getPermissionPackage);
}
