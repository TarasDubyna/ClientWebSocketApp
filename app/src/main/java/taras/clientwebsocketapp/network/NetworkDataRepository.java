package taras.clientwebsocketapp.network;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import taras.clientwebsocketapp.Utils.NetworkUtils;
import taras.clientwebsocketapp.clientService.WatchSocket;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkDataRepository implements ConnectionRepository {

    private ScanningInterface scanningInterface;

    @Override
    public Observable<ScannerPackage> scanNetwork(ScanningInterface scanningInterface, String networkIP) {
        this.scanningInterface = scanningInterface;
        return (Observable<ScannerPackage>) Observable.just(networkIP)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String ip) throws Exception {
                        for (int i =0; i < 255; i++){
                            return ip + i;
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(NetworkOperations::takeRequest)
                .map(NetworkOperations::convertResponse)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scanningInterface::successfulResponse, scanningInterface::errorResponse);
    }
}
