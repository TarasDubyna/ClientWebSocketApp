package taras.clientwebsocketapp.managers;

import android.app.Activity;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.custom_views.SelectedFileView;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GlobalBus;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 03.03.2018.
 */

public class SelectedFileManager {

    private Activity mActivity;

    private ArrayList<File> selectedDirectoriesFilesList;
    private ArrayList<String> selectedDevicesIp;
    private int maxSelectedDevices = 1;

    private static SelectedFileManager selectedFileManager;
    private SelectedFileView selectedFileView;

    public static SelectedFileManager getSelectedFileManager(){
        if (selectedFileManager == null){
            selectedFileManager = new SelectedFileManager();
        }
        return selectedFileManager;
    }

    public SelectedFileManager() {
        selectedDirectoriesFilesList = new ArrayList<>();
        selectedDevicesIp = new ArrayList<>();
    }

    public SelectedFileManager setActivity(Activity activity){
        this.mActivity = activity;
        return this;
    }
    public void setSelectedFileView(SelectedFileView selectedFileView, SelectedFileView.SelectedFileViewInterface selectedFileViewInterface) {
        this.selectedFileView = selectedFileView;
        this.selectedFileView.initRemoveAllFilesFromSelected(selectedFileViewInterface);
    }
    public void setSelectedFileView() {
        //this.selectedFileView.initRemoveAllDeviceFromSelected(selectedDeviceViewInterface);
    }

    //work with selected files;
    public SelectedFileManager insertToSelectedFilesList(File file){
        if (selectedDirectoriesFilesList.contains(file)){
            removeFromSelectedFiles(file);
        } else {
            if ( selectedDevicesIp.size() <= maxSelectedDevices){
                addToSelectedFiles(file);
            }
        }
        return this;
    }
    public List<File> getAllSelectedDirectoriesFilesList(){
        return this.selectedDirectoriesFilesList;
    }
    public int getSelectedFilesListSize(){
        return selectedDirectoriesFilesList.size();
    }
    public void removeAllSelectedFiles(){
        selectedDirectoriesFilesList.clear();
        ((MainActivity)mActivity).setDrawerLayoutOpened();
        selectedFileView.setSelectedNum(0);
        selectedFileView.setVisibility(View.GONE);
    }
    private void addToSelectedFiles(File file){
        selectedDirectoriesFilesList.add(file);
        selectedFileView.setSelectedNum(selectedDirectoriesFilesList.size());
        if (selectedDirectoriesFilesList.size() > 0){
            ((MainActivity)mActivity).setDrawerLayoutLocked();
            selectedFileView.setVisibility(View.VISIBLE);
        }
    }
    private void removeFromSelectedFiles(File file){
        selectedDirectoriesFilesList.remove(file);
        selectedFileView.setSelectedNum(selectedDirectoriesFilesList.size());
        if (selectedDirectoriesFilesList.size() == 0){
            ((MainActivity)mActivity).setDrawerLayoutOpened();
            selectedFileView.setVisibility(View.GONE);
        }
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

    //work with selected devices
    public SelectedFileManager insertToSelectedDevicesList(String deviceIp){
        if (selectedDevicesIp.contains(deviceIp)){
            removeFromSelectedDevices(deviceIp);
        } else {
            addToSelectedDevices(deviceIp);
        }
        return this;
    }
    private void addToSelectedDevices(String deviceIp){
        selectedDevicesIp.add(deviceIp);
        if (selectedDevicesIp.size() > 0){
            selectedFileView.getIvShare().setVisibility(View.VISIBLE);
        }
    }
    private void removeFromSelectedDevices(String deviceIp){
        selectedDevicesIp.remove(deviceIp);
        if (selectedDevicesIp.size() == 0){
            selectedFileView.getIvShare().setVisibility(View.INVISIBLE);
        }
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


    //work with service
    public void sendDataToService(){
        List<String> filesName = new ArrayList<>();
        for (File file: selectedDirectoriesFilesList){
            filesName.add(file.getName());
        }

        PermissionPackage permissionPackage = new PermissionPackage();
        permissionPackage.setDescription(GlobalBus.TO_SERVICE);
        permissionPackage.setType(Constants.PACKAGE_TYPE_PERMISSION);
        permissionPackage.setFilesName(filesName);
        permissionPackage.setClientDeviceIp(AppApplication.deviceIp);
        permissionPackage.setClientDeviceName(PreferenceUtils.getDeviceName());

        GlobalBus.getBus().post(permissionPackage);
        SelectedFileManager.getSelectedFileManager().removeAllSelectedFiles();
    }

}
