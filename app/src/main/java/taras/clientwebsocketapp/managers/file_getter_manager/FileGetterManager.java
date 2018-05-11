package taras.clientwebsocketapp.managers.file_getter_manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import taras.clientwebsocketapp.model.FileSendPackage;

public class FileGetterManager {

    private static FileGetterManager fileGetterManager;

    private List<List<FileSendPackage>> gettedFileSendPackages;
    private Map<FileSendPackage, byte[]> compileFileMap;

    private final Object lock = new Object();

    public static FileGetterManager getFileGetterManager(){
        if (fileGetterManager == null){
            fileGetterManager = new FileGetterManager();
        }
        return fileGetterManager;
    }

    public FileGetterManager() {
        this.gettedFileSendPackages = new ArrayList<>();
        this.compileFileMap = new HashMap<>();
    }


    public void addGettedFileSandPackage(FileSendPackage fileSendPackage){

    }

    private void compileFile(FileSendPackage fileSendPackage){

        compileFileMap.
    }


    private boolean checkIfFileExist(FileSendPackage fileSendPackage){
        Iterator it = compileFileMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            FileSendPackage comparedFileSendPackage = (FileSendPackage) pair.getKey();
            if (compareFileSendPackagesByFileName(comparedFileSendPackage, fileSendPackage)){
                return true;
            }
        }
        return false;
    }


    public void addFileSendPackage(FileSendPackage fileSendPackage){

    }



    /*public void addFileSendPackage(FileSendPackage fileSendPackage){
        synchronized (lock){
            if (isFileExist(fileSendPackage)){
                addFile(fileSendPackage);
            } else {
                List<FileSendPackage> fileSendPackages = new ArrayList<>();
                fileSendPackages.add(fileSendPackage);
                this.gettedFileSendPackages.add(fileSendPackages);
            }
        }
    }
    public void removeFileSendPackage(FileSendPackage fileSendPackage){
        synchronized (lock){
            for (List<FileSendPackage> fileSendPackageList: gettedFileSendPackages){
                for (FileSendPackage comparedFileSendPackage: fileSendPackageList){
                    if (compareFileSendPackagesByCurrentPart(comparedFileSendPackage, fileSendPackage)){
                        fileSendPackageList.remove(fileSendPackage);
                        break;
                    }
                }
            }
        }
    }*/

    private void addFile(FileSendPackage fileSendPackage){
        for (List<FileSendPackage> fileSendPackageList: gettedFileSendPackages){
            if (compareFileSendPackagesByFileName(fileSendPackageList.get(0), fileSendPackage)){
                fileSendPackageList.add(fileSendPackage);
                break;
            }
        }
    }
    private boolean isFileExist(FileSendPackage fileSendPackage){
        if (gettedFileSendPackages.size() == 0){
            return false;
        } else {
            for (List<FileSendPackage> fileList: gettedFileSendPackages){
                if (fileList.size() != 0){
                    if (fileList.get(0).getFileName().equals(fileSendPackage.getFileName())
                            && fileList.get(0).getToken().equals(fileSendPackage.getToken())){
                        return true;
                    }
                }
            }
            return false;
        }

    }
    private boolean compareFileSendPackagesByFileName(FileSendPackage fileSendPackageCompared, FileSendPackage fileSendPackage){
        if (fileSendPackageCompared.getToken().equals(fileSendPackage.getToken())
                && fileSendPackageCompared.getFileName().equals(fileSendPackage.getFileName()))
            return true;
        else return false;
    }
    private boolean compareFileSendPackagesByCurrentPart(FileSendPackage fileSendPackageCompared, FileSendPackage fileSendPackage){
        if (fileSendPackageCompared.getToken().equals(fileSendPackage.getToken())
                && fileSendPackageCompared.getFileName().equals(fileSendPackage.getFileName())
                && fileSendPackageCompared.getCurrentPart() == fileSendPackage.getCurrentPart())
            return true;
       else return false;
    }


}
