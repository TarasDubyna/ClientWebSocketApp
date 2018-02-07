package taras.clientwebsocketapp.clientService;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Taras on 04.02.2018.
 */

class GetPacket extends AsyncTask<SocketData, Integer, Integer> {
    private static final String LOG_TAG = "myLogs";
    private long timeout = 10000;
    private long startTime;
    private boolean state = true;

    Context mCtx;
    char[] mData;
    Socket mySock;

    protected void onPostExecute(Integer result) {
        // Это выполнится после завершения работы потока
    }

    protected Integer doInBackground(SocketData... param) {
        mySock = param[0].sock;
        mCtx = param[0].ctx;
        mData = new char[4096];

        try {
            Log.d(LOG_TAG, "GetPacket: create reader");
            BufferedReader reader = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
            int read = 0;
            // Принимаем сообщение от сервера
            // Данный цикл будет работать, пока соединение не оборвется
            // или внешний поток не скажет данному cancel()
            //startTime = System.currentTimeMillis();
            while ((read = reader.read(mData)) >= 0 && !isCancelled() && state) {
                // "Вызываем" onProgressUpdate каждый раз, когда принято сообщение
                Log.d(LOG_TAG, "GetPacket: progress, reade = " + read);
                if(read > 0) {
                    Log.d(LOG_TAG, "GetPacket: publish progress");
                    publishProgress(read);
                }
                /*
                if ((System.currentTimeMillis() - startTime) > timeout){
                    state = false;
                }
                */
            }
            Log.d(LOG_TAG, "GetPacket: close reader");
            reader.close();
        } catch (IOException e) {
            Log.d(LOG_TAG, "GetPacket: IOException");
            return -1;
        }
        catch (Exception e) {
            Log.d(LOG_TAG, "GetPacket: Exception");
            return -1;
        }
        Log.d(LOG_TAG, "GetPacket: end");
        return 0;
    }

    protected void onProgressUpdate(Integer... progress) {
        try {
            // Получаем принятое от сервера сообщение
            Log.d(LOG_TAG, "GetPacket: publish progress");
            String prop = String.valueOf(mData);
            // Делаем с сообщением, что хотим. Я, например, пишу в базу
        } catch(Exception e) {
            Toast.makeText(mCtx, "Socket error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}