package taras.clientwebsocketapp.manager.back_press_manager;

import android.app.Activity;

import taras.clientwebsocketapp.screens.file_manager.FileManagerFragment;
import taras.clientwebsocketapp.screens.file_manager.FileManagerInterface;

/**
 * Created by Taras on 20.02.2018.
 */

public class BackPressManager {

    private static final String SCAN_NETWORK_FRAGMENT = "SCAN_NETWORK_FRAGMENT";
    private static final String FILE_MANAGER_FRAGMENT = "FILE_MANAGER_FRAGMENT";


    private BackPressManagerInterface backPressManagerInterface;
    private FileManagerInterface fileManagerInterface;

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

    public BackPressManager setFileManagerInterface(FileManagerInterface fileManagerInterface){
        this.fileManagerInterface = fileManagerInterface;
        return this;
    }


    public void checkCurrentFragment(String currentFragmentClass){
        switch (currentFragmentClass){
            case SCAN_NETWORK_FRAGMENT:
                break;
            case FILE_MANAGER_FRAGMENT:
                if (FileManagerFragment.getFragment().getDirectoryList().size() > 1){
                }
                break;
        }
    }
}
