package taras.clientwebsocketapp.managers;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.EventBusMsg;

/**
 * Created by Taras on 03.03.2018.
 */

public class SelectedFileManager {

    private static final int MAX_SELECTED_DEVICES = 1;

    private ArrayList<File> selectedDirectoriesFilesList;
    private ArrayList<String> selectedDevicesIp;

    private static SelectedFileManager selectedFileManager;
    //private SelectedFileView selectedFileView;

    public SelectedFileManager() {
        selectedDirectoriesFilesList = new ArrayList<>();
        selectedDevicesIp = new ArrayList<>();
    }

    public static SelectedFileManager getSelectedFileManager(){
        if (selectedFileManager == null){
            selectedFileManager = new SelectedFileManager();
        }
        return selectedFileManager;
    }

    /*
    public void setSelectedFileView(SelectedFileView selectedFileView){
        this.selectedFileView = selectedFileView;
    }
    */

    //work with selected files;
    public SelectedFileManager insertToSelectedFilesList(File file){
        if (selectedDirectoriesFilesList.contains(file)){
            removeFromSelectedFiles(file);
        } else {
            if ( selectedDevicesIp.size() <= MAX_SELECTED_DEVICES){
                addToSelectedFiles(file);
            }
        }
        return this;
    }

    //work with selected files
    public List<File> getAllSelectedDirectories(){
        return this.selectedDirectoriesFilesList;
    }
    public List<String> getAllSelectedFilesNames(){
        List<String> filesName = new ArrayList<>();
        for (File file: selectedDirectoriesFilesList){
            filesName.add(file.getAbsolutePath());
        }
        return filesName;
    }


    public void removeAllSelectedFiles(){
        selectedDirectoriesFilesList.clear();
        //selectedFileView.setSelectedNum(0);
        //selectedFileView.setVisibility(View.GONE);
    }
    public boolean isSelectedFilesListEmpty(){
        if (selectedDirectoriesFilesList.size() == 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean isFileSelected(File file){
        if (selectedDirectoriesFilesList.contains(file)){
            return true;
        } else {
            return false;
        }
    }

    private void addToSelectedFiles(File file){
        selectedDirectoriesFilesList.add(file);
        //selectedFileView.setSelectedNum(selectedDirectoriesFilesList.size());
        if (selectedDirectoriesFilesList.size() > 0){
            //selectedFileView.setVisibility(View.VISIBLE);
        }
    }
    private void removeFromSelectedFiles(File file){
        selectedDirectoriesFilesList.remove(file);
        //selectedFileView.setSelectedNum(selectedDirectoriesFilesList.size());
        if (selectedDirectoriesFilesList.size() == 0){
            //selectedFileView.setVisibility(View.GONE);
        }
    }


    //work with selected devices
    public SelectedFileManager insertToSelectedDevicesList(String deviceIp){
        if (selectedDevicesIp.contains(deviceIp)){
            removeFromSelectedDevices(deviceIp);
        } else {
            addToSelectedDevices(deviceIp);
        }
        return this;
    }
    public boolean isSelectedDevicesListEmpty(){
        if (selectedDevicesIp.size() == 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean isDeviceSelected(String deviceIp){
        if (selectedDevicesIp.contains(deviceIp)){
            return true;
        } else {
            return false;
        }
    }
    public void removeAllSelectedDevices(){
        selectedDevicesIp.clear();
    }

    private void addToSelectedDevices(String deviceIp){
        selectedDevicesIp.add(deviceIp);
        if (selectedDevicesIp.size() > 0){
            //selectedFileView.getIvShare().setVisibility(View.VISIBLE);
        }
    }
    private void removeFromSelectedDevices(String deviceIp){
        selectedDevicesIp.remove(deviceIp);
        if (selectedDevicesIp.size() == 0){
            //selectedFileView.getIvShare().setVisibility(View.INVISIBLE);
        }
    }


    //work with service
    public void sendDataToService(){
        List<String> filesName = new ArrayList<>();
        for (File file: selectedDirectoriesFilesList){
            filesName.add(file.getName());
        }

        PermissionPackage permissionPackage = new PermissionPackage();
        permissionPackage.setFilesName(filesName);
        permissionPackage.setServerIp(selectedDevicesIp.get(0));

        Log.d("myLogs", "sendDataToService: " + permissionPackage.toJson());

        removeAllSelectedFiles();
        EventBusMsg<PermissionPackage> message =
                new EventBusMsg<PermissionPackage>(EventBusMsg.TO_SERVICE, EventBusMsg.PACKAGE_SCANNER, permissionPackage);
        EventBus.getDefault().postSticky(message);

    }

}
