package taras.clientwebsocketapp.network.callbacks;

import taras.clientwebsocketapp.model.ScannerPackage;

public interface ScanningNetworkCallback {
    void successfulScanningResponse(ScannerPackage scannerPackage);
    void scanningNetworkEnd();
    void errorScanning(Throwable throwable);
}
