package taras.clientwebsocketapp.managers.file_sender_manager;

import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.ConverterUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

public class FilePreparator {

    private static final String LOG_TAG = FilePreparator.class.getSimpleName();

    private String STORAGE_FILE_DIRECTORY = PreferenceUtils.getLocalStorageDirection();

    private File file;
    private String fileName;
    private int chunkSize;
    private byte[] fileInByteArray;
    private List<byte[]> fileSplitedInByteList;



    public FilePreparator(){

    }

    public FilePreparator addFileName(String fileName){
        this.fileName = fileName;
        this.file = new File(fileName);
        return this;
    }

    public FilePreparator setChunkSize(int size){
        this.chunkSize = size;
        return this;
    }

    public void getFileInByteList(){
        this.fileSplitedInByteList = new ArrayList<>();
        convertFileToByteArray();
        splitByteArrayOnChunk();
    }

    public void getFileForSendList(PermissionPackage permissionPackage, FilePreparatorCallback filePreparatorCallback){
        getFileInByteList();
        List<FileSendPackage> list = new ArrayList<>();
        for (int i = 0; i < this.fileSplitedInByteList.size(); i++){
            FileSendPackage fileSendPackage = new FileSendPackage();
            fileSendPackage.setType(Constants.PACKAGE_FILE_SEND);
            fileSendPackage.fillAfterPermission(permissionPackage);
            fileSendPackage.setFileName(fileName);
            fileSendPackage.setData(this.fileSplitedInByteList.get(i));
            fileSendPackage.setCurrentPart(i);
            fileSendPackage.setAllPart(this.fileSplitedInByteList.size());
            Log.d(LOG_TAG, "File name: " + ConverterUtils.getFileNameFromDirectory(fileSendPackage.getFileName()) + ",Package #" + i + "  , fileSendPackage data byte[] size: " + fileSendPackage.getData().length);
            list.add(fileSendPackage);
        }
        filePreparatorCallback.getFileForSendList(list);
    }


    public FilePreparator addFileAsSplitted(List<byte[]> fileSplitedInByteList){
        this.fileSplitedInByteList = fileSplitedInByteList;
        return this;
    }

    public void convertFileToByteArray(){
        if (this.file != null){
            this.fileInByteArray = new byte[(int) this.file.length()];
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(this.file);
                fileInputStream.read(this.fileInByteArray);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public FilePreparator convertByteArrayToFile(String fileName){
        String newFileDirectory = STORAGE_FILE_DIRECTORY + fileName;
        Log.d(LOG_TAG, "newFileDirectory: " + newFileDirectory);
        try (FileOutputStream fileOuputStream = new FileOutputStream(newFileDirectory)) {
            fileOuputStream.write(this.fileInByteArray);
            Log.d(LOG_TAG, "newFile: " + newFileDirectory + " write successful");
        } catch (IOException e) {
            Log.d(LOG_TAG, "newFile: " + newFileDirectory + " write error");
            e.printStackTrace();
        }
        return this;
    }

    public void splitByteArrayOnChunk (){
        int len = this.fileInByteArray.length;
        this.fileSplitedInByteList = new ArrayList<>();
        for (int i = 0; i < len - this.chunkSize + 1; i += this.chunkSize){
            this.fileSplitedInByteList.add(Arrays.copyOfRange(this.fileInByteArray, i, i + this.chunkSize));
        }
        if (len % this.chunkSize != 0)
            this.fileSplitedInByteList.add(Arrays.copyOfRange(this.fileInByteArray, len - len % this.chunkSize, len));
    }


    public FilePreparator joinChunckToByteArray (int chunckSize){
        return this;
    }


    private void convertListToArray(List<byte[]> list){

    }
}
