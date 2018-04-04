package taras.clientwebsocketapp.network;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkOperations {

    private static final String LOG_TAG = "myLogs";

    public static void takeRequest(String ip, ScanningInterface scanningInterface) {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(ip), Constants.SERVER_PORT);
            while (true) {
                Log.d(LOG_TAG, "WatchSocket: open socket - " + ip);

                // Посылаем message на сервер
                try {
                    Log.d(LOG_TAG, "WatchSocket: send message - " + ip);
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(new ScannerPackage().toJson());
                } catch (Exception e) {}

                // Следим за потоком, принимающим сообщения
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    char[] buffer = new char[4096];
                    stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));
                    Log.d(LOG_TAG, "WatchSocket: socket get response - " + ip);
                    Log.d(LOG_TAG, "WatchSocket: response - " + stringBuilder);
                    if (stringBuilder.toString().length() > 0){
                        socket.close();
                        Log.d(LOG_TAG, "WatchSocket: close response socket - " + ip);
                        scanningInterface.successfulScanningResponse(GsonUtils.parseScannerPackage(stringBuilder.toString()));
                    }
                }
            }
        } catch (IOException e) {
            Log.d(LOG_TAG, "WatchSocket: IOException - " + ip);
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            scanningInterface.errorResponse(e);
        } catch (Exception e) {
            Log.d(LOG_TAG, "WatchSocket: Exception - " + ip);
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            scanningInterface.errorResponse(e);
        }
    }
    public static void takeRequest(String ip, String message, ScanningInterface scanningInterface) {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(ip), Constants.SERVER_PORT);
            while (true) {
                Log.d(LOG_TAG, "WatchSocket: open socket - " + ip);

                // Посылаем message на сервер
                try {
                    Log.d(LOG_TAG, "WatchSocket: send message - " + ip);
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(message);
                } catch (Exception e) {}

                // Следим за потоком, принимающим сообщения
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    char[] buffer = new char[4096];
                    stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));
                    Log.d(LOG_TAG, "WatchSocket: socket get response - " + ip);
                    Log.d(LOG_TAG, "WatchSocket: response - " + stringBuilder);
                    if (stringBuilder.toString().length() > 0){
                        socket.close();
                        Log.d(LOG_TAG, "WatchSocket: close response socket - " + ip);
                        scanningInterface.successfulResponse(stringBuilder.toString());
                    }
                }
            }
        } catch (IOException e) {
            Log.d(LOG_TAG, "WatchSocket: IOException - " + ip);
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            scanningInterface.errorResponse(e);
        } catch (Exception e) {
            Log.d(LOG_TAG, "WatchSocket: Exception - " + ip);
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            scanningInterface.errorResponse(e);
        }
    }

    public static void checkPermission(PermissionPackage permissionPackage, ScanningInterface scanningInterface) {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(permissionPackage.getServerIp()), Constants.SERVER_PORT);
            while (true) {
                Log.d(LOG_TAG, "WatchSocket: open socket - " + permissionPackage.getServerIp());

                // Посылаем message на сервер
                try {
                    Log.d(LOG_TAG, "WatchSocket: send message - " + permissionPackage.getServerIp());
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(permissionPackage.toJson());
                } catch (Exception e) {}

                // Следим за потоком, принимающим сообщения
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    char[] buffer = new char[4096];
                    stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));
                    Log.d(LOG_TAG, "WatchSocket: socket get response - " + permissionPackage.getServerIp());
                    Log.d(LOG_TAG, "WatchSocket: response - " + stringBuilder);
                    if (stringBuilder.toString().length() > 0){
                        socket.close();
                        Log.d(LOG_TAG, "WatchSocket: close response socket - " + permissionPackage.getServerIp());
                        scanningInterface.successfulGetPermission(GsonUtils.parsePermissionPackage(stringBuilder.toString()));
                    }
                }
            }
        } catch (IOException e) {
            Log.d(LOG_TAG, "WatchSocket: IOException - " + permissionPackage.getServerIp());
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            scanningInterface.errorResponse(e);
        } catch (Exception e) {
            Log.d(LOG_TAG, "WatchSocket: Exception - " + permissionPackage.getServerIp());
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            scanningInterface.errorResponse(e);
        }
    }

}
