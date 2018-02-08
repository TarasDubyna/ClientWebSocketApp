package taras.clientwebsocketapp.network;

import io.reactivex.Observable;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public interface ConnectionRepository {

    Observable<ScannerPackage> scanNetwork(ScanningInterface scanningInterface, String networkIP);
}
