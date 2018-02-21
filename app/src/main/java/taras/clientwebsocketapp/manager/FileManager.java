package taras.clientwebsocketapp.manager;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.model.FileFolder;

/**
 * Created by Taras on 18.02.2018.
 */

public class FileManager {
    private static final String LOG_TAG = "myLogs";

    private String path;

    private String currentDirectory;
    private String externalStorageDirectory;
    private File[] filesInLocation;

    private List<String> directoryList;

    private File mCurrentDir; //Our current location.

    private static FileManager fileManager;

    public static FileManager getManager(){
        if (fileManager == null){
            fileManager = new FileManager();
        }
        return fileManager;
    }

    public static File checkExternalStorage(){
        if (AppApplication.externalStorageDir.equals(Environment.MEDIA_MOUNTED)) {
            Log.i(LOG_TAG, String.valueOf(AppApplication.externalStorageDir));
            return AppApplication.externalStorageDir;
        } else {
            Log.i(LOG_TAG, "External storage unavailable");
            return null;
        }
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

    public boolean isFile(ArrayList<File> files){
        if (files == null){
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmptyFolder(ArrayList<File> files){
        if (files.size() > 0){
            return false;
        } else {
            return true;
        }
    }
}
