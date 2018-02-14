package taras.clientwebsocketapp.clientService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.network.NetworkConnection;
import taras.clientwebsocketapp.network.ScanningInterface;

/**
 * Created by Taras on 04.02.2018.
 */

public class NotificationService extends Service implements ScanningInterface {

    private static final String LOG_TAG = "myLogs";

    public static final String MY_ACTION = "MY_ACTION";

    public static final String TYPE = "TYPE";
    public static final String SCAN_NETWORK = "SCAN_NETWORK";
    public static final String DEVICE_IP = "DEVICE_IP";

    public static final String SEND_MESSAGE = "SEND_MESSAGE";
    public static final String SEND_DATA = "SEND_DATA";

    private String type;
    private String serverIp;
    private String messageText;



    public class LocalBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        type = intent.getStringExtra(TYPE);
        switch (type){
            case SCAN_NETWORK:
                try {
                    NetworkConnection.getConnectionRepository()
                            .scanNetwork(this, AppApplication.networkIp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case SEND_MESSAGE:
                try {
                    messageText = intent.getStringExtra("message");
                    serverIp = intent.getStringExtra("ip");
                    NetworkConnection.getConnectionRepository()
                            .sendMessage(this, messageText, serverIp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case SEND_DATA:
                break;

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onDestroy() { }


    @Override
    public void successfulResponse(String scannerPackage) {
        Log.d(LOG_TAG, "successfulResponse: " + scannerPackage);
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("string", scannerPackage);
        sendBroadcast(intent);
    }

    @Override
    public void errorResponse(Throwable throwable) {

    }

    @Override
    public void successfulResponse(ScannerPackage scannerPackage) {

    }
}
