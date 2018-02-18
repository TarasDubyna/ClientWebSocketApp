package taras.clientwebsocketapp.screens.file_manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.file_manager.FileManager;
import taras.clientwebsocketapp.model.FileFolder;
import taras.clientwebsocketapp.screens.scann_network.DevicesRecyclerAdapter;

import static taras.clientwebsocketapp.file_manager.FileManager.getExternalStorageDirectory;

/**
 * Created by Taras on 17.02.2018.
 */

public class FileManagerFragment extends Fragment implements FileManagerInterface {
    private static final String LOG_TAG = "myLogs";


    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    @BindView(R.id.rvDirectories)
    RecyclerView rvDirectories;

    private View rootView;
    private FileManagerAdapter fileManagerAdapter;
    private DirectoryAdapter directoryAdapter;

    private ArrayList<ArrayList<FileFolder>> allFilesDirectoryList;

    private ArrayList<String> directoryList;


    private static FileManagerFragment fileManagerFragment;
    public static FileManagerFragment getFragment(){
        if (fileManagerFragment == null){
            fileManagerFragment = new FileManagerFragment();
        }
        return fileManagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "FileManagerFragment, onCreate");
        directoryList = new ArrayList<>();
        allFilesDirectoryList = new ArrayList<>();

        getFilesOfDirectory("/");

        directoryAdapter = new DirectoryAdapter(getContext(), this, directoryList);
        fileManagerAdapter = new FileManagerAdapter(getContext(), this, allFilesDirectoryList.get(0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_file_manager, container, false);
        ButterKnife.bind(this, rootView);
        initScanningRecycler();
        return rootView;
    }

    private void initScanningRecycler(){
        rvDirectories.setHasFixedSize(true);
        rvDirectories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDirectories.setAdapter(directoryAdapter);


        rvFiles.setHasFixedSize(true);
        rvFiles.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvFiles.setAdapter(fileManagerAdapter);
    }

    private void getFilesOfDirectory(String path){
        FileManager fileManager = new FileManager();
        fileManager.getAllFiles(new File(path));
        allFilesDirectoryList.add(fileManager.findFoldersInDirectory(path));

        directoryList.add(path);
    }

    @Override
    public void getFilePathLast(String path) {
        getFilesOfDirectory(path);
        fileManagerAdapter.addFileFolderList(allFilesDirectoryList.get(allFilesDirectoryList.size() - 1));
        directoryAdapter.notifyDataSetChanged();
        fileManagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void getFilePathPosition(String path, int position) {
        Log.d(LOG_TAG, "directoryList, size: " + directoryList.size());
        getFilesOfDirectory(path);
        removeListToPosition(directoryList, position);
        fileManagerAdapter.addFileFolderList(allFilesDirectoryList.get(position));
        directoryAdapter.notifyDataSetChanged();
        fileManagerAdapter.notifyDataSetChanged();
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
}
