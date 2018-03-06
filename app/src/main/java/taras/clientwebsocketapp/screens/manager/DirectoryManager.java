package taras.clientwebsocketapp.screens.manager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Taras on 21.02.2018.
 */

public class DirectoryManager {

    private String currentDirectory;
    private ArrayList<String> directoryShortList;
    private ArrayList<String> directoryList;

    public DirectoryManager() {
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }
    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void createDirectoryShortList(String path){
        String[] array = path.split("/+");
        this.directoryShortList.clear();
        Collections.addAll(this.directoryShortList, array);
    }
}
