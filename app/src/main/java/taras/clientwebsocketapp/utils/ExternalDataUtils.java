package taras.clientwebsocketapp.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taras on 21.02.2018.
 */

public class ExternalDataUtils {
    private static final String LOG_TAG = "myLogs";


    public static File getPhoneExternalStorage(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.i(LOG_TAG, "Phone external: " + String.valueOf(Environment.getExternalStorageDirectory()));
            return Environment.getExternalStorageDirectory();
        } else {
            Log.i(LOG_TAG, "External storage unavailable");
            return null;
        }
    }
    public static void getExtSDCardPath() {
        File externalStorage = Environment.getExternalStorageDirectory();
        File sdf = Environment.getDataDirectory();
        File root = Environment.getRootDirectory();
        String file2 = Environment.getExternalStorageState();
        Log.d(LOG_TAG, "getExtSDCardPath: ");
    }


    public static String getCardExternalStorage() {
        String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String[] paths = internalPath.split("/");
        String parentPath = "/";
        for (String s : paths) {
            if (s.trim().length() > 0) {
                parentPath = parentPath.concat(s);
                break;
            }
        }
        File parent = new File(parentPath);
        if (parent.exists()) {
            File[] files = parent.listFiles();
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                Log.d(LOG_TAG, filePath);
                if (filePath.equals(internalPath)) {
                    continue;
                } else if (filePath.toLowerCase().contains("sdcard")) {
                    return filePath;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        if (Environment.isExternalStorageRemovable(file)) {
                            Log.e(LOG_TAG, "Card path: " + filePath);
                            return filePath;
                        }
                    } catch (RuntimeException e) {
                        Log.e(LOG_TAG, "RuntimeException: " + e);
                    }
                }
            }

        }
        return null;
    }

    public static boolean checkSdCard(){
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        if(isSDSupportedDevice && isSDPresent) {
            return true;
        } else {
            return false;
        }
    }
}
