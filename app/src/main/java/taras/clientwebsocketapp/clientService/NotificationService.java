package taras.clientwebsocketapp.clientService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import taras.clientwebsocketapp.NetworkScanner;

/**
 * Created by Taras on 04.02.2018.
 */

public class NotificationService extends Service {

    private static final String LOG_TAG = "myLogs";

    public static final String MY_ACTION = "MY_ACTION";

    public static final String TYPE = "TYPE";

    public static final String SCAN_NETWORK = "SCAN_NETWORK";
    public static final String DEVICE_IP = "DEVICE_IP";
    private String deviceIP;


    public static final String SEND_MESSAGE = "SEND_MESSAGE";
    public static final String SEND_DATA = "SEND_DATA";

    public static final int PORT = 20000;

    private String type;

    private Context mContext;

    String ipAddress;
    String messageText;


    public class LocalBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        type = intent.getStringExtra(TYPE);
        messageText = intent.getStringExtra("message");
        switch (type){
            case SCAN_NETWORK:
                ipAddress = intent.getStringExtra(DEVICE_IP);
                startScanNetwork();
                break;
            case SEND_MESSAGE:
                sendMessage(intent.getStringExtra("ip"), messageText);
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

    // Здесь выполняем инициализацию нужных нам значений
    // и открываем наше сокет-соединение



    // данный метод открыает соединение
    public void sendMessage(String serverIp, String messageText) {
        Log.d(LOG_TAG, "start scanning");
        try {
            // WatchData - это класс, с помощью которого мы передадим параметры в
            // создаваемый поток
            WatchData data = new WatchData();
            data.message = messageText;
            data.ctx = this;
            data.ipAddress = serverIp;
            data.portAddress = String.valueOf(PORT);

            // создаем новый поток для сокет-соединения
            new WatchSocket().execute(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startScanNetwork(){
        Log.d(LOG_TAG, "start scanning");
        ipAddress = getIP(getApplicationContext());
        String[] fn = ipAddress.split("\\.");
        String networkAddress = "";
        for (int i = 0; i < fn.length - 1; i++){
            System.out.println(fn[i]);
            networkAddress += (fn[i]) + ".";
        }
        scanning(networkAddress);


        /*
        for (int i = 0; i < 255; i++){
            //final WatchData data = new WatchData(messageText, this, networkAddress + i, String.valueOf(PORT));
            Log.d(LOG_TAG, "scanning networkAddress: " + networkAddress + i);
            //new WatchSocket().execute(data);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 25; i++){
                        new WatchSocket().execute(new WatchData(messageText, mContext, networkAddressFinal + i, String.valueOf(PORT)));
                    }
                }
            }).start();
        }
        */
    }

    private String getIP(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    private void scanning(final String networkAddressFinal){
        for (int i = 0; i < 255; i++){
            final int p = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new NetworkScanner(getApplicationContext()).scanMethod("this is scanning", networkAddressFinal + p, String.valueOf(PORT));
                    //scanMethod("hello scann", networkAddressFinal + p,String.valueOf(PORT));
                    //new WatchSocket().execute(new WatchData(messageText, mContext, networkAddressFinal + p, String.valueOf(PORT)));
                }
            }).start();
        }
    }


    private void scanMethod(String mes, String ip, String portAddress){
        InetAddress serverAddr;
        Socket socket = null;

        long startTime;
        long timeout = 500;
        boolean state = true;

        try {
            while(true) {
                Log.d(LOG_TAG, "WatchSocket: open socket - " + ip);
                serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, Integer.parseInt(portAddress));

                // Посылаем message на сервер
                try {
                    Log.d(LOG_TAG, "WatchSocket: send message - " + ip);
                    PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
                    out.println(mes);
                } catch(Exception e){

                }

                // Следим за потоком, принимающим сообщения
                startTime = System.currentTimeMillis();
                while(state){
                    if ((System.currentTimeMillis() - startTime) > timeout){
                        Log.d(LOG_TAG, "WatchSocket: timeout end - " + ip);
                        state = false;
                    }
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    final StringBuilder stringBuilder = new StringBuilder();
                    char[] buffer = new char[4096];
                    stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));
                    Handler handler = new Handler(Looper.getMainLooper());
                    Log.d(LOG_TAG, "WatchSocket: socket get response - " + ip);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), stringBuilder, Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                // Если поток закончил принимать сообщения - это означает,
                // что соединение разорвано (других причин нет).
                // Это означает, что нужно закрыть сокет
                // и открыть его опять в бесконечном цикле (см. while(true) выше)
                try {
                    Log.d(LOG_TAG, "WatchSocket: close socket in the center - " + ip);
                    socket.close();
                }
                catch (SocketTimeoutException e){
                    if(socket.isConnected()){
                        Log.d(LOG_TAG, "WatchSocket: socket connected - " + ip);
                        Toast.makeText(mContext, "connected", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(LOG_TAG, "WatchSocket: socket disconnected - " + ip);
                    Toast.makeText(mContext, "disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {

        } finally {
            Log.d(LOG_TAG, "WatchSocket: close socket in the end - " + ip);
            /*
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }

    }

}
