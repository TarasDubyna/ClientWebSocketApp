package taras.clientwebsocketapp.network;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import taras.clientwebsocketapp.managers.file_sender_manager.FileSenderRequestCallback;
import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.FileSendStatePackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkOperations {
    private static final String LOG_TAG = "myLogs";
    private static final int BUFFER_SIZE = 4096;

    public static void scanNetwork(String ip, RequestServiceInterface scanningInterface) {
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
                    char[] buffer = new char[BUFFER_SIZE];

                    try {
                        stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));
                    } catch (Exception ex){
                        Log.d(LOG_TAG, "Exception: " + ex.getMessage());
                    }

                    Log.d(LOG_TAG, "WatchSocket: socket get response - " + ip);
                    Log.d(LOG_TAG, "WatchSocket: response - " + stringBuilder);
                    if (stringBuilder.toString().length() > 0){
                        socket.close();
                        Log.d(LOG_TAG, "WatchSocket: close response socket - " + ip);
                        scanningInterface.successfulScanningResponse(ScannerPackage.parse(stringBuilder.toString()));
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
                scanningInterface.errorScanning(e1);
            }
            e.printStackTrace();
            scanningInterface.errorScanning(e);
        } catch (Exception e) {
            Log.d(LOG_TAG, "WatchSocket: Exception - " + ip);
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                scanningInterface.errorScanning(e1);
            }
            e.printStackTrace();
            scanningInterface.errorScanning(e);
        }
    }
    public static void getPermission(PermissionPackage permissionPackage, RequestServiceInterface scanningInterface) {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(permissionPackage.getServerIp()), Constants.SERVER_PORT);
            while (true) {
                Log.d(LOG_TAG, "WatchSocket: open socket - " + permissionPackage.getServerIp());

                // request to server
                try {
                    Log.d(LOG_TAG, "WatchSocket: send message - " + permissionPackage.getServerIp());
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(permissionPackage.toJson());
                } catch (Exception e) {}

                // check thread, rake response
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    char[] buffer = new char[BUFFER_SIZE];
                    stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));

                    Log.d(LOG_TAG, "WatchSocket: response - " + stringBuilder);
                    PermissionPackage responsePermissionPackage = PermissionPackage.parse(stringBuilder.toString());
                    Log.d(LOG_TAG, "WatchSocket: socket get response - " + responsePermissionPackage.getServerIp());

                    if (stringBuilder.toString().length() > 0){
                        socket.close();
                        Log.d(LOG_TAG, "WatchSocket: close response socket - " + responsePermissionPackage.getServerIp());
                        scanningInterface.successfulGetPermission(responsePermissionPackage);
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


    private static FileSendStatePackage fileSendStatePackage;
    public static void sendFile(FileSendPackage fileSendPackage, FileSenderRequestCallback fileSenderRequestCallback){
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(fileSendPackage.getServerIp()), Constants.SERVER_PORT);
            while (true) {
                Log.d(LOG_TAG, "WatchSocket: open socket - " + fileSendPackage.getServerIp());

                // request to server
                try {
                    Log.d(LOG_TAG, "WatchSocket: send message - " + fileSendPackage.getServerIp());
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(fileSendPackage.toJson());
                } catch (Exception e) {}

                // check thread, rake response
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());

                    char[] buffer = new char[BUFFER_SIZE];
                    stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));

                    Log.d(LOG_TAG, "WatchSocket: response - " + stringBuilder);
                    fileSendStatePackage = FileSendStatePackage.parse(stringBuilder.toString());
                    Log.d(LOG_TAG, "WatchSocket: socket get response - " + fileSendStatePackage.getServerIp());

                    if (stringBuilder.toString().length() > 0){
                        socket.close();
                        Log.d(LOG_TAG, "WatchSocket: close response socket - " + fileSendStatePackage.getServerIp());
                        fileSenderRequestCallback.getFileSendResponse(fileSendStatePackage);
                    }
                }
            }
        } catch (IOException e) {
            Log.d(LOG_TAG, "WatchSocket: IOException - " + fileSendPackage.getServerIp());
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            fileSenderRequestCallback.errorRequest(fileSendStatePackage, e);
        } catch (Exception e) {
            Log.d(LOG_TAG, "WatchSocket: Exception - " + fileSendPackage.getServerIp());
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            fileSenderRequestCallback.errorRequest(fileSendStatePackage, e);
        }
    }

}
