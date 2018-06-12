package taras.clientwebsocketapp.managers.file_getter_manager;

import android.app.NotificationManager;
import android.icu.util.RangeValueIterator;
import android.util.Log;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import taras.clientwebsocketapp.managers.NotificationsManager;
import taras.clientwebsocketapp.model.FileSendPackage;

public class FileGetterManager {

    private static final String LOG_TAG = FileGetterManager.class.getSimpleName();

    private static FileGetterManager fileGetterManager;

    private List<List<FileSendPackage>> gettedFileSendPackages;
    private HashMap<String, Integer> notificationMap;

    private final Object lock = new Object();

    public static FileGetterManager getFileGetterManager(){
        if (fileGetterManager == null){
            fileGetterManager = new FileGetterManager();
        }
        return fileGetterManager;
    }

    public FileGetterManager() {
        this.gettedFileSendPackages = new ArrayList<>();
        this.notificationMap = new HashMap<>();
    }

    public void addGettedFileSandPackage(FileSendPackage fileSendPackage){
        synchronized (lock){
            if (!isFileExist(fileSendPackage)){
                createNewFileList(fileSendPackage);
            } else {
                for (List<FileSendPackage> fileList : gettedFileSendPackages){
                    if (compareFileSendPackagesByFileName(fileList.get(0), fileSendPackage)){
                        addFilePackage(fileList, fileSendPackage);
                    }
                }
            }
        }
    }

    private boolean isFileExist(FileSendPackage fileSendPackage){
        if (gettedFileSendPackages.size() == 0){
            return false;
        } else {
            for (List<FileSendPackage> filePackagesList : gettedFileSendPackages){
                for (FileSendPackage pack : filePackagesList){
                    if (compareFileSendPackagesByFileName(pack, fileSendPackage)){
                        return true;
                    }
                }
            }
            return false;
        }
    }
    private void createNewFileList(FileSendPackage fileSendPackage){
        List<FileSendPackage> list = new ArrayList<FileSendPackage>(fileSendPackage.getAllPart() + 1);

        list.add(fileSendPackage);
        this.gettedFileSendPackages.add(list);
        Log.d(LOG_TAG, "FileGetterManager, createNewFileList, addFilePackage, current size: " + list.size() + " , all size: " + fileSendPackage.getAllPart());

        notificationMap.put(fileSendPackage.getFileName(), notificationMap.size() + 1);
        NotificationsManager.createFileGetNotification(notificationMap.get(fileSendPackage.getFileName()), fileSendPackage.getFileName());
    }
    private void addFilePackage(List<FileSendPackage> fileList, FileSendPackage fileSendPackage){
        fileList.add(fileSendPackage);
        Log.d(LOG_TAG, "FileGetterManager, addFilePackage, current size: " + fileList.size() + " , all size: " + fileSendPackage.getAllPart());
        if (fileList.size() == fileSendPackage.getAllPart()){
            String[] parts = fileSendPackage.getFileName().split("/");
            NotificationsManager.finishFileGetNotification(notificationMap.get(fileSendPackage.getFileName()), fileSendPackage.getClientName(), parts[parts.length - 1]);
        } else {
            NotificationsManager.updateFileGetNotification(notificationMap.get(fileSendPackage.getFileName()), getDownloadProgress(fileList));
        }
    }

    private List<FileSendPackage> getFileList(FileSendPackage fileSendPackage){
        for (List<FileSendPackage> fileSendPackageList: gettedFileSendPackages){
            for (FileSendPackage pack: fileSendPackageList){
                if (compareFileSendPackagesByFileName(pack, fileSendPackage)){
                    return fileSendPackageList;
                }
            }
        }
        return new ArrayList<FileSendPackage>();
    }

    private boolean checkIsFileDownloadedFull(FileSendPackage fileSendPackage){
        long fileCounter = 0;
        List<FileSendPackage> list = getFileList(fileSendPackage);
        for (FileSendPackage pack: list){
            if (pack != null){
                fileCounter++;
            }
        }
        if (fileCounter == fileSendPackage.getAllPart()){
            return true;
        } else {
            return false;
        }
    }


    private void addFile(FileSendPackage fileSendPackage){
        for (List<FileSendPackage> fileSendPackageList: gettedFileSendPackages){
            if (compareFileSendPackagesByFileName(fileSendPackageList.get(0), fileSendPackage)){
                fileSendPackageList.add(fileSendPackage);
                break;
            }
        }
    }

    private boolean compareFileSendPackagesByFileName(FileSendPackage fileSendPackageCompared, FileSendPackage fileSendPackage){
        if (fileSendPackageCompared.getToken().equals(fileSendPackage.getToken())
                && fileSendPackageCompared.getFileName().equals(fileSendPackage.getFileName()))
            return true;
        else return false;
    }
    private boolean compareFileSendPackagesByCurrentPart(FileSendPackage fileSendPackageCompared, FileSendPackage fileSendPackage){
        if (fileSendPackageCompared.getToken().equals(fileSendPackage.getToken())
                && fileSendPackageCompared.getFileName().equals(fileSendPackage.getFileName())
                && fileSendPackageCompared.getCurrentPart() == fileSendPackage.getCurrentPart())
            return true;
       else return false;
    }


    private int getDownloadProgress(List<FileSendPackage> fileList){
        long downloadedPackages = 0;
        for (int i = 0; i < fileList.size(); i++){
            if (fileList.get(i) != null){
                downloadedPackages++;
            }
        }

        int percent = (int) (downloadedPackages/fileList.size() * 100);

        return percent;
    }



}
