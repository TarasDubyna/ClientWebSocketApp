package taras.clientwebsocketapp.managers.file_sender_manager;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.PreferenceUtils;

public class FileSenderManager {

    private static final String LOG_TAG = FileSenderManager.class.getSimpleName();

    private static FileSenderManager fileSenderManager;

    private String STORAGE_FILE_DIRECTORY = PreferenceUtils.getLocalStorageDirection();
    private static final int CHUNK_SIZE = 4000;

    private List<List<FileSendPackage>> filesForSendingList;


    private final Object lock = new Object();

    public static FileSenderManager getFileSenderManager() {
        if (fileSenderManager == null){
            fileSenderManager = new FileSenderManager();
        }
        return fileSenderManager;

    }

    public FileSenderManager() {
        this.filesForSendingList = new ArrayList<>();
    }

    public void addFileToSend(PermissionPackage permissionPackage){
        for (String fileName: permissionPackage.getFilesName()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FilePreparator filePreparator = new FilePreparator();
                    filePreparator.addFileName(fileName)
                            .setChunkSize(CHUNK_SIZE)
                            .getFileForSendList(permissionPackage, filePreparatorCallback);
                }
            }).start();
        }
    }



    FilePreparatorCallback filePreparatorCallback = new FilePreparatorCallback() {
        @Override
        public void getFileForSendList(List<FileSendPackage> fileSendPackageList) {
            //todo send file
        }
    };




    public void addToFilesForSendingList(List<FileSendPackage> fileSendPackageList){
        synchronized (lock){
            this.filesForSendingList.add(fileSendPackageList);
            Log.d(LOG_TAG, "Added to filesForSendingList dropped file: " + fileSendPackageList.get(0).getFileName());
        }
    }





}
