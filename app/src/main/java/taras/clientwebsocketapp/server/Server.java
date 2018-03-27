package taras.clientwebsocketapp.server;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import taras.clientwebsocketapp.BackgroundService;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;

public class Server {
    private static final String LOG_TAG = "myLogs";

    private ServerSocket serverSocket;
    private Thread socketServerThread;
    private boolean serverState = false;

    private static Server server;
    private Handler uiHandler;


    public Server(Handler handler) {
        this.uiHandler = handler;
    }

    public static Server getInstance(Handler handler){
        if (server == null){
            server = new Server(handler);
        }
        return server;
    }

    public void startServer(){
        socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
        serverState = true;
    }

    public void stopServer(){
        serverState = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {
        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
                serverSocket = new ServerSocket(Constants.SERVER_PORT);
                while (serverState) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            InputStreamReader inputStreamReader = null;
                            try {
                                inputStreamReader = new InputStreamReader(socket.getInputStream());
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                                StringBuilder stringBuilder = new StringBuilder();
                                char[] buffer = new char[4096];
                                stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));
                                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                                System.out.println("message from client: " + stringBuilder);

                                uiHandler.obtainMessage(1, new Object());

                                String response = parseRequestToServer(stringBuilder.toString(), checkPackageType(stringBuilder.toString()));
                                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                                //outputStreamWriter.write("Message from server 192.168.1.133");
                                outputStreamWriter.write(response);
                                outputStreamWriter.flush();
                                outputStreamWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String parseRequestToServer(String jsonString, String type){
        ServerResponse serverResponse = new ServerResponse();
        switch (type){
            case Constants.PACKAGE_TYPE_SCANNING:
                return serverResponse.createScanningNetworkResponse(GsonUtils.parseScannerPackage(jsonString));
            case Constants.PACKAGE_TYPE_PERMISSION:
                Log.d(LOG_TAG, "PACKAGE_TYPE_PERMISSION");
                uiHandler.obtainMessage(1, new Object());

                return serverResponse.createPermissionResponse(GsonUtils.parsePermissionPackage(jsonString));
        }
        return null;
    }
    private String checkPackageType (String stringRequest){
        try {
            JSONObject jsonObject = new JSONObject(stringRequest);
            Log.d(LOG_TAG, "Server stringRequest: " + stringRequest);
            String type = jsonObject.getString("type");
            return type;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



}