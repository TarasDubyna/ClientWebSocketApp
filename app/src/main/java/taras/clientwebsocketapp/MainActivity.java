package taras.clientwebsocketapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.nsd.NsdManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.Observable;

import taras.clientwebsocketapp.clientService.NotificationService;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "myLogs";

    private String SERVICE_NAME = "Client Device";
    private String SERVICE_TYPE = "_http._tcp.";

    private int SocketServerPort = 8080;
    private static final String REQUEST_CONNECT_CLIENT = "request-connect-client";

    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;


    EditText etAddress;
    EditText etHost;
    EditText etMessage;
    Button btnConnect;
    Button btnScann;

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            String responseData = arg1.getStringExtra("string");

            Toast.makeText(MainActivity.this,"Message: " + responseData, Toast.LENGTH_SHORT).show();
        }
    }

    boolean bound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(LOG_TAG, "MainActivity onServiceConnected");
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
            bound = false;
        }
    };

    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Log.d(LOG_TAG, "Device OS: " + System.getProperty("os.name"));


        etMessage = findViewById(R.id.etMessage);
        etMessage.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        etAddress = findViewById(R.id.etAddress);
        etHost = findViewById(R.id.etHost);
        btnConnect = findViewById(R.id.btnConnect);
        btnScann = findViewById(R.id.btnScann);


        btnScann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myReceiver == null){
                    myReceiver = new MyReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(NotificationService.MY_ACTION);
                    registerReceiver(myReceiver, intentFilter);
                }

                Log.d("myLogs", "btnScann");
                Intent intent = new Intent(MainActivity.this, NotificationService.class);
                intent.putExtra(NotificationService.TYPE, NotificationService.SCAN_NETWORK);
                intent.putExtra("ip", etAddress.getText().toString());
                startService(intent);
            }
        });


        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myReceiver == null){
                    myReceiver = new MyReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(NotificationService.MY_ACTION);
                    registerReceiver(myReceiver, intentFilter);
                }

                Log.d("myLogs", "btnConnect");
                Intent intent = new Intent(MainActivity.this, NotificationService.class);
                intent.putExtra(NotificationService.TYPE, NotificationService.SEND_MESSAGE);
                intent.putExtra("ip", etAddress.getText().toString());
                intent.putExtra("message", etMessage.getText().toString());
                startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
