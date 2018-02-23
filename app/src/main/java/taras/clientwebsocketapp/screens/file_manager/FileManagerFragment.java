package taras.clientwebsocketapp.screens.file_manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.manager.FileManager;
import taras.clientwebsocketapp.screens.interfaces.DirectoryAdapterInterface;

/**
 * Created by Taras on 17.02.2018.
 */

public class FileManagerFragment extends Fragment implements DirectoryAdapterInterface, FileManagerAdapter.FileManagerAdapterInterface {
    private static final String LOG_TAG = "myLogs";

    /*
    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    */
    @BindView(R.id.rvDirectories)
    RecyclerView rvDirectories;
    @BindView(R.id.tvEmptyFolder)
    TextView tvEmptyFolder;

    @BindView(R.id.vpFileManagerFragment)
    ViewPager vpDirectoryContent;
    FileManagerViewPagerAdapter fileManagerViewPagerAdapter;

    DirectoryAdapter directoryAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "FileManagerFragment, onCreateView");
        rootView = inflater.inflate(R.layout.fragment_file_manager, container, false);
        ButterKnife.bind(this, rootView);
        initFileManagerRecyclers();
        fileManagerViewPagerAdapter = new FileManagerViewPagerAdapter(getFragmentManager(), getContext(), this)
                .setStartDirectory(FileManager.getManager(getContext()).getStartDirectory());
        vpDirectoryContent.setAdapter(fileManagerViewPagerAdapter);
        vpDirectoryContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Log.d(LOG_TAG, "onPageScrolled, position: " + position + " , directoryAdapter.size: " + directoryAdapter.getItemCount());
                directoryAdapter.setCurrentDirectoryPosition(position);
                vpDirectoryContent.setCurrentItem(position, true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(LOG_TAG, "onPageScrollStateChanged: " + state);
            }
        });

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "FileManagerFragment, onResume");
    }



    private void initFileManagerRecyclers(){
        directoryAdapter = new DirectoryAdapter(getContext(), this);
        directoryAdapter.addStartDirectory(FileManager.getManager(getContext()).getStartDirectory());
        rvDirectories.setHasFixedSize(true);
        rvDirectories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDirectories.setAdapter(directoryAdapter);
    }


    //Click directory recycler position
    @Override
    public void moveToPosition(int position) {
        Log.d(LOG_TAG, "moveToPosition: " + position);
        directoryAdapter.setCurrentDirectoryPosition(position);
        fileManagerViewPagerAdapter.removeListToPosition(position);
        vpDirectoryContent.setCurrentItem(position, true);
    }

    @Override
    public void moveToNextFolder(String path) {
        directoryAdapter.addCurrentDirectory(path);
        fileManagerViewPagerAdapter.addDirectory(path);
        vpDirectoryContent.setCurrentItem(directoryAdapter.getItemCount(),true);
    }

}
