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

import taras.clientwebsocketapp.utils.PreferenceUtils;

public class FilePreparator {

    private static final String LOG_TAG = FilePreparator.class.getSimpleName();

    private String STORAGE_FILE_DIRECTORY = PreferenceUtils.getLocalStorageDirection();

    private File file;
    private byte[] fileInByteArray;
    private List<byte[]> fileSplitedInByteList;



    public FilePreparator() {
        FilePreparator filePreparator = new FilePreparator();
    }

    public FilePreparator addFile(File file){
        this.file = file;
        return this;
    }
    public FilePreparator addFileAsSplitted(List<byte[]> fileSplitedInByteList){
        this.fileSplitedInByteList = fileSplitedInByteList;
        return this;
    }

    public FilePreparator convertFileToByteArray(){
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
        return this;
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

    public FilePreparator splitByteArrayOnChunk (int chunckSize){
        int len = this.fileInByteArray.length;
        this.fileSplitedInByteList = new ArrayList<>();
        for (int i = 0; i < len - chunckSize + 1; i += chunckSize){
            this.fileSplitedInByteList.add(Arrays.copyOfRange(this.fileInByteArray, i, i + chunckSize));
        }
        if (len % chunckSize != 0)
            this.fileSplitedInByteList.add(Arrays.copyOfRange(this.fileInByteArray, len - len % chunckSize, len));

        return this;
    }
    public FilePreparator joinChunckToByteArray (int chunckSize){
        int listSize = this.fileSplitedInByteList.size();

        byte[] outputArray = new byte[chunckSize];

        for (int i = 0; i < listSize; i++){
            outputArray = outputArray + fileSplitedInByteList.get(i);
        }

    }


    private void convertListToArray(List<byte[]> list){

    }
}
