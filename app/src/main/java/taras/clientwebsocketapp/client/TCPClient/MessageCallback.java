package taras.clientwebsocketapp.client.TCPClient;

public interface MessageCallback {
    /**
     * Method overriden in AsyncTask 'doInBackground' method while creating the TCPClient object.
     * @param message Received message from server app.
     */
    public void callbackMessageReceiver(String message);
}
