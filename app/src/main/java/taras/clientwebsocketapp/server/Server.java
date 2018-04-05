package taras.clientwebsocketapp.server;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
                                System.out.println("message from client: " + stringBuilder);

                                ServerManager serverManager = new ServerManager();
                                serverManager.getRequest(stringBuilder.toString());
                                String response = serverManager.returnResponse();

                                //String response = parseRequestToServer(stringBuilder.toString(), checkPackageType(stringBuilder.toString()));
                                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
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
}