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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.screens.manager.FileManager;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.dialogs.FileInfoDialog;
import taras.clientwebsocketapp.screens.dialogs.FileInfoDialogInterface;

/**
 * Created by Taras on 17.02.2018.
 */

public class FileManagerFragment extends Fragment implements FileManagerInterface, FileInfoDialogInterface {
    private static final String LOG_TAG = "myLogs";


    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    @BindView(R.id.rvDirectories)
    RecyclerView rvDirectories;
    @BindView(R.id.tvEmptyFolder)
    TextView tvEmptyFolder;

    DirectoryAdapter adapterDirectories;
    FileManagerAdapter adapterFiles;

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
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterDirectories = new DirectoryAdapter(DirectoryAdapter.FILE_MANAGER, this);
        adapterFiles = new FileManagerAdapter(getContext(), this, FileManager.getManager(getContext()).getStartDirectory());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "FileManagerFragment, onCreateView");
        rootView = inflater.inflate(R.layout.fragment_file_manager, container, false);
        unbinder = ButterKnife.bind(this, rootView);

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
                setSelectedFileView(((MainActivity)getActivity()).getSelectedFileView(), () -> {adapterFiles.setFooterVisible(false);});
        Log.d(LOG_TAG, "FileManagerFragment, onResume");
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.files));
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

    @Override
    public void moveToDirectory(int position) {
        adapterFiles.setCurrentDirectory(adapterDirectories.getItem(position));
        adapterFiles.notifyDataSetChanged();


        adapterFiles.setCurrentDirectory(adapterDirectory.getDirectoryLast());
        rvFiles.setVisibility(View.VISIBLE);
        tvEmptyFolder.setVisibility(View.GONE);
    }

    @Override
    public void moveNextDirectory(String directory) {
        File file = new File(directory);
        adapterDirectories.addItem(directory);
        adapterFiles.setCurrentDirectory(directory);

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
        adapterFiles.updateRecycler();
    }

    @Override
    public void updateAfterFavorite() {
        adapterFiles.notifyDataSetChanged();
    }
}
