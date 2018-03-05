package taras.clientwebsocketapp.screens.favorite;

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
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.manager.FileManager;
import taras.clientwebsocketapp.managers.FavoriteFilesManager;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.dialogs.FileInfoDialog;
import taras.clientwebsocketapp.screens.dialogs.FileInfoDialogInterface;
import taras.clientwebsocketapp.screens.file_manager.DirectoryAdapter;
import taras.clientwebsocketapp.screens.file_manager.FileManagerAdapter;
import taras.clientwebsocketapp.screens.file_manager.FileManagerFragment;
import taras.clientwebsocketapp.screens.file_manager.FileManagerInterface;

/**
 * Created by Taras on 04.03.2018.
 */

public class FavoriteFragment extends Fragment implements FileManagerInterface, FileInfoDialogInterface {
    private static final String LOG_TAG = "myLogs";

    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    @BindView(R.id.rvDirectories)
    RecyclerView rvDirectories;
    @BindView(R.id.tvEmptyFolder)
    TextView tvEmptyFolder;

    DirectoryAdapter directoryAdapter;
    FileManagerAdapter fileManagerAdapter;
    private int lastPageSelected;

    private View rootView;

    //---------------------------------------------

    public FavoriteFragment() {
    }




    /*
    private static FavoriteFragment favoriteFragment;
    public static FavoriteFragment getFragment(){
        if (favoriteFragment == null){
            favoriteFragment = new FavoriteFragment();
        }
        return favoriteFragment;
    }
    */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "FileManagerFragment, onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "FileManagerFragment, onCreateView");
        rootView = inflater.inflate(R.layout.fragment_file_manager, container, false);
        ButterKnife.bind(this, rootView);

        initDirectoryRecyclers();
        initFileManagerRecyclers();

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "FileManagerFragment, onResume");
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.favorite));
    }

    private void initFileManagerRecyclers(){
        fileManagerAdapter = new FileManagerAdapter(getContext(), this, FavoriteFilesManager.getInstance().getAllStringFavorites());
        rvFiles.setHasFixedSize(true);
        rvFiles.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvFiles.setAdapter(fileManagerAdapter);
    }

    private void initDirectoryRecyclers(){
        directoryAdapter = new DirectoryAdapter(DirectoryAdapter.FAVORITE, getContext(), this);
        rvDirectories.setHasFixedSize(true);
        rvDirectories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDirectories.setAdapter(directoryAdapter);
    }

    @Override
    public void returnToPosition(int position) {
        directoryAdapter.removeListToPosition(position);
        if (position == 0){
            fileManagerAdapter.setFavoritesDirectory(FavoriteFilesManager.getInstance().getAllStringFavorites());
        } else {
            fileManagerAdapter.setCurrentDirectory(directoryAdapter.getDirectoryOfListByPosition(position));
        }
        rvFiles.setVisibility(View.VISIBLE);
        tvEmptyFolder.setVisibility(View.GONE);
    }

    @Override
    public void moveNextDirectory(String directory) {
        File file = new File(directory);
        directoryAdapter.addDirectory(directory);
        fileManagerAdapter.setCurrentDirectory(directory);
        if (file.listFiles().length > 0){
            rvFiles.setVisibility(View.VISIBLE);
            tvEmptyFolder.setVisibility(View.GONE);
        } else {
            rvFiles.setVisibility(View.GONE);
            tvEmptyFolder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void callFileInfo(File file) {
        FileInfoDialog fileInfoDialog = new FileInfoDialog();
        fileInfoDialog.setParamsInfo(file, this);
        fileInfoDialog.show(getFragmentManager(), "sdf");
    }

    @Override
    public void updateFileManagerRecyclerAll() {
        fileManagerAdapter.updateRecycler();
    }
}