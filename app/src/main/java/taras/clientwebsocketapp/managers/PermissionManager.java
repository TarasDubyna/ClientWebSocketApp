package taras.clientwebsocketapp.managers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.model.PermissionPackage;

public class PermissionManager {

    public static final int CLIENT = 0;
    public static final int SERVER = 1;

    //private List<PermissionPackage> permissionPackageList;
    private List<PermissionPackage> permissionPackageListClient;
    private List<PermissionPackage> permissionPackageListServer;

    private final Object lock = new Object();

    private static PermissionManager permissionManager;

    public PermissionManager() {
        this.permissionPackageListClient = new ArrayList<>();
        this.permissionPackageListServer = new ArrayList<>();
    }

    public static PermissionManager getPermissionManager(){
        if (permissionManager == null){
            permissionManager = new PermissionManager();
        }
        return permissionManager;
    }

    public void addToPermissionManager(int typeManager, PermissionPackage permissionPackage){
        synchronized (lock){
            getPermissionList(typeManager).add(permissionPackage);
        }
    }

    public void removeFromPermissionManager(int typeManager, PermissionPackage permissionPackage){
        synchronized (lock){
            for (PermissionPackage pack: getPermissionList(typeManager)){
                if (isListPermissionConsist(typeManager, permissionPackage)){
                    getPermissionList(typeManager).remove(pack);
                    break;
                }
            }
        }
    }

    public boolean isListPermissionConsist(int typeManager, PermissionPackage permissionPackage){
        synchronized (lock){
            for (PermissionPackage pack: getPermissionList(typeManager)){
                if (pack.getToken().equals(permissionPackage.getToken())){
                    return true;
                }
            }
            return false;
        }
    }




    /*
    * Сейчас используется isAllowed, надо что бы использовался код шифрования
    */
    public void setAcceptPermission(int typeManager, PermissionPackage permissionPackage, boolean isAllowed){
        synchronized (lock){
            for (PermissionPackage pack: getPermissionList(typeManager)){
                if (pack.getToken().equals(permissionPackage.getToken())){
                    if (pack.getIsAllowed() == null){
                        if (isAllowed){
                            Log.d("test", "setIsAllowed: true");
                            pack.setIsAllowed("true");
                            break;
                        } else {
                            Log.d("test", "setIsAllowed: false");
                            pack.setIsAllowed("false");
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }


    private List<PermissionPackage> getPermissionList(int typeManager){
        if (typeManager == CLIENT){
            return this.permissionPackageListClient;
        }
        if (typeManager == SERVER){
            return this.permissionPackageListServer;
        }
        return new ArrayList<>();
    }


    public PermissionPackage getPermissionPackageFromManager(int typeManager, PermissionPackage permissionPackage){
        synchronized (lock){
            for (PermissionPackage pack: getPermissionList(typeManager)){
                if (pack.getToken().equals(permissionPackage.getToken())){
                    Log.d("test","getToken(), true");
                    return pack;
                }
            }
            return permissionPackage;
        }
    }
}
