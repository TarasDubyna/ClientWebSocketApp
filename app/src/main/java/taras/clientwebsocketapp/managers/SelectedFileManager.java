package taras.clientwebsocketapp.managers;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import taras.clientwebsocketapp.custom_views.SelectedFileView;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.file_manager.FileManagerAdapter;

/**
 * Created by Taras on 03.03.2018.
 */

public class SelectedFileManager {


    private ArrayList<File> selectedDirectoriesFilesList;
    private int selectedFilesCounter = 0;

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
    }

    public SelectedFileManager insertToSelected(File file){
        if (selectedDirectoriesFilesList.contains(file)){
            removeFromSelected(file);
        } else {
            addToSelected(file);
        }
        return this;
    }
    public List<File> getAllSelectedDirectoriesFilesList(){
        return this.selectedDirectoriesFilesList;
    }

    public void removeAllSelected(){
        selectedDirectoriesFilesList.clear();
        selectedFileView.setSelectedNum(0);
        selectedFileView.setVisibility(View.GONE);
    }

    private void addToSelected(File file){
        selectedDirectoriesFilesList.add(file);
        selectedFilesCounter = selectedDirectoriesFilesList.size();
        selectedFileView.setSelectedNum(selectedFilesCounter);
        if (selectedFilesCounter > 0){
            selectedFileView.setVisibility(View.VISIBLE);
        }
    }
    private void removeFromSelected(File file){
        selectedDirectoriesFilesList.remove(file);
        selectedFilesCounter = selectedDirectoriesFilesList.size();
        selectedFileView.setSelectedNum(selectedFilesCounter);
        if (selectedFilesCounter == 0){
            selectedFileView.setVisibility(View.GONE);
        }
    }

    public int getSize(){
        return selectedDirectoriesFilesList.size();
    }

    public void setSelectedFileView(SelectedFileView selectedFileView, SelectedFileView.SelectedFileViewInterface selectedFileViewInterface) {
        this.selectedFileView = selectedFileView;
        this.selectedFileView.initRemoveAllFilesFromSelected(selectedFileViewInterface);
    }
    public boolean isEmpty(){
        if (selectedDirectoriesFilesList.size() == 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean isSelected(File file){
        if (selectedDirectoriesFilesList.contains(file)){
            return true;
        } else {
            return false;
        }
    }
}
