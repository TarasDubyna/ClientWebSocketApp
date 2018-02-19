package taras.clientwebsocketapp.file_manager;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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
    private File mPreviousDir; //Our previous location.
    private Stack<File> mHistory; //Our navigation History.

    public FileManager() {
        mHistory = new Stack<>();
        checkExternalStorage();
    }

    public void checkExternalStorage(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mCurrentDir = Environment.getExternalStorageDirectory();
            Log.i(LOG_TAG, String.valueOf(mCurrentDir));
        } else {
            Log.i(LOG_TAG, "External storage unavailable");
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

    public static String getTypeFile(Uri uri) {
        String mimeType = null;

        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.getPath());

        if (MimeTypeMap.getSingleton().hasExtension(extension)) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
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
