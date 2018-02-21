package taras.clientwebsocketapp.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Taras on 21.02.2018.
 */

public class DataUtils {
    private static final String LOG_TAG = "myLogs";
    public static File checkExternalStorage(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.i(LOG_TAG, String.valueOf(Environment.getExternalStorageDirectory()));
            return Environment.getExternalStorageDirectory();
        } else {
            Log.i(LOG_TAG, "External storage unavailable");
            return null;
        }
    }
}
