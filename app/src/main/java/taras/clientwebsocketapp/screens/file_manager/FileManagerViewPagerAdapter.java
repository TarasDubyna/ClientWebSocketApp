package taras.clientwebsocketapp.screens.file_manager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.manager.FileManager;
import taras.clientwebsocketapp.screens.file_manager.file_recycler_view_pager_fragment.FileRecyclerViewPagerItemFragment;

/**
 * Created by Taras on 22.02.2018.
 */

public class FileManagerViewPagerAdapter extends FragmentPagerAdapter {
    private static final String LOG_TAG = "myLogs";

    private Context mContext;

    private String directory;
    private ArrayList<String> directoriesList;
    private FileManagerAdapter.FileManagerAdapterInterface fileManagerAdapterInterface;

    public FileManagerViewPagerAdapter(FragmentManager fragmentManager, Context mContext, FileManagerAdapter.FileManagerAdapterInterface fileManagerAdapterInterface) {
        super(fragmentManager);
        this.mContext = mContext;
        this.fileManagerAdapterInterface = fileManagerAdapterInterface;
        this.directoriesList = new ArrayList<>();
    }

    public FileManagerViewPagerAdapter setStartDirectory(String startDirectory){
        directoriesList.add(startDirectory);
        return this;
    }

    @Override
    public int getCount() {
        return directoriesList.size();
    }

    @Override
    public Fragment getItem(int position) {
        FileRecyclerViewPagerItemFragment fileRecyclerViewPagerItemFragment = new FileRecyclerViewPagerItemFragment();
        fileRecyclerViewPagerItemFragment.setParams(fileManagerAdapterInterface, directoriesList.get(position));
        return fileRecyclerViewPagerItemFragment;
    }

    public void setDirectoriesList(ArrayList<String> directoriesList) {
        this.directoriesList = directoriesList;
    }

    public void addDirectory(String directory){
        this.directoriesList.add(directory);
        notifyDataSetChanged();
    }

    public void removeListToPosition(int position){
        Log.d(LOG_TAG, "ArrayList<String> list, size: " + directoriesList.size());
        Log.d(LOG_TAG, "Position: " + position);

        for (int i = directoriesList.size() - 1; i > 0; i--){
            Log.d(LOG_TAG, "list item: " + directoriesList.get(i));
            Log.d(LOG_TAG, "list item position: " + i);
            if (directoriesList.size() > position + 1){
                directoriesList.remove(i);
            }
        }
    }
}
