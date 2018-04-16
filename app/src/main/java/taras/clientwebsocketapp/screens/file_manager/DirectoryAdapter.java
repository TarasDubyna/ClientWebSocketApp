package taras.clientwebsocketapp.screens.file_manager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.view_holders.DirectoryHolder;
import taras.clientwebsocketapp.utils.PreferenceUtils;

import static taras.clientwebsocketapp.utils.Constants.CONTENT_FAVORITE;
import static taras.clientwebsocketapp.utils.Constants.CONTENT_USUAL;

/**
 * Created by Taras on 18.02.2018.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryHolder> {
    private static final String LOG_TAG = "myLogs";

    private int type;
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
    public DirectoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_directory_item, parent, false);
        return new DirectoryHolder(rootView, type);
    }


    @Override
    public void onBindViewHolder(DirectoryHolder holder, int position) {
        holder.bind(position, directoryList.get(position));
        holder.listener(getItemCount(), new DirectoryInterface() {
            @Override
            public void moveToDirectory(String directory) {
                Log.d(LOG_TAG, "moveToDirectory: " + directory);
                Log.d(LOG_TAG, "position: " + position);
                removeListToPosition(position);
                directoryInterface.moveToDirectory(directory);
                notifyDataSetChanged();
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