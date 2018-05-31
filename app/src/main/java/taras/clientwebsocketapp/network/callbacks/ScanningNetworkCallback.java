package taras.clientwebsocketapp.network.callbacks;

import taras.clientwebsocketapp.model.ScannerPackage;

public interface ScanningNetworkCallback extends NetworkCallback {
    void successfulScanningResponse(ScannerPackage scannerPackage);
    void scanningNetworkEnd();
    void errorScanning(Throwable throwable);
}
