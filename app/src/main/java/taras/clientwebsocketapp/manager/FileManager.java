package taras.clientwebsocketapp.manager;

import android.content.Context;
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
import taras.clientwebsocketapp.screens.file_manager.DirectoryAdapter;
import taras.clientwebsocketapp.screens.file_manager.FileManagerAdapter;
import taras.clientwebsocketapp.screens.file_manager.FileManagerInterface;

/**
 * Created by Taras on 18.02.2018.
 */

public class FileManager implements FileManagerInterface {
    private static final String LOG_TAG = "myLogs";

    private Context mContext;

    private ArrayList<String> directoryList;
    private ArrayList<File> mCurrentDirectoryFilesList;
    private File mCurrentDir; //Our current location.

    private static FileManager fileManager;

    private UpdateGuiRecyclersInterface updateGuiRecyclersInterface;
    private FileManagerAdapter fileManagerAdapter;
    private DirectoryAdapter directoryAdapter;

    public static FileManager getManager(Context context){
        if (fileManager == null){
            fileManager = new FileManager(context);
        }
        return fileManager;
    }

    public FileManager(Context context) {
        this.mContext = context;
        File file = Environment.getExternalStorageDirectory();
        directoryList = new ArrayList<>();
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

    public FileManager initUpdateGuiRecyclersInterface(UpdateGuiRecyclersInterface updateGuiRecyclersInterface){
        this.updateGuiRecyclersInterface = updateGuiRecyclersInterface;
        return this;
    }

    //init adapters
    public FileManager initFileManagerAdapter(){
        fileManagerAdapter = new FileManagerAdapter(mContext, this , mCurrentDirectoryFilesList);
        return this;
    }
    public FileManager initDirectoryAdapter(){
        directoryAdapter = new DirectoryAdapter(mContext, this);
        return this;
    }

    public FileManagerAdapter getFileManagerAdapter() {
        return fileManagerAdapter;
    }
    public DirectoryAdapter getDirectoryAdapter() {
        return directoryAdapter;
    }

    public void done(){
        directoryAdapter.addDirectoryList(directoryList);
        fileManagerAdapter.addFileList(mCurrentDirectoryFilesList);
    }

    private void removeListToPosition(ArrayList<String> list, int position){
        Log.d(LOG_TAG, "ArrayList<String> list, size: " + list.size());
        Log.d(LOG_TAG, "Position: " + position);

        for (int i = list.size() - 1; i > 0; i--){
            Log.d(LOG_TAG, "list item: " + list.get(i));
            Log.d(LOG_TAG, "list item position: " + i);
            if (list.size() > position + 1){
                list.remove(i);
            }
        }
    }

    public ArrayList<String> getDirectoryList() {
        return directoryList;
    }

    @Override
    public void getFolderWithFiles(String absolutePath) {
        directoryList.add(absolutePath);
        mCurrentDirectoryFilesList = createCurrentFile(absolutePath).getAllFiles();
        fileManagerAdapter.addFileList(mCurrentDirectoryFilesList);
        directoryAdapter.notifyDataSetChanged();
        fileManagerAdapter.notifyDataSetChanged();
        updateGuiRecyclersInterface.updateFilesRecyclerIfFolderNotEmpty();
    }

    @Override
    public void getFolderEmpty(String absolutePath) {
        directoryList.add(absolutePath);
        updateGuiRecyclersInterface.updateFilesRecyclerIfFolderEmpty();
    }

    @Override
    public void goToPreviousFolder(int position) {
        wentToPreviousFolder(position);
    }

    public void wentToPreviousFolder(int position){
        mCurrentDirectoryFilesList = createCurrentFile(directoryList.get(position)).getAllFiles();
        removeListToPosition(directoryList, position);
        fileManagerAdapter.addFileList(mCurrentDirectoryFilesList);
        directoryAdapter.notifyDataSetChanged();
        fileManagerAdapter.notifyDataSetChanged();
        updateGuiRecyclersInterface.updateFilesRecyclerIfFolderNotEmpty();
    }
}
