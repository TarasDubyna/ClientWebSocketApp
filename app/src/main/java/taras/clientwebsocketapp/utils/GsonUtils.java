package taras.clientwebsocketapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.FileSendStatePackage;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.model.ServerStatePackage;

/**
 * Created by Taras on 08.02.2018.
 */

public class GsonUtils {

    // json -> object
    public static ScannerPackage parseScannerPackage(String json){
        Gson gson = new Gson();
        ScannerPackage scannerPackage = gson.fromJson(json, ScannerPackage.class);
        return scannerPackage;
    }
    public static PermissionPackage parsePermissionPackage(String json){
        Gson gson = new Gson();
        PermissionPackage permissionPackage = gson.fromJson(json, PermissionPackage.class);
        return permissionPackage;
    }

    public static FileSendStatePackage parseFileSendStatePackage(String json){
        Gson gson = new Gson();
        FileSendStatePackage fileSendStatePackage = gson.fromJson(json, FileSendStatePackage.class);
        return fileSendStatePackage;
    }

    public static FileSendPackage parseFileSendPackage(String json){
        Gson gson = new Gson();
        FileSendPackage fileSendPackage = gson.fromJson(json, FileSendPackage.class);
        return fileSendPackage;
    }


    // object -> json
    public static String createJsonScannerPackage(ScannerPackage scannerPackage){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(scannerPackage);
    }
    public static String createJsonPermissionPackageFirst(PermissionPackage permissionPackage){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(permissionPackage);
    }
    public static String createJsonFileSendStatePackage(FileSendStatePackage fileSendStatePackage){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(fileSendStatePackage);
    }
    public static String createJsonFileSendPackage(FileSendPackage fileSendPackage){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(fileSendPackage);
    }






}
