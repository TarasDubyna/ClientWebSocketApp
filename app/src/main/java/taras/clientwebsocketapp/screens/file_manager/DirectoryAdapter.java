package taras.clientwebsocketapp.screens.file_manager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.file_manager.view_holders.DirectoryHeaderHolder;
import taras.clientwebsocketapp.screens.view_holders.DirectoryHolder;
import taras.clientwebsocketapp.utils.PreferenceUtils;

import static taras.clientwebsocketapp.utils.Constants.CONTENT_FAVORITE;
import static taras.clientwebsocketapp.utils.Constants.CONTENT_USUAL;

/**
 * Created by Taras on 18.02.2018.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = DirectoryAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    private int type;
    private int memoryType;
    DirectoryInterface directoryInterface;


    private ArrayList<String> directoryList;


    public DirectoryAdapter(DirectoryInterface directoryInterface) {
        this.directoryInterface = directoryInterface;
        this.directoryList = new ArrayList<>();
    }

    public void setType(int type){
        this.type = type;
        if (type == CONTENT_USUAL){
            this.directoryList.add(PreferenceUtils.getLocalStorageDirection());
        }
        if (type == CONTENT_FAVORITE){
            this.directoryList.add("favorite");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER){
            View headerView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_directory_header, parent, false);
            return new DirectoryHeaderHolder(headerView, type, memoryType);
        } else if (viewType == TYPE_ITEM){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_directory_item, parent, false);
            return new DirectoryHolder(itemView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DirectoryHolder){
            ((DirectoryHolder) holder).bind(directoryList.get(position));
            ((DirectoryHolder) holder).onRowClicked(new DirectoryInterface() {
                @Override
                public void moveToDirectory(String directory) {
                    Log.d(LOG_TAG, "moveToDirectory: " + directory + " ,directory position: " + position);
                    removeListToPosition(position);
                    notifyDataSetChanged();
                    directoryInterface.moveToDirectory(directory);
                }
                @Override
                public void goToZeroPosition(String directory) {

                }
                @Override
                public void changeTypeMemory(String directory, int memType) {
                }
            });
        } else if (holder instanceof DirectoryHeaderHolder){
            ((DirectoryHeaderHolder) holder).onRowClicked(directoryList.size(), new DirectoryInterface() {
                @Override
                public void moveToDirectory(String directory) {

                }

                @Override
                public void goToZeroPosition(String directory) {
                    Log.d(LOG_TAG, "directory, goToZeroPosition: " + directory);
                    removeListToPosition(0);
                    notifyDataSetChanged();
                    directoryInterface.moveToDirectory(directory);
                }

                @Override
                public void changeTypeMemory(String directory, int memType) {
                    if (type == CONTENT_USUAL){
                        Log.d(LOG_TAG, "directory, changeTypeMemory: " + directory);
                        memoryType = memType;
                        notifyDataSetChanged();
                        directoryInterface.moveToDirectory(directory);
                    }
                }
            });
        }

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