package taras.clientwebsocketapp.managers.file_sender_manager;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.FileSendStatePackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.network.NetworkConnection;
import taras.clientwebsocketapp.network.callbacks.FileSenderRequestCallback;
import taras.clientwebsocketapp.utils.PreferenceUtils;

public class FileSenderManager {

    private static final String LOG_TAG = FileSenderManager.class.getSimpleName();

    private static FileSenderManager fileSenderManager;

    private String STORAGE_FILE_DIRECTORY = PreferenceUtils.getLocalStorageDirection();
    private static final int CHUNK_SIZE = 2000;

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

    private void sendFilePackages(List<FileSendPackage> fileSendPackageList){
        addToFilesForSendingList(fileSendPackageList);
        for (int i = 0; i < fileSendPackageList.size(); i++){
            try {
                NetworkConnection.getConnectionRepository().sendFilePackage(fileSenderRequestCallback, fileSendPackageList.get(i));
            } catch (IOException e) {
                Log.d(LOG_TAG, "sendFilePackages, error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    FilePreparatorCallback filePreparatorCallback = new FilePreparatorCallback() {
        @Override
        public void getFileForSendList(List<FileSendPackage> fileSendPackageList) {
            //todo send file
            sendFilePackages(fileSendPackageList);
        }
    };
    FileSenderRequestCallback fileSenderRequestCallback = new FileSenderRequestCallback() {
        @Override
        public void getFileSendResponse(FileSendStatePackage fileSendStatePackage) {
            Log.d(LOG_TAG, "Sucsessful send filePackage, fileName: " + fileSendStatePackage.getFileName()
                    + " , part: " + fileSendStatePackage.getCurrentPart()
                    + " , all part: " + fileSendStatePackage.getAllPart());
            removeFromFilesForSendingList(fileSendStatePackage);
        }

        @Override
        public void errorRequest(FileSendStatePackage fileSendStatePackage, Throwable throwable) {
            Log.d(LOG_TAG, "Error send file package");
            /*FileSendPackage fileSendPackage = getFileSendPackageByState(fileSendStatePackage);
            if (fileSendPackage != null){
                try {
                    NetworkConnection.getConnectionRepository().sendFilePackage(fileSenderRequestCallback, fileSendPackage);
                } catch (IOException e) {
                    Log.d(LOG_TAG, "sendFilePackages, error: " + e.getMessage());
                    e.printStackTrace();
                }
            }*/

        }
    };



    private void addToFilesForSendingList(List<FileSendPackage> fileSendPackageList){
        synchronized (lock){
            this.filesForSendingList.add(fileSendPackageList);
            Log.d(LOG_TAG, "Added to filesForSendingList dropped file: " + fileSendPackageList.get(0).getFileName());
        }
    }
    private void removeFromFilesForSendingList(FileSendStatePackage fileSendStatePackage){
        synchronized (lock){
            for (int i = 0; i < filesForSendingList.size(); i++){
                if (filesForSendingList.get(i).get(0).getToken().equals(fileSendStatePackage.getToken())){
                    for (int j = 0; j < filesForSendingList.get(i).size(); j++){
                        if (filesForSendingList.get(i).get(j).getFileName().equals(fileSendStatePackage.getFileName()) &&
                                filesForSendingList.get(i).get(j).getCurrentPart() == fileSendStatePackage.getCurrentPart()){
                            filesForSendingList.get(i).remove(j);
                            Log.d(LOG_TAG, "Remove Package: " + i + ", " + j);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
    private FileSendPackage getFileSendPackageByState(FileSendStatePackage fileSendStatePackage){
        for (int i = 0; i < filesForSendingList.size(); i++){
            for (int j = 0; j < filesForSendingList.get(i).size(); j++){
                if (compareFileSendPackagesByFileName(filesForSendingList.get(i).get(j), fileSendStatePackage)){
                    if (compareFileSendPackagesByCurrentPart(filesForSendingList.get(i).get(j), fileSendStatePackage)){
                        filesForSendingList.get(i).remove(j);
                        return filesForSendingList.get(i).get(j);
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
/*


        for (int i = 0; i < filesForSendingList.size(); i++){
            if (filesForSendingList.get(i).get(0).getToken().equals(fileSendStatePackage.getToken())){
                for (int j = 0; j < filesForSendingList.get(i).size(); j++){
                    if (filesForSendingList.get(i).get(j).getFileName().equals(fileSendStatePackage.getFileName()) &&
                            filesForSendingList.get(i).get(j).getCurrentPart() == fileSendStatePackage.getCurrentPart()){
                        filesForSendingList.get(i).remove(j);
                        return filesForSendingList.get(i).get(j);
                    }
                }
                break;
            }
        }
        return null;*/
    }

    private boolean compareFileSendPackagesByFileName(FileSendPackage fileSendPackageCompared, FileSendStatePackage fileSendPackage){
        if (fileSendPackageCompared != null){
            if (fileSendPackageCompared.getToken().equals(fileSendPackage.getToken())
                    && fileSendPackageCompared.getFileName().equals(fileSendPackage.getFileName()))
                return true;
            else return false;
        } else {
            return false;
        }
    }
    private boolean compareFileSendPackagesByCurrentPart(FileSendPackage fileSendPackageCompared, FileSendStatePackage fileSendPackage){
        if (fileSendPackageCompared.getToken().equals(fileSendPackage.getToken())
                && fileSendPackageCompared.getFileName().equals(fileSendPackage.getFileName())
                && fileSendPackageCompared.getCurrentPart() == fileSendPackage.getCurrentPart())
            return true;
        else return false;
    }

}
