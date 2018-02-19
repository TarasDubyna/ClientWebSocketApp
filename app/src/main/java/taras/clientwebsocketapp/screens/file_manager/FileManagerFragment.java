package taras.clientwebsocketapp.screens.file_manager;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.file_manager.FileManager;
import taras.clientwebsocketapp.model.FileFolder;

/**
 * Created by Taras on 17.02.2018.
 */

public class FileManagerFragment extends Fragment implements FileManagerInterface {
    private static final String LOG_TAG = "myLogs";

    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    @BindView(R.id.rvDirectories)
    RecyclerView rvDirectories;
    @BindView(R.id.tvEmptyFolder)
    TextView tvEmptyFolder;

    private View rootView;
    private FileManagerAdapter fileManagerAdapter;
    private DirectoryAdapter directoryAdapter;

    //---------------------------------------------

    private ArrayList<String> directoryList;

    //---------------------------------------------

    private FileManager mFileManager;
    private ArrayList<File> mCurrentFilesList;


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
        File file = Environment.getExternalStorageDirectory();
        directoryList.add(file.getPath());

        mFileManager = new FileManager();

        mCurrentFilesList = mFileManager.setCurrentFile(file).getAllFiles();
        directoryAdapter = new DirectoryAdapter(getContext(), this, directoryList);
        fileManagerAdapter = new FileManagerAdapter(getContext(), this, mCurrentFilesList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "FileManagerFragment, onCreateView");
        rootView = inflater.inflate(R.layout.fragment_file_manager, container, false);
        ButterKnife.bind(this, rootView);
        initScanningRecycler();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "FileManagerFragment, onResume");
        directoryAdapter.addDirectoryList(directoryList);
        fileManagerAdapter.addFileList(mCurrentFilesList);
    }

    private void initScanningRecycler(){
        rvDirectories.setHasFixedSize(true);
        rvDirectories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDirectories.setAdapter(directoryAdapter);

        rvFiles.setHasFixedSize(true);
        rvFiles.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvFiles.setAdapter(fileManagerAdapter);
    }



    @Override
    public void getFolderWithFiles(String absolutePath) {
        Log.d(LOG_TAG, "FolderWithFiles: " + absolutePath);
        directoryList.add(absolutePath);
        mCurrentFilesList = mFileManager.createCurrentFile(absolutePath).getAllFiles();
        fileManagerAdapter.addFileList(mCurrentFilesList);
        directoryAdapter.notifyDataSetChanged();
        fileManagerAdapter.notifyDataSetChanged();
        rvDirectories.smoothScrollToPosition(directoryAdapter.getItemCount());
        tvEmptyFolder.setVisibility(View.GONE);
        rvFiles.setVisibility(View.VISIBLE);
    }

    @Override
    public void getFolderEmpty(String absolutePath) {
        Log.d(LOG_TAG, "FolderEmpty: " + absolutePath);
        directoryList.add(absolutePath);
        tvEmptyFolder.setVisibility(View.VISIBLE);
        rvFiles.setVisibility(View.GONE);
    }



    @Override
    public void getFilePathPosition(int position) {
        Log.d(LOG_TAG, "directoryList, size: " + directoryList.size());
        rvFiles.setVisibility(View.VISIBLE);
        mCurrentFilesList = mFileManager.createCurrentFile(directoryList.get(position)).getAllFiles();

        removeListToPosition(directoryList, position);
        fileManagerAdapter.addFileList(mCurrentFilesList);
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
