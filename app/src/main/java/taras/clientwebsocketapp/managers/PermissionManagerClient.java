package taras.clientwebsocketapp.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import taras.clientwebsocketapp.model.PermissionPackage;

public class PermissionManagerClient {

    private List<PermissionPackage> permissionPackageList;

    private static PermissionManagerClient permissionManager;
    private final Object lock = new Object();

    public PermissionManagerClient() {
        this.permissionPackageList = new ArrayList<>();
    }

    public static PermissionManagerClient getPermissionManager(){
        if (permissionManager == null){
            permissionManager = new PermissionManagerClient();
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

}
