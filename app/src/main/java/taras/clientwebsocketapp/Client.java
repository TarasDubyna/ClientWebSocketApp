package taras.clientwebsocketapp;

import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Taras on 31.01.2018.
 */

public class Client extends AsyncTask<Void, Void, String> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;

    Client(String addr, int port, TextView textResponse) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
    }

    @Override
    protected String doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);

            String toSend = "Message from client to server";
            //Writer out = new BufferedWriter(new OutputStreamWriter(toSend, "UTF8"));

            // append and flush in logical chunks
            //out.append(toSend).append("\n");
            //out.append("appending more before flushing").append("\n");
            //out.flush();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

			/*
             * notice: inputStream.read() will block if no data return
			 */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        textResponse.setText(response);
        super.onPostExecute(result);
    }


    private void sendToServer(Socket socket){
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("Hello from Client, It is Response!");
            dataOutputStream.flush();
        } catch (IOException e) {
            Toast.makeText(textResponse.getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}