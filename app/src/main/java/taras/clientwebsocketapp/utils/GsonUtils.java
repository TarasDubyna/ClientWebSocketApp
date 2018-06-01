package taras.clientwebsocketapp.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import taras.clientwebsocketapp.model.FileSendPackage;
import taras.clientwebsocketapp.model.FileSendStatePackage;
import taras.clientwebsocketapp.model.Package;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.model.ServerStatePackage;

/**
 * Created by Taras on 08.02.2018.
 */

public class GsonUtils {

    private static final String LOG_TAG = GsonUtils.class.getSimpleName();


    public static String convertToJson(Package pack){
        switch (pack.getType()){
            case Constants.PACKAGE_TYPE_SCANNING:
                return ((ScannerPackage) pack).toJson();
            case Constants.PACKAGE_TYPE_PERMISSION:
                return ((PermissionPackage) pack).toJson();
            case Constants.PACKAGE_FILE_SEND:
                return ((FileSendPackage) pack).toJson();
                default:
                    return "";
        }
    }

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
        FileSendPackage fileSendPackage = null;
        try {
            gson.getAdapter(FileSendPackage.class).fromJson(json);
            fileSendPackage = gson.getAdapter(FileSendPackage.class).fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "IOException: " + e.getMessage());
        }
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
