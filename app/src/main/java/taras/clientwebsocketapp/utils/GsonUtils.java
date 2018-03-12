package taras.clientwebsocketapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import taras.clientwebsocketapp.model.GetPermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;

/**
 * Created by Taras on 08.02.2018.
 */

public class GsonUtils {

    public static ScannerPackage parseScannerPackage(String json){
        Gson gson = new Gson();
        ScannerPackage scannerPackage = gson.fromJson(json, ScannerPackage.class);
        return scannerPackage;
    }

    public static String createJsonScannerPackage(ScannerPackage scannerPackage){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(scannerPackage);
    }


    public static GetPermissionPackage parseGetPermissionPackage(String json){
        Gson gson = new Gson();
        GetPermissionPackage getPermissionPackage = gson.fromJson(json, GetPermissionPackage.class);
        return getPermissionPackage;
    }

    public static String createGetPermissionPackage(GetPermissionPackage getPermissionPackage){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(getPermissionPackage);
    }


}
