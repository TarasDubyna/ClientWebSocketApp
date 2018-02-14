package taras.clientwebsocketapp.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.AppApplication;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkUtils {

    private static final String LOG_TAG = "myLogs";

    public static String getWifiState(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    /*
    public static InetAddress getIpAddress(){
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
    */

    public static List<String> getAllNetworkAddresses(){
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i <= 255; i++){
            arrayList.add(AppApplication.networkIp + i);
        }

        return arrayList;
    }

    public static String getIpNetworkAddressString(){
        String ipAddress = AppApplication.deviceIp;
        String[] fn = ipAddress.split("\\.");
        String networkAddress = "";
        for (int i = 0; i < fn.length - 1; i++){
            System.out.println(fn[i]);
            networkAddress += (fn[i]) + ".";
        }
        Log.d(LOG_TAG, "Network IP: " + networkAddress);
        return networkAddress;
    }
}
