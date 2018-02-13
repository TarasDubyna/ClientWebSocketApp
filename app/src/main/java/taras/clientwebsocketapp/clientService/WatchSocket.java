package taras.clientwebsocketapp.clientService;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
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

/**
 * Created by Taras on 04.02.2018.
 */

public class WatchSocket extends AsyncTask<WatchData, Integer, Integer> {

    private static final String LOG_TAG = "myLogs";

    private long timeout = 100;
    private long startTime;
    private boolean state = true;

    AsyncTask<SocketData, Integer, Integer> running;

    Context mContext;
    Socket mSocket;

    String ipAddress;
    String portAddress;

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    protected Integer doInBackground(WatchData... param) {
        InetAddress serverAddr;

        mContext = param[0].ctx;
        ipAddress = param[0].ipAddress;
        portAddress = param[0].portAddress;
        String message = param[0].message;

        try {
            while(true)
            {
                Log.d(LOG_TAG, "WatchSocket: open socket - " + ipAddress);
                serverAddr = InetAddress.getByName(ipAddress);
                mSocket = new Socket(serverAddr, Integer.parseInt(portAddress));

                Thread.sleep(50);

                // открываем сокет-соединение
                SocketData data = new SocketData();
                data.ctx = mContext;
                data.sock = mSocket;
                data.message = message;

                // ВНИМАНИЕ! Финт ушами - еще один поток =)
                // Именно он будет принимать входящие сообщения

                /*
                GetPacket pack = new GetPacket();
                running = pack.execute(data);
                */

                // Посылаем message на сервер
                try {
                    Log.d(LOG_TAG, "WatchSocket: send message - " + ipAddress);
                    PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(mSocket.getOutputStream())),true);
                    out.println(message);
                } catch(Exception e){

                }

                // Следим за потоком, принимающим сообщения
                startTime = System.currentTimeMillis();
                while(state){
                    if ((System.currentTimeMillis() - startTime) > timeout){
                        Log.d(LOG_TAG, "WatchSocket: timeout end - " + ipAddress);
                        state = false;
                    }
                    InputStreamReader inputStreamReader = new InputStreamReader(mSocket.getInputStream());
                    final StringBuilder stringBuilder = new StringBuilder();
                    char[] buffer = new char[4096];
                    stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));
                    Handler handler = new Handler(Looper.getMainLooper());
                    Log.d(LOG_TAG, "WatchSocket: socket get response - " + ipAddress);
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
                    Log.d(LOG_TAG, "WatchSocket: close socket in the center - " + ipAddress);
                    mSocket.close();
                }
                catch (SocketTimeoutException e){
                    if(mSocket.isConnected()){
                        Log.d(LOG_TAG, "WatchSocket: socket connected - " + ipAddress);
                        Toast.makeText(mContext, "connected", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(LOG_TAG, "WatchSocket: socket disconnected - " + ipAddress);
                    Toast.makeText(mContext, "disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
        }
        try {
            Log.d(LOG_TAG, "WatchSocket: close socket in the end - " + ipAddress);
            mSocket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            return -1;
        }
    }
}
