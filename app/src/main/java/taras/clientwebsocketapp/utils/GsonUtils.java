package taras.clientwebsocketapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import taras.clientwebsocketapp.model.PermissionPackageFirst;
import taras.clientwebsocketapp.model.PermissionPackageSecond;
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
    public static PermissionPackageFirst parsePermissionPackageFirst(String json){
        Gson gson = new Gson();
        PermissionPackageFirst permissionPackageFirst = gson.fromJson(json, PermissionPackageFirst.class);
        return permissionPackageFirst;
    }
    public static PermissionPackageSecond parsePermissionPackageSecond(String json){
        Gson gson = new Gson();
        PermissionPackageSecond permissionPackageSecond = gson.fromJson(json, PermissionPackageSecond.class);
        return permissionPackageSecond;
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
    public static String createJsonPermissionPackageFirst(PermissionPackageFirst permissionPackageFirst){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(permissionPackageFirst);
    }
    public static String createJsonPermissionPackageSecond(PermissionPackageSecond permissionPackageSecond){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(permissionPackageSecond);
    }
    public static String createJsonServerStatePackage(ServerStatePackage serverStatePackage){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(serverStatePackage);
    }






}
