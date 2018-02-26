package taras.clientwebsocketapp.manager;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import taras.clientwebsocketapp.model.FileFolder;
import taras.clientwebsocketapp.screens.file_manager.DirectoryAdapter;
import taras.clientwebsocketapp.screens.file_manager.FileManagerAdapter;

/**
 * Created by Taras on 18.02.2018.
 */

public class FileManager {
    private static final String LOG_TAG = "myLogs";

    private Context mContext;

    //----------------------------------------------------------------------------------------------

    private String startDirectory;
    private String lastDirectory;
    private ArrayList<String> allDirectoriesList;

    //----------------------------------------------------------------------------------------------

    private ArrayList<String> directoryList;
    private ArrayList<File> mCurrentDirectoryFilesList;
    private File mCurrentDir; //Our current location.

    private static FileManager fileManager;


    public static FileManager getManager(Context context){
        if (fileManager == null){
            fileManager = new FileManager(context);
        }
        return fileManager;
    }
    public FileManager(Context context) {
        this.mContext = context;
        directoryList = new ArrayList<>();

        File file = Environment.getExternalStorageDirectory();
        startDirectory = file.getPath();
        directoryList.add(startDirectory);

        directoryList.add(file.getPath());
        mCurrentDirectoryFilesList = setCurrentFile(file).getAllFiles();
    }

    public FileManager createCurrentFile(String direction){
        this.mCurrentDir = new File(direction);
        return this;
    }
    public  FileManager setCurrentFile(File file){
        this.mCurrentDir = file;
        return this;
    }
    public ArrayList<File> getAllFiles(){
        File[] allFiles = mCurrentDir.listFiles();

        /* I want all directories to appear before files do, so I have separate lists for both that are merged into one later.*/
        ArrayList<File> dirs = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();

        if (allFiles == null){
            return null;
        } else {
            for (File file : allFiles) {
                if (file.isDirectory()) {
                    dirs.add(file);
                } else {
                    files.add(file);
                }
            }
            Collections.sort(dirs);
            Collections.sort(files);

        /*Both lists are sorted, so I can just add the files to the dirs list.
        This will give me a list of dirs on top and files on bottom. */
            dirs.addAll(files);
        }

        return dirs;
    }


    public static final String TYPE_FILE = "TYPE_FILE";
    public static final String TYPE_FOLDER = "TYPE_FOLDER";
    public static final String TYPE_FOLDER_EMPTY = "TYPE_FOLDER_EMPTY";

    public static String getTypeFile(File file){
        File[] allFiles = file.listFiles();
        if (allFiles == null){
            return TYPE_FILE;
        } else if (allFiles.length == 0){
            return TYPE_FOLDER_EMPTY;
        } else {
            return TYPE_FOLDER;
        }
    }
    public static String getTypeFileFolder(File file){
        File[] allFiles = file.listFiles();
        if (allFiles == null){
            return TYPE_FILE;
        } else {
            return TYPE_FOLDER;
        }
    }
    public boolean isFile(File file){
        if (file.listFiles() != null){
            ArrayList<File> files = new ArrayList(Arrays.asList(file.listFiles()));
            if (files == null){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public boolean isEmptyFolder(File file){
        ArrayList<File> files = new ArrayList<>(Arrays.asList(file));
        if (files.size() > 0){
            return false;
        } else {
            return true;
        }
    }





    public String getStartDirectory() {
        return startDirectory;
    }
    public String getLastDirectory(){return lastDirectory;}
    public ArrayList<String> getDirectoryList() {
        return directoryList;
    }

}
