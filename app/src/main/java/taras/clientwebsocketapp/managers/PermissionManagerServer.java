package taras.clientwebsocketapp.managers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.model.PermissionPackage;

public class PermissionManagerServer {
    private List<PermissionPackage> permissionPackageList;

    private static PermissionManagerServer permissionManager;
    private final Object lock = new Object();

    public PermissionManagerServer() {
        this.permissionPackageList = new ArrayList<>();
    }

    public static PermissionManagerServer getPermissionManager(){
        if (permissionManager == null){
            permissionManager = new PermissionManagerServer();
        }
        return permissionManager;
    }

    public void addToPermissionManager(PermissionPackage permissionPackage){
        synchronized (lock){
            this.permissionPackageList.add(permissionPackage);
        }
    }
    public void removeFromPermissionManager(PermissionPackage permissionPackage){
        synchronized (lock){
            for (PermissionPackage pack: permissionPackageList){
                if (isPermissionConsist(permissionPackage)){
                    permissionPackageList.remove(pack);
                    break;
                }
            }
        }
    }

    public void setAcceptPermission(PermissionPackage permissionPackage, boolean isAllowed){
        synchronized (lock){
            for (PermissionPackage pack: permissionPackageList){
                if (pack.getToken().equals(permissionPackage.getToken())){
                    if (isAllowed){
                        permissionPackage.setIsAllowed("true");
                    } else {
                        permissionPackage.setIsAllowed("false");
                    }
                    break;
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
                    PermissionPackage returnPack = pack;
                    Log.d("getPermission: ", returnPack.getIsAllowed());
                    if (returnPack.getIsAllowed() == null){
                        return returnPack;
                    } else {
                        if (pack.getIsAllowed().equals("true")){
                            return returnPack;
                        }
                        if (pack.getIsAllowed().equals("false")){
                            removeFromPermissionManager(permissionPackage);
                            return returnPack;
                        }
                    }

                }
            }
            return permissionPackage;
        }
    }
}
