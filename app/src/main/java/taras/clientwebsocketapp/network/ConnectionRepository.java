package taras.clientwebsocketapp.network;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public interface ConnectionRepository {
    void scanNetwork(ScanningInterface scanningInterface, String networkIP) throws IOException;
    void sendMessage(ScanningInterface scanningInterface, String message, String ip) throws IOException;
}
