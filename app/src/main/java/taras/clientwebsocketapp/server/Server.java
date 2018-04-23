package taras.clientwebsocketapp.server;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import taras.clientwebsocketapp.utils.Constants;

public class Server {
    private static final String LOG_TAG = "myLogs";

    private ServerSocket serverSocket;
    private Thread socketServerThread;
    private boolean serverState = false;

    private static Server server;

    private Handler mainHandler;


    public Server(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public static Server getInstance(Handler mainHandler){
        if (server == null){
            server = new Server(mainHandler);
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

    public boolean isServerIsRun(){
        Log.d(LOG_TAG, "serverState: " + serverState);
        //socketServerThread.getState().toString();
        return serverState;
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
                            try {
                                Log.d(LOG_TAG, "server listen");
                                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                                ServerManager serverManager = new ServerManager(mainHandler);
                                serverManager.getRequest(getRequestFromClient(inputStreamReader));

                                sendResponse(outputStreamWriter, serverManager.returnResponse());
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

        private String getRequestFromClient(InputStreamReader inputStreamReader) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[4096];
            stringBuilder.append(buffer, 0, inputStreamReader.read(buffer));
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }

        private void sendResponse(OutputStreamWriter outputStreamWriter, String response) throws IOException {
            outputStreamWriter.write(response);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }

    }
}