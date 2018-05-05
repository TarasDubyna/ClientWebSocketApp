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
            if (typeManager == CLIENT){
                this.permissionPackageListClient.add(permissionPackage);
            }
            if (typeManager == SERVER){
                this.permissionPackageListServer.add(permissionPackage);
            }
        }
    }

    public void removeFromPermissionManager(int typeManager, PermissionPackage permissionPackage){
        synchronized (lock){
            if (typeManager == CLIENT){
                removeFromList(this.permissionPackageListClient, permissionPackage);
            }
            if (typeManager == SERVER){
                removeFromList(this.permissionPackageListServer, permissionPackage);
            }
        }
    }


    private void removeFromList(List<PermissionPackage> list, PermissionPackage pack){
        for (PermissionPackage permissionPackage: list){
            if (isPermissionConsist(permissionPackage)){
                list.remove(pack);
                break;
            }
        }
    }

    public void setAcceptPermission(PermissionPackage permissionPackage, boolean isAllowed){
        synchronized (lock){
            for (PermissionPackage pack: permissionPackageList){
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
                    }
                }
            }
        }
    }
    public boolean isPermissionConsist(PermissionPackage permissionPackage){
        synchronized (lock){
            for (PermissionPackage pack: permissionPackageList){
                if (pack.getToken().equals(permissionPackage.getToken())){
                    return true;
                }
            }
            return false;
        }
    }

    public PermissionPackage getPermissionFromManager(PermissionPackage permissionPackage){
        synchronized (lock){
            for (PermissionPackage pack: permissionPackageList){
                if (pack.getToken().equals(permissionPackage.getToken())){
                    Log.d("test","getToken(), true");
                    return pack;
                }
            }
            return permissionPackage;
        }
    }
}
