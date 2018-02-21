package taras.clientwebsocketapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.io.File;

import taras.clientwebsocketapp.utils.DataUtils;

import static taras.clientwebsocketapp.utils.NetworkUtils.getIpNetworkAddressString;

/**
 * Created by Taras on 08.02.2018.
 */

public class AppApplication extends Application{

    private static final String LOG_TAG = "myLogs";

    public static String deviceIp;
    public static String networkIp;
    public static String deviceOs;
    public static String deviceType;
    public static File externalStorageDir;

    public static Context appContext;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();

        externalStorageDir = DataUtils.checkExternalStorage();

        Hawk.init(getContext())
                .build();

        getNetworkParams();
    }

    private void getNetworkParams(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Context context = AppApplication.appContext;
                ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                int ipAddress = connectionInfo.getIpAddress();

                AppApplication.deviceIp = Formatter.formatIpAddress(ipAddress);
                AppApplication.networkIp = getIpNetworkAddressString();
                AppApplication.deviceOs = "Android";

                Log.d(LOG_TAG,"Device IP: " + AppApplication.deviceIp);
                Log.d(LOG_TAG,"Network IP: " + AppApplication.networkIp);

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
