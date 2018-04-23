package taras.clientwebsocketapp.network;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import taras.clientwebsocketapp.model.PermissionPackageFirst;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkOperations {
    private static final String LOG_TAG = "myLogs";

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
                    char[] buffer = new char[4096];

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

    public static void getPermission(PermissionPackageFirst permissionPackageFirst, RequestServiceInterface scanningInterface) {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(permissionPackageFirst.getServerIp()), Constants.SERVER_PORT);
            while (true) {
                Log.d(LOG_TAG, "WatchSocket: open socket - " + permissionPackageFirst.getServerIp());

                // request to server
                try {
                    Log.d(LOG_TAG, "WatchSocket: send message - " + permissionPackageFirst.getServerIp());
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(permissionPackageFirst.toJson());
                } catch (Exception e) {}

                // check thread, rake response
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    char[] buffer = new char[4096];
                    stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));
                    Log.d(LOG_TAG, "WatchSocket: socket get response - " + permissionPackageFirst.getServerIp());
                    Log.d(LOG_TAG, "WatchSocket: response - " + stringBuilder);
                    if (stringBuilder.toString().length() > 0){
                        socket.close();
                        Log.d(LOG_TAG, "WatchSocket: close response socket - " + permissionPackageFirst.getServerIp());
                        scanningInterface.successfulGetPermissionFirstStage(permissionPackageFirst);
                    }
                }
            }
        } catch (IOException e) {
            Log.d(LOG_TAG, "WatchSocket: IOException - " + permissionPackageFirst.getServerIp());
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
            Log.d(LOG_TAG, "WatchSocket: Exception - " + permissionPackageFirst.getServerIp());
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


    /*
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

*/

}
