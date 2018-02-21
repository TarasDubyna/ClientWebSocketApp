package taras.clientwebsocketapp.screens.file_manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.manager.FileManager;
import taras.clientwebsocketapp.manager.UpdateGuiRecyclersInterface;
import taras.clientwebsocketapp.manager.back_press_manager.BackPressManager;
import taras.clientwebsocketapp.screens.MainActivity;

/**
 * Created by Taras on 17.02.2018.
 */

public class FileManagerFragment extends Fragment implements UpdateGuiRecyclersInterface {
    private static final String LOG_TAG = "myLogs";

    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    @BindView(R.id.rvDirectories)
    RecyclerView rvDirectories;
    @BindView(R.id.tvEmptyFolder)
    TextView tvEmptyFolder;

    private View rootView;

    //---------------------------------------------




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


        FileManager.getManager(getContext())
                .initUpdateGuiRecyclersInterface(this)
                .initFileManagerAdapter()
                .initDirectoryAdapter()
                .done();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "FileManagerFragment, onCreateView");
        rootView = inflater.inflate(R.layout.fragment_file_manager, container, false);
        ButterKnife.bind(this, rootView);
        initFileManagerRecyclers();
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "FileManagerFragment, onResume");
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button
                    BackPressManager.getBackPressManager(getActivity()).checkCurrentFragment(((MainActivity) getActivity()).getCurrentFragmentClass());
                    return true;

                }

                return false;
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initFileManagerRecyclers(){
        rvDirectories.setHasFixedSize(true);
        rvDirectories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDirectories.setAdapter(FileManager.getManager(getContext()).getDirectoryAdapter());

        rvFiles.setHasFixedSize(true);
        rvFiles.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvFiles.setAdapter(FileManager.getManager(getContext()).getFileManagerAdapter());
    }

    @Override
    public void updateFilesRecyclerIfFolderEmpty() {
        tvEmptyFolder.setVisibility(View.VISIBLE);
        rvFiles.setVisibility(View.GONE);
    }

    @Override
    public void updateFilesRecyclerIfFolderNotEmpty() {;
        rvDirectories.smoothScrollToPosition(FileManager.getManager(getContext()).getDirectoryAdapter().getItemCount());
        tvEmptyFolder.setVisibility(View.GONE);
        rvFiles.setVisibility(View.VISIBLE);
    }
}
