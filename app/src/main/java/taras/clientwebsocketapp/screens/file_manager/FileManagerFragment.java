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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.custom_views.selected_file_view.SelectedFileView;
import taras.clientwebsocketapp.custom_views.selected_file_view.SelectedFileViewCallback;
import taras.clientwebsocketapp.managers.PermissionManager;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.model.FileManagerHolderClickCallback;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.dialogs.scanning_for_sending.ScanningForSendingDialog;
import taras.clientwebsocketapp.screens.dialogs.file_info_dialog.FileInfoDialog;
import taras.clientwebsocketapp.screens.dialogs.file_info_dialog.FileInfoDialogInterface;
import taras.clientwebsocketapp.screens.dialogs.scanning_for_sending.ScanningForSendingDialogCallback;
import taras.clientwebsocketapp.utils.EventBusMsg;

import static taras.clientwebsocketapp.utils.Constants.FILE_MANAGER_TYPE;

/**
 * Created by Taras on 17.02.2018.
 */

public class FileManagerFragment extends Fragment {
    private static final String LOG_TAG = FileManagerFragment.class.getSimpleName();


    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    @BindView(R.id.rvDirectories)
    RecyclerView rvDirectories;
    @BindView(R.id.tvEmptyFolder)
    TextView tvEmptyFolder;
    @BindView(R.id.selectedFileView)
    SelectedFileView selectedFileView;

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
                Log.d(LOG_TAG, "moveToDirectory: " + directory);
                if (directory != null){
                    adapterFiles.setNewDirectory(directory);
                } else {
                    adapterFiles.setType(fileManagerType);
                }
                rvFiles.setVisibility(View.VISIBLE);
                tvEmptyFolder.setVisibility(View.GONE);
            }

            @Override
            public void goToZeroPosition(String directory) {

            }

            @Override
            public void changeTypeMemory(String directory, int memoryType) {

            }
        });
        adapterFiles = new FileManagerAdapter(getContext(), fileManagerHolderClickCallback);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        selectedFileView.setCallback(selectedFileViewCallback);

        adapterFiles.setType(fileManagerType);
        rvFiles.setVisibility(View.VISIBLE);
        tvEmptyFolder.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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

        selectedFileView.setCallback(selectedFileViewCallback);
        initDirectoryRecyclers();
        initFileManagerRecyclers();

        return rootView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        SelectedFileManager.getSelectedFileManager().removeAllSelectedFiles();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "FileManagerFragment, onResume");
        adapterFiles.setNewDirectory(adapterDirectories.getItem(adapterDirectories.getItemCount() - 1));
    }



    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getPermissionResult(EventBusMsg<Object> ebMessage) {
        Log.d(LOG_TAG, "getPermissionResult");
        if (ebMessage.getCodeDirection() == EventBusMsg.TO_APP
                && ebMessage.getCodeType() == EventBusMsg.PACKAGE_PERMISSION){
            ((MainActivity) getActivity()).hideWaitingPermissionDialog((PermissionPackage) ebMessage.getModel());
        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
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
                if (adapterDirectories.getItemCount() == 1){
                    adapterFiles.setType(fileManagerType);
                } else {
                    adapterFiles.updateRecycler();
                }
            }

            @Override
            public void updateAfterFavorite() {
                adapterFiles.notifyDataSetChanged();
            }
        });
        fileInfoDialog.show(getFragmentManager(), "sdf");
    }



    private SelectedFileViewCallback selectedFileViewCallback = new SelectedFileViewCallback() {
        @Override
        public void clickShare() {
            Log.d(LOG_TAG, "SelectedFileViewCallback, clickShare");
            if (!SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                ScanningForSendingDialog dialog = new ScanningForSendingDialog();
                dialog.setCallback(new ScanningForSendingDialogCallback() {
                    @Override
                    public void sendPermission(PermissionPackage pack) {
                        PermissionManager.getPermissionManager().addToPermissionManager(PermissionManager.CLIENT, pack);
                        EventBusMsg<PermissionPackage> message =
                                new EventBusMsg<PermissionPackage>(EventBusMsg.TO_SERVICE, EventBusMsg.PACKAGE_PERMISSION, pack);
                        EventBus.getDefault().postSticky(message);

                        ((MainActivity) getActivity()).showWaitingPermissionDialog(pack);
                    }
                });
                dialog.show(getFragmentManager(), ScanningForSendingDialog.class.getSimpleName());
            }
        }
        @Override
        public void clickCancel() {
            Log.d(LOG_TAG, "SelectedFileViewCallback, clickCancel");
            SelectedFileManager.getSelectedFileManager().removeAllSelectedFiles();
            selectedFileView.setSelectedNum(SelectedFileManager
                    .getSelectedFileManager()
                    .getAllSelectedDirectories().size());
            selectedFileView.setVisibility(View.GONE);
            adapterFiles.setFooterVisible(false);
        }
    };
    private FileManagerHolderClickCallback fileManagerHolderClickCallback = new FileManagerHolderClickCallback() {
        @Override
        public void longClick(int position) {
            if (SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                if (!adapterFiles.getItem(position).isDirectory()){
                    SelectedFileManager.getSelectedFileManager().insertToSelectedFilesList(adapterFiles.getItem(position));
                    adapterFiles.setFooterVisible(true);
                    selectedFileView.setVisibility(View.VISIBLE);
                }
            } else {
                SelectedFileManager.getSelectedFileManager().removeAllSelectedFiles();
                adapterFiles.notifyDataSetChanged();
            }
            selectedFileView.setSelectedNum(SelectedFileManager.getSelectedFileManager().getAllSelectedDirectories().size());
        }

        @Override
        public void shortClick(int position) {
            if (SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                File file = adapterFiles.getItem(position);
                adapterDirectories.addItem(file.getAbsolutePath());
                rvDirectories.scrollToPosition(adapterDirectories.getItemCount() - 1);
                adapterFiles.setNewDirectory(file.getAbsolutePath());
                if (file.listFiles().length > 0){
                    rvFiles.setVisibility(View.VISIBLE);
                    tvEmptyFolder.setVisibility(View.GONE);
                } else {
                    rvFiles.setVisibility(View.GONE);
                    tvEmptyFolder.setVisibility(View.VISIBLE);
                }
            } else {
                if (!adapterFiles.getItem(position).isDirectory()){
                    SelectedFileManager.getSelectedFileManager().insertToSelectedFilesList(adapterFiles.getItem(position));
                    selectedFileView.setSelectedNum(SelectedFileManager.getSelectedFileManager().getAllSelectedDirectories().size());
                    adapterFiles.notifyItemChanged(position);
                    if (SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                        selectedFileView.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public void moreInfoClick(File file) {
            callInfo(file);
        }
    };



}
