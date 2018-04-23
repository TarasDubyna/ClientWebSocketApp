package taras.clientwebsocketapp.managers;

import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.model.PermissionPackage;

public class PermissionManager {

    private List<PermissionPackage> permissionPackageList;

    private static PermissionManager permissionManager;

    public PermissionManager() {
        this.permissionPackageList = new ArrayList<>();
    }

    public static PermissionManager getPermissionManager(){
        if (permissionManager == null){
            permissionManager = new PermissionManager();
        }
        return permissionManager;
    }

    public void addToPermissionManager(PermissionPackage permissionPackage){
        this.permissionPackageList.add(permissionPackage);
    }
    public void removeFromPermissionManager(PermissionPackage permissionPackage){
        for (PermissionPackage pack: permissionPackageList){
            if (isPermissionConsist(permissionPackage)){
                permissionPackageList.remove(pack);
                break;
            }
        }
    }
    public boolean isPermissionConsist(PermissionPackage permissionPackage){
        for (PermissionPackage pack: permissionPackageList){
            if (pack.getFilesName().equals(permissionPackage.getFilesName())
                    && pack.getClientIp().equals(permissionPackage.getClientIp())){
                return true;
            }
        }
        return false;
    }

}
