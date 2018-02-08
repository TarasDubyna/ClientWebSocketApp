package taras.clientwebsocketapp.Utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkUtils {

    public static String getWifiState(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }
}
