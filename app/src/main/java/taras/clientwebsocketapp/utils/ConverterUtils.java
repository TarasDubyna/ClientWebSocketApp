package taras.clientwebsocketapp.utils;

public class ConverterUtils {

    public static String getIpEnd(String fullIp){
        String[] words = fullIp.split("\\.");
        return words[words.length - 1];
    }
    public static String getFileNameFromDirectory(String fileDirectory){
        String[] array = fileDirectory.split("/+");
        return array[array.length - 1];
    }
}
