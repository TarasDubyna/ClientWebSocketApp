package taras.clientwebsocketapp.network;

import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public interface ScanningInterface {
    void successfulResponse(ScannerPackage scannerPackage);
    void errorResponse(Throwable throwable);
}
