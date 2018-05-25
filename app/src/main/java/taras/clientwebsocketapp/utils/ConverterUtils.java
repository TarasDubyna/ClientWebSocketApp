package taras.clientwebsocketapp.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ConverterUtils {

    public static String getIpEnd(String fullIp){
        String[] words = fullIp.split("\\.");
        return words[words.length - 1];
    }
    public static String getFileNameFromDirectory(String fileDirectory){
        String[] array = fileDirectory.split("/+");
        return array[array.length - 1];
    }

    public static float convertDpToPx(float dps) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, metrics);
    }

    public static int convertDpToPx(int dps) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, metrics));
    }
}
