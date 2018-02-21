package taras.clientwebsocketapp.manager.back_press_manager;

import android.app.Activity;
import android.util.Log;

import taras.clientwebsocketapp.manager.FileManager;
import taras.clientwebsocketapp.screens.file_manager.FileManagerFragment;
import taras.clientwebsocketapp.screens.file_manager.FileManagerInterface;

/**
 * Created by Taras on 20.02.2018.
 */

public class BackPressManager {
    private static final String LOG_TAG = "myLogs";

    public static final String SCAN_NETWORK_FRAGMENT = "SCAN_NETWORK_FRAGMENT";
    public static final String FILE_MANAGER_FRAGMENT = "FILE_MANAGER_FRAGMENT";


    private BackPressManagerInterface backPressManagerInterface;

    private Activity mActivity;

    private static BackPressManager backPressManager;
    public static BackPressManager getBackPressManager(Activity activity){
        if (backPressManager == null){
            backPressManager = new BackPressManager(activity);
        }
        return backPressManager;
    }

    public BackPressManager(Activity activity) {
        this.mActivity = activity;
    }

    public BackPressManager setBackPressManagerInterface(BackPressManagerInterface backPressManagerInterface){
        this.backPressManagerInterface = backPressManagerInterface;
        return this;
    }


    public BackPressManager checkCurrentFragment(String currentFragmentClass){
        Log.d(LOG_TAG, "BackPressManager, currentFragmentClass: " + currentFragmentClass);
        switch (currentFragmentClass){
            case SCAN_NETWORK_FRAGMENT:
                break;
            case FILE_MANAGER_FRAGMENT:
                int directoryListSize = FileManager.getManager(mActivity).getDirectoryList().size();
                if (directoryListSize > 1){
                 FileManager.getManager(mActivity).wentToPreviousFolder(directoryListSize - 1);
                }
                break;
        }
        return this;
    }
}
