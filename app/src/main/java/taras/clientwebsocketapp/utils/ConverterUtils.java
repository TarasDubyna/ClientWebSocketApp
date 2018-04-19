package taras.clientwebsocketapp.utils;

public class ConverterUtils {

    public static String getIpEnd(String fullIp){
        String[] words = fullIp.split("\\.");
        return words[words.length - 1];
    }
}
