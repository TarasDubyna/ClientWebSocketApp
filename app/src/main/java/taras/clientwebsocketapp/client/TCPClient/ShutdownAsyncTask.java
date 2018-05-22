package taras.clientwebsocketapp.client.TCPClient;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class ShutdownAsyncTask extends AsyncTask<String, String, TCPClient> {
    private static final String LOG_TAG = ShutdownAsyncTask.class.getSimpleName();

    private static final String COMMAND = "shutdown -s";
    private TCPClient tcpClient;


    public ShutdownAsyncTask(){

    }


    /**
     * Overriden method from AsyncTask class. There the TCPClient object is created.
     * @param params From MainActivity class empty string is passed.
     * @return TCPClient object for closing it in onPostExecute method.
     */
    @Override
    protected TCPClient doInBackground(String... params) {
        Log.d(LOG_TAG, "In do in background");

        try{
            tcpClient = new TCPClient("192.168.1.1", COMMAND,
                    new MessageCallback() {
                        @Override
                        public void callbackMessageReceiver(String message) {
                            publishProgress(message);
                        }
                    });

        }catch (NullPointerException e){
            Log.d(LOG_TAG, "Caught null pointer exception");
            e.printStackTrace();
        }
        tcpClient.run();
        return null;
    }

    /**
     * Overriden method from AsyncTask class. Here we're checking if server answered properly.
     * @param values If "restart" message came, the client is stopped and computer should be restarted.
     *               Otherwise "wrong" message is sent and 'Error' message is shown in UI.
     */

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d(LOG_TAG, "In progress update, values: " + values.toString());
        if(values[0].equals("shutdown")){
            tcpClient.sendMessage(COMMAND);
            tcpClient.stopClient();
        }else{
            tcpClient.sendMessage("wrong");
            tcpClient.stopClient();
        }
    }

    @Override
    protected void onPostExecute(TCPClient result){
        super.onPostExecute(result);
        Log.d(LOG_TAG, "In on post execute");
        if(result != null){
            result.stopClient();
        }
    }
}
