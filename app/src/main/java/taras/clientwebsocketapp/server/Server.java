package taras.clientwebsocketapp.server;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GsonUtils;

public class Server {
    private static final String LOG_TAG = "myLogs";

    private ServerSocket serverSocket;
    private Thread socketServerThread;
    private boolean serverState = false;

    private static Server server;


    public Server() {
    }

    public static Server getInstance(){
        if (server == null){
            server = new Server();
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

                                String response = parseRequestToServer(stringBuilder.toString(), checkPackageType(stringBuilder.toString()));

                                System.out.println("message from client: " + stringBuilder);

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