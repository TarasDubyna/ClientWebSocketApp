package taras.clientwebsocketapp.client.TCPClient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Handler;

import taras.clientwebsocketapp.utils.Constants;

public class TCPClient extends Thread implements Runnable {

    private static final String LOG_TAG = TCPClient.class.getSimpleName();
    private String ipNumber, incomingMessage, messageRequest;
    BufferedReader in;
    PrintWriter out;
    private MessageCallback listener = null;
    private  boolean  mRun = false;

    /**
     * TCPClient class constructor, which is created in AsyncTasks after the button click.
     * @param messageRequest  Command passed as an argument, e.g. "shutdown -r" for restarting computer
     * @param ipNumber String retrieved from IpGetter class that is looking for ip number.
     * @param listener Callback interface object
     */

    public TCPClient(String ipNumber, String messageRequest, MessageCallback listener) {
        this.ipNumber = ipNumber;
        this.messageRequest = messageRequest;
        this.listener = listener;
    }

    /**
     * Public method for sending the message via OutputStream object.
     * @param message Message passed as an argument and sent via OutputStream object.
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
            Log.d(LOG_TAG, "Sent Message: " + message);
        }
    }

    /**
     * Public method for stopping the TCPClient object ( and finalizing it after that ) from AsyncTask
     */
    public void stopClient(){
        Log.d(LOG_TAG, "Client stopped!");
        mRun = false;
    }


    @Override
    public void run() {
        try {
            // Creating InetAddress object from ipNumber passed via constructor from IpGetter class.
            InetAddress serverAddress = InetAddress.getByName(ipNumber);

            Log.d(LOG_TAG, "Connecting...");

            /**
             * Sending empty message with static int value from MainActivity
             * to update UI ( 'Connecting...' ).
             *
             * @see com.example.turnmeoff.MainActivity.CONNECTING
             */

            /**
             * Here the socket is created with hardcoded port.
             * Also the port is given in IpGetter class.
             *
             * @see com.example.turnmeoff.IpGetter
             */
            Socket socket = new Socket(serverAddress, Constants.SERVER_PORT);


            try {

                // Create PrintWriter object for sending messages to server.
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //Create BufferedReader object for receiving messages from server.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Log.d(LOG_TAG, "In/Out created");

                //Sending message with command specified by AsyncTask
                this.sendMessage(messageRequest);

                //Listen for the incoming messages while mRun = true
                while (mRun) {
                    incomingMessage = in.readLine();
                    if (incomingMessage != null && listener != null) {
                        /**
                         * Incoming message is passed to MessageCallback object.
                         * Next it is retrieved by AsyncTask and passed to onPublishProgress method.
                         *
                         */
                        listener.callbackMessageReceiver(incomingMessage);

                    }
                    incomingMessage = null;
                }
                Log.d(LOG_TAG, "Received Message: " +incomingMessage);
            } catch (Exception e) {
                Log.d(LOG_TAG, "Error", e);
            } finally {
                out.flush();
                out.close();
                in.close();
                socket.close();
                Log.d(LOG_TAG, "Socket Closed");
            }

        } catch (Exception e) {
            Log.d(LOG_TAG, "Error", e);
        }
    }
}
