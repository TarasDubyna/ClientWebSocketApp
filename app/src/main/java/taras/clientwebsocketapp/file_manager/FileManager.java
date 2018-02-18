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

    public List<File> getAllFiles(File f){
        File[] allFiles = f.listFiles();

        /* I want all directories to appear before files do, so I have separate lists for both that are merged into one later.*/
        List<File> dirs = new ArrayList<>();
        List<File> files = new ArrayList<>();

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

        return dirs;
    }

    public String getTypeFile(Uri uri) {
        String mimeType = null;

        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.getPath());

        if (MimeTypeMap.getSingleton().hasExtension(extension)) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }




















    /*
    public static String getExternalStorageDirectory(){
        String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
        Log.d(LOG_TAG, "Environment.getExternalStorageDirectory(): " +  Environment.getExternalStorageDirectory());
        Log.d(LOG_TAG, "Environment.getExternalStorageState(): " +  Environment.getExternalStorageState());
        //Log.d(LOG_TAG, "ExternalStorageDirectory: " + externalStorageDirectory);
        return externalStorageDirectory;
    }

    public String getStringPathFromArray(List<String> directories){
        String path = new String();
        for (String directory :directories){
            path += "/";
            path += directory;
        }
        return path;
    }

    public FileManager getFilesInLocation(){
        File directoryFile = new File(externalStorageDirectory);
        this.filesInLocation = directoryFile.listFiles();
        String[] filesString = directoryFile.list();

        File directory = new File(externalStorageDirectory);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                Log.d(LOG_TAG, "FileName:" + file.toString());
                //files.add(file);
            } else if (file.isDirectory()) {
                Log.d(LOG_TAG, "FileName file.getAbsolutePath():" + file.getAbsolutePath());
                //listf(file.getAbsolutePath(), files);
            }
        }
        return this;
    }

    public ArrayList<FileFolder> findFoldersInDirectory(String path){
        File currentDir = new File(path);
        ArrayList<FileFolder> fileFolderList = new ArrayList<>();
        try {
            File[] files = currentDir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    fileFolderList.add(new FileFolder(new FileFolder.Folder(file.getCanonicalPath(), file.getName(), file.getAbsolutePath())));
                    Log.d(LOG_TAG, "Folder:" + file.getName());
                } else {
                    fileFolderList.add(new FileFolder(new FileFolder.File(file.getCanonicalPath(), file.getName(), file.getAbsolutePath())));
                    Log.d(LOG_TAG, "File:" + file.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileFolderList;
    }


    public ArrayList<FileFolder> findFoldersInDirectory(String path) {
        ArrayList<FileFolder> fileFolderList = new ArrayList<>();
        File directory = new File(path);

        FileFilter directoryFileFilter = new FileFilter() {
            public boolean accept(File file) {
                Log.d(LOG_TAG, "FileName:" + file.toString());
                fileFolderList.add(new FileFolder(new FileFolder.File(file.getPath(), file.getPath(), file.getAbsolutePath())));
                //findFoldersInDirectory(file.toString());
                return file.isDirectory();
            }
        };

        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        if (directoryListAsFile != null) {
            ArrayList<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
            for (File directoryAsFile : directoryListAsFile) {
                //findFoldersInDirectory(directoryAsFile.getPath());
                Log.d(LOG_TAG, "directoryAsFile.getName():" + directoryAsFile.toString());
                Log.d(LOG_TAG, "directoryAsFile.getPath():" + directoryAsFile.getPath());
                fileFolderList.add(new FileFolder(new FileFolder.Folder(directoryAsFile.getPath(), directoryAsFile.getPath(), directoryAsFile.getAbsolutePath())));
                foldersInDirectory.add(directoryAsFile.getName());
            }
        }
        return fileFolderList;
    }



    public List<String> getFilesInLocationToString(){
        List<String>  filesString = new ArrayList<>();

        for (int i = 0; i < filesInLocation.length; i++){
            filesString.add(filesInLocation[i].getPath());
        }
        return filesString;
    }

    public void getStorageDirector(){
        // Use the current directory as title
        String path = Environment.getExternalStorageDirectory().toString();
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("Files", "Size: " + file.length);
        for (int i = 0; i < file.length; i++) {
            //here populate your listview
            Log.d("Files", "FileName:" + file[i].getName());

        }
    }
    */
}
