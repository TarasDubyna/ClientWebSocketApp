package taras.clientwebsocketapp.network.callbacks;

import taras.clientwebsocketapp.model.PermissionPackage;

public interface GetPermissionCallback extends NetworkCallback {
    void successfulGetPermission(PermissionPackage permissionPackage);
    void errorGetPermissionResponse(Throwable throwable);
}
