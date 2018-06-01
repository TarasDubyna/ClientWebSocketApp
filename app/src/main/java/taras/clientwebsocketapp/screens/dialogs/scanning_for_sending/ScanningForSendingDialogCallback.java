package taras.clientwebsocketapp.screens.dialogs.scanning_for_sending;

import taras.clientwebsocketapp.model.PermissionPackage;

public interface ScanningForSendingDialogCallback {
    void sendPermission(PermissionPackage permissionPackage);
}
