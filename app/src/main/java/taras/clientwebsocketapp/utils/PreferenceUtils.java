package taras.clientwebsocketapp.utils;

import android.support.annotation.NonNull;

import com.orhanobut.hawk.Hawk;

/**
 * Created by Taras on 14.02.2018.
 */

public final class PreferenceUtils {

    private static final String DEVICE_NAME = "device_name";
    private static final String SAVING_FOLDER = "saving_folder";
    private static final String LOCAL_STORAGE_DIRECTION = "local_storage_direction";
    private static final String SD_STORAGE_DIRECTION = "sd_storage_direction";


    public static void saveDeviceName(@NonNull String daviceName) {
        Hawk.put(DEVICE_NAME, daviceName);
    }
    public static void saveSavingFolderPath(@NonNull String path){
        Hawk.put(SAVING_FOLDER, path);
    }
    public static void saveLocalStorageDirection(@NonNull String path){
        Hawk.put(LOCAL_STORAGE_DIRECTION, path);
    }
    public static void saveSDStorageDirection(@NonNull String path){
        Hawk.put(SD_STORAGE_DIRECTION, path);
    }



    //----------------------------------------------------------------------------------------------

    @NonNull
    public static String getSavingFolderPath() {
        return Hawk.get(SAVING_FOLDER, "");
    }
    @NonNull
    public static String getLocalStorageDirection() {
        return Hawk.get(LOCAL_STORAGE_DIRECTION, "");
    }
    @NonNull
    public static String getDeviceName() {
        return Hawk.get(DEVICE_NAME, "");
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
    @NonNull
    public static String getSDStorageDirection() {
        return Hawk.get(SD_STORAGE_DIRECTION, null);
    }
}
