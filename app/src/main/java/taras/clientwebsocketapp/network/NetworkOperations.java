package taras.clientwebsocketapp.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import taras.clientwebsocketapp.Utils.Constants;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkOperations {

    private static final String LOG_TAG = "myLogs";


    private String ip;

    public NetworkOperations(String ip) {
        this.ip = ip;
    }

    public NetworkOperations() {
    }

    public static String takeRequest(String ip){
        InetAddress serverAddr;
        Socket socket = null;

        long startTime;
        long timeout = 500;
        boolean state = true;

        try {
            while(true) {
                Log.d(LOG_TAG, "WatchSocket: open socket - " + ip);
                serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, Constants.SERVER_PORT);

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
        return "test";
    }

    public static ScannerPackage convertResponse(String response){
        return new ScannerPackage();
    }
}
