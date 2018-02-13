package taras.clientwebsocketapp.network;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import taras.clientwebsocketapp.Utils.Constants;
import taras.clientwebsocketapp.Utils.NetworkUtils;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkDataRepository implements ConnectionRepository {

    private ScanningInterface scanningInterface;

    @Override
    public void scanNetwork(ScanningInterface scanningInterface, String networkIP) throws IOException {
        this.scanningInterface = scanningInterface;
        List<String> consumer = NetworkUtils.getAllNetworkAddresses();
        for (String address: consumer){
            new Thread(() -> NetworkOperations.takeRequest(address, this.scanningInterface)).start();
        }
    }

    @Override
    public void sendMessage(ScanningInterface scanningInterface, String message, String ip) throws IOException {
        this.scanningInterface = scanningInterface;
        new Thread(() -> NetworkOperations.takeRequest(ip, message, this.scanningInterface)).start();
    }

}
