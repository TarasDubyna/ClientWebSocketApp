package taras.clientwebsocketapp.utils;

import android.support.annotation.NonNull;

import com.orhanobut.hawk.Hawk;

/**
 * Created by Taras on 14.02.2018.
 */

public final class PreferenceUtils {

    private static final String DEVICE_NAME = "device_name";

    public static void saveDeviceName(@NonNull String daviceName) {
        Hawk.put(DEVICE_NAME, daviceName);
    }

    @NonNull
    public static String getDeviceName() {
        return Hawk.get(DEVICE_NAME, "");
    }
}
