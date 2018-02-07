package taras.clientwebsocketapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
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
import java.net.Socket;
import java.net.SocketTimeoutException;

import taras.clientwebsocketapp.clientService.NotificationService;


/**
 * Created by Taras on 06.02.2018.
 */

public class NetworkScanner {
    private static final String LOG_TAG = "myLogs";

    private String mDeviceIP;
    private Context mContext;
    Socket socket = null;

    public NetworkScanner(Context mContext) {
        this.mContext = mContext;
        this.mDeviceIP = getIP();
    }

    private String getIP(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    private boolean checkWiFiConnection(){
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    public void scanNetwork(){
        Intent intent = new Intent(mContext, NotificationService.class);
        intent.putExtra(NotificationService.TYPE, NotificationService.SCAN_NETWORK);
        intent.putExtra(NotificationService.DEVICE_IP, getIP());
        mContext.startService(intent);
    }


    public void scanMethod(String mes, String ip, String portAddress){
        InetAddress serverAddr;

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
                            Toast.makeText(mContext, stringBuilder, Toast.LENGTH_SHORT).show();
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
            try {
                if (socket!= null){
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
