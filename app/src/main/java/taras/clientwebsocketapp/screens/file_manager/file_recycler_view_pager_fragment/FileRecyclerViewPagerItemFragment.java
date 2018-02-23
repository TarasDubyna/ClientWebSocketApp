package taras.clientwebsocketapp.screens.file_manager.file_recycler_view_pager_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.file_manager.FileManagerAdapter;

/**
 * Created by Taras on 23.02.2018.
 */

public class FileRecyclerViewPagerItemFragment extends Fragment {

    private View rootView;
    private RecyclerView directoryFilesRecyclerView;

    private FileManagerAdapter.FileManagerAdapterInterface fileManagerAdapterInterface;
    private String directory;

    public FileRecyclerViewPagerItemFragment() {
        // Required empty public constructor
    }

    public void setParams(FileManagerAdapter.FileManagerAdapterInterface fileManagerAdapterInterface, String directory){
        this.fileManagerAdapterInterface = fileManagerAdapterInterface;
        this.directory = directory;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.file_manager_recycler, container, false);
        directoryFilesRecyclerView = rootView.findViewById(R.id.rvFiles);
        directoryFilesRecyclerView.setHasFixedSize(false);
        directoryFilesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        FileManagerAdapter fileManagerAdapter = new FileManagerAdapter(getContext(), fileManagerAdapterInterface, directory);
        directoryFilesRecyclerView.setAdapter(fileManagerAdapter);
        return rootView;
    }

}
