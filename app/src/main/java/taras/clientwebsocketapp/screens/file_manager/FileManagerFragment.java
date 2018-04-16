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
import android.widget.TextView;

import java.io.File;
import java.util.PrimitiveIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.screens.manager.FileManager;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.dialogs.FileInfoDialog;
import taras.clientwebsocketapp.screens.dialogs.FileInfoDialogInterface;

import static taras.clientwebsocketapp.utils.Constants.CONTENT_FAVORITE;
import static taras.clientwebsocketapp.utils.Constants.FILE_MANAGER_TYPE;

/**
 * Created by Taras on 17.02.2018.
 */

public class FileManagerFragment extends Fragment {
    private static final String LOG_TAG = "myLogs";


    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    @BindView(R.id.rvDirectories)
    RecyclerView rvDirectories;
    @BindView(R.id.tvEmptyFolder)
    TextView tvEmptyFolder;

    DirectoryAdapter adapterDirectories;
    FileManagerAdapter adapterFiles;

    private int fileManagerType;

    private View rootView;
    private Unbinder unbinder;

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
        adapterDirectories = new DirectoryAdapter(new DirectoryInterface() {
            @Override
            public void moveToDirectory(String directory) {
                if (directory != null){
                    adapterFiles.setNewDirectory(directory);
                    rvFiles.setVisibility(View.VISIBLE);
                    tvEmptyFolder.setVisibility(View.GONE);
                } else {
                    adapterFiles.setType(CONTENT_FAVORITE);
                }
            }
        });
        adapterFiles = new FileManagerAdapter(getContext(), new FileManagerAdapterInterface() {
            @Override
            public void callFileInfo(File file) {
                callInfo(file);
            }

            @Override
            public void moveNextDirectory(String newFileDirectory) {
                File file = new File(newFileDirectory);
                adapterDirectories.addItem(newFileDirectory);
                adapterFiles.setNewDirectory(newFileDirectory);

                if (file.listFiles().length > 0){
                    rvFiles.setVisibility(View.VISIBLE);
                    tvEmptyFolder.setVisibility(View.GONE);
                } else {
                    rvFiles.setVisibility(View.GONE);
                    tvEmptyFolder.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "FileManagerFragment, onCreateView");
        rootView = inflater.inflate(R.layout.fragment_file_manager, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (getArguments() != null){
            fileManagerType = getArguments().getInt(FILE_MANAGER_TYPE);
            adapterDirectories.setType(fileManagerType);
            adapterFiles.setType(fileManagerType);
        }
        initDirectoryRecyclers();
        initFileManagerRecyclers();

        return rootView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
            adapterFiles.setFooterVisible(true);
        }
        SelectedFileManager.getSelectedFileManager().setActivity(getActivity()).
                setSelectedFileView(((MainActivity)getActivity()).getSelectedFileView(), () -> {
                    adapterFiles.setFooterVisible(false);
                });
        Log.d(LOG_TAG, "FileManagerFragment, onResume");
        //((MainActivity) getActivity()).setToolbarTitle(getString(R.string.files));
    }

    private void initFileManagerRecyclers(){
        rvFiles.setHasFixedSize(true);
        rvFiles.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvFiles.setAdapter(adapterFiles);
    }
    private void initDirectoryRecyclers(){
        rvDirectories.setHasFixedSize(true);
        rvDirectories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDirectories.setAdapter(adapterDirectories);
    }

    private void callInfo(File file){
        FileInfoDialog fileInfoDialog = new FileInfoDialog();
        fileInfoDialog.setParamsInfo(file, new FileInfoDialogInterface() {
            @Override
            public void updateFileManagerRecyclerAll() {
                adapterFiles.updateRecycler();
            }

            @Override
            public void updateAfterFavorite() {
                adapterFiles.notifyDataSetChanged();
            }
        });
        fileInfoDialog.show(getFragmentManager(), "sdf");
    }
}
