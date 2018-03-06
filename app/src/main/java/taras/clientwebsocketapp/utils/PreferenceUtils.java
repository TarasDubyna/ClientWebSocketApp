package taras.clientwebsocketapp.utils;

import android.support.annotation.NonNull;

import com.orhanobut.hawk.Hawk;

/**
 * Created by Taras on 14.02.2018.
 */

public final class PreferenceUtils {

    private static final String DEVICE_NAME = "device_name";
    private static final String SERVER_RUNNING = "server_running";
    private static final String SAVING_FOLDER = "saving_folder";


    public static void saveDeviceName(@NonNull String daviceName) {
        Hawk.put(DEVICE_NAME, daviceName);
    }
    @NonNull
    public static String getDeviceName() {
        return Hawk.get(DEVICE_NAME, "");
    }

    public static void saveServerState(@NonNull boolean isRunning) {
        Hawk.put(SERVER_RUNNING, isRunning);
    }
    @NonNull
    public static boolean isServerRunning() {
        return Hawk.get(SERVER_RUNNING, false);
    }


    public static void saveSavingFolderPath(@NonNull String path){
        Hawk.put(SAVING_FOLDER, path);
    }
    @NonNull
    public static String getSavingFolderPath() {
        return Hawk.get(SAVING_FOLDER, "");
    }
    @NonNull
    public static boolean isSavingFolderPathCreated() {
        String savingFolderPath = Hawk.get(SAVING_FOLDER, "");
        if (!savingFolderPath.equals("")){
            return true;
        } else {
            return false;
        }
    }
}
