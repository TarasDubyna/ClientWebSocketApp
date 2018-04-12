package taras.clientwebsocketapp.screens.file_manager;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.manager.FileManager;
import taras.clientwebsocketapp.screens.view_holders.DirectoryHolder;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 18.02.2018.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryHolder> {
    private static final String LOG_TAG = "myLogs";

    public static final int FILE_MANAGER = 0;
    public static final int FAVORITE = 1;

    private int type;
    FileManagerInterface fileManagerInterface;

    private ArrayList<String> directoryList;

    public DirectoryAdapter(int type, FileManagerInterface fileManagerInterface) {
        this.type = type;
        this.fileManagerInterface = fileManagerInterface;

        this.directoryList = new ArrayList<>();
        if (type == FILE_MANAGER){
            this.directoryList.add(PreferenceUtils.getLocalStorageDirection());
        }
        if (type == FAVORITE){
            this.directoryList.add("favorite");
        }
    }

    @Override
    public DirectoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_directory_item, parent, false);
        return new DirectoryHolder(rootView, type);
    }


    @Override
    public void onBindViewHolder(DirectoryHolder holder, int position) {
        holder.bind(position, directoryList.get(position));
        holder.listener(getItemCount(), new DirectoryHolder.DirectoryHolderInterface() {
            @Override
            public void moveOnDirectory() {
                removeListToPosition(position);
                notifyDataSetChanged();
                fileManagerInterface.moveToDirectory(0);
            }
        });
    }


    @Override
    public int getItemCount() {
        return directoryList.size();
    }


    public void addItem(String directory){
        this.directoryList.add(directory);
        notifyItemInserted(directoryList.size() - 1);
    }
    public String getItem(int position){
        return directoryList.get(position);
    }
    public void removeListToPosition(int position){
        Log.d(LOG_TAG, "ArrayList<String> list, size: " + directoryList.size());
        Log.d(LOG_TAG, "Position: " + position);
        ArrayList<String> newFileList = new ArrayList<>();
        for (int i = 0; i <= position; i++){
            newFileList.add(directoryList.get(i));
        }
        directoryList = newFileList;
        notifyDataSetChanged();
    }
}