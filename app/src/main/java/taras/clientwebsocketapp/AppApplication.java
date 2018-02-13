package taras.clientwebsocketapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import taras.clientwebsocketapp.Utils.NetworkUtils;

import static taras.clientwebsocketapp.Utils.NetworkUtils.getIpNetworkAddressString;

/**
 * Created by Taras on 08.02.2018.
 */

public class AppApplication extends Application{

    private static final String LOG_TAG = "myLogs";

    public static String deviceIp;
    public static String networkIp;
    public static String deviceMac;
    public static String deviceType;

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getBaseContext();
        getNetworkParams();

    }

    private void getNetworkParams(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Context context = AppApplication.appContext;
                ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                int ipAddress = connectionInfo.getIpAddress();
                String mac = wifiManager.getConnectionInfo().getMacAddress();

                AppApplication.deviceIp = Formatter.formatIpAddress(ipAddress);
                AppApplication.networkIp = getIpNetworkAddressString();
                AppApplication.deviceMac = mac;


                Log.d(LOG_TAG,"Device IP: " + AppApplication.deviceIp);
                Log.d(LOG_TAG,"Network IP: " + AppApplication.networkIp);
                Log.d(LOG_TAG,"Device MAC: " + AppApplication.deviceMac);
            }
        }).start();
    }

    public Context getContext() {
        return appContext;
    }
    public void setContext(Context context) {
        this.appContext = context;
    }
}
