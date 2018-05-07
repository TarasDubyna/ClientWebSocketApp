package taras.clientwebsocketapp.managers.file_sender_manager;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.PreferenceUtils;

public class FileSenderManager {

    private static final String LOG_TAG = FileSenderManager.class.getSimpleName();

    private static FileSenderManager fileSenderManager;

    private String STORAGE_FILE_DIRECTORY = PreferenceUtils.getLocalStorageDirection();
    private static final int CHUNK_SIZE = 256000;

    private List<List<FileSendPackage>> lists;


    public static FileSenderManager getFileSenderManager() {
        if (fileSenderManager == null){
            fileSenderManager = new FileSenderManager();
        }
        return fileSenderManager;
    }

    public FileSenderManager() {
        this.lists = new ArrayList<>();
    }

    public void addFileToSend(String fileName){
        this.lists.add();
    }


    private byte[] convertFileToByteArray(File file) throws IOException {
        byte[] byteFile = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(byteFile);
        Log.d(LOG_TAG, byteFile.toString());
        return byteFile;
    }

    private void saveByteArrayInFile(byte[] fileByteArray, String fileName) throws IOException {
        String newFileDirectory = STORAGE_FILE_DIRECTORY + fileName;
        Log.d(LOG_TAG, "newFileDirectory: " + newFileDirectory);
        try (FileOutputStream fileOuputStream = new FileOutputStream(newFileDirectory)) {
            fileOuputStream.write(fileByteArray);
            Log.d(LOG_TAG, "newFile: " + newFileDirectory + " write successful");
        } catch (IOException e) {
            Log.d(LOG_TAG, "newFile: " + newFileDirectory + " write error");
            e.printStackTrace();
        }
    }


    private List<byte[]> splitByteArray (byte[] bytesArray){
        int len = bytesArray.length;
        List<byte[]> splittedBytesArray = new ArrayList<>();

        for (int i = 0; i < len - CHUNK_SIZE + 1; i += CHUNK_SIZE){
            splittedBytesArray.add(Arrays.copyOfRange(bytesArray, i, i + CHUNK_SIZE));
        }
        if (len % CHUNK_SIZE != 0)
            splittedBytesArray.add(Arrays.copyOfRange(bytesArray, len - len % CHUNK_SIZE, len));

        return splittedBytesArray;
    }

    private List<FileSendPackage> createFileSendPackages(List<byte[]> splittedBytesArray, PermissionPackage permissionPackage){
        int size = splittedBytesArray.size();
        List<FileSendPackage> fileSendPackageList = new ArrayList<>();

        for (int i = 0; i < size; i++){
            FileSendPackage fileSendPackage = new FileSendPackage();
            fileSendPackage.fillAfterPermission(permissionPackage);
            fileSendPackage.createToSend(i, size, splittedBytesArray.get(i));
            fileSendPackageList.add(fileSendPackage);
        }
        return fileSendPackageList;
    }



    private byte[] convertToPrimitiveArray(List<Byte> list){
        byte[] array = new byte[list.size()];
        for(int i=0 ; i<list.size() ; i++){
            array[i] = list.get(i);
        }
        return array;
    }

}