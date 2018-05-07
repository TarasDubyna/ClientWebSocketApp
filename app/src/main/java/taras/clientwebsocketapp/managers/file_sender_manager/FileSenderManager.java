package taras.clientwebsocketapp.managers.file_sender_manager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import taras.clientwebsocketapp.model.FileFolder;
import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.utils.FileUtils;

public class FileSenderManager {

    private static FileSenderManager fileSenderManager;

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
        this.lists.add()
    }

    private List<FileSendPackage> createFileSendPackage(File file){
        Arrays.copyofRa
    }

    private byte[] convertFileToByteArray(File file) throws IOException {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    private File convertByteArrayToFile(byte[] array, String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(array);
        out.close();
        return new File()
    }


    private List<byte[]> split(byte[] array, byte value){
        List<byte[]> list = new ArrayList<>();
        List<Byte> subList = new ArrayList<>();
        for(byte element : array){
            if(element == value && !subList.isEmpty()){
                list.add(convertToPrimitiveArray(subList));
                subList = new ArrayList<>();
            }else if(element != value){
                subList.add(element);
            }
        }
        if(!subList.isEmpty()){
            list.add(convertToPrimitiveArray(subList));
        }
        return list;
    }

    private byte[] convertToPrimitiveArray(List<Byte> list){
        byte[] array = new byte[list.size()];
        for(int i=0 ; i<list.size() ; i++){
            array[i] = list.get(i);
        }
        return array;
    }

}
