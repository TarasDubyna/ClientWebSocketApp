package taras.clientwebsocketapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    public static PermissionPackage parsePermissionPackageFirst(String json){
        Gson gson = new Gson();
        PermissionPackage permissionPackage = gson.fromJson(json, PermissionPackage.class);
        return permissionPackage;
    }
    public static ServerStatePackage parseServerStatePackage(String json){
        Gson gson = new Gson();
        ServerStatePackage serverStatePackage = gson.fromJson(json, ServerStatePackage.class);
        return serverStatePackage;
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
    public static String createJsonServerStatePackage(ServerStatePackage serverStatePackage){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(serverStatePackage);
    }






}
