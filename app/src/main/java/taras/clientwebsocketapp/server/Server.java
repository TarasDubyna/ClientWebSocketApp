package taras.clientwebsocketapp.server;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import taras.clientwebsocketapp.client.TCPClient.ShutdownAsyncTask;
import taras.clientwebsocketapp.managers.NotificationsManager;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.ConstatsLogTag;

public class Server {
    private ServerSocket serverSocket;
    private Thread socketServerThread;
    private boolean serverState = false;

    private static Server server;

    private Handler mainHandler;
    private Context context;





    public Server(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public static Server getInstance(Handler mainHandler, Context context){
        if (server == null){
            server = new Server(mainHandler);
        }
        return server;
    }

    public void startServer(){
        socketServerThread = new Thread(new SocketServerThread());
        serverState = true;
        socketServerThread.start();
        Log.d(ConstatsLogTag.Server, "server started");

    }

    public void stopServer(){
        try {
            NotificationsManager.removeServerStatusNotification();
            serverState = false;
            serverSocket.close();
            Log.d(ConstatsLogTag.Server, "server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){
            //PreferenceUtils.saveRunningServerState(false);
        }
    }

    public boolean isServerIsRun(){
        Log.d(ConstatsLogTag.Server, "serverState: " + serverState);
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
                                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                                ServerManager serverManager = new ServerManager(mainHandler, context);

                                String stringRequest = getRequestFromClient(inputStreamReader);
                                Log.d(ConstatsLogTag.Server, "Request from client: " + stringRequest);
                                String stringResponse = serverManager.returnResponse(stringRequest);
                                Log.d(ConstatsLogTag.Server, "Response to client: " + stringResponse);

                                sendResponse(outputStreamWriter, stringResponse);
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
            //stringBuilder.append(inputStreamReader.read());
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