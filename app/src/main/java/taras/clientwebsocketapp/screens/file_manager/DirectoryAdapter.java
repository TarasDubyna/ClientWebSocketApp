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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.AppApplication;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.interfaces.DirectoryAdapterInterface;

/**
 * Created by Taras on 18.02.2018.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {
    private static final String LOG_TAG = "myLogs";

    private Context mContext;

    FileManagerInterface fileManagerInterface;

    private ArrayList<String> directoryList;
    private String fullDirectory;

    public DirectoryAdapter(Context mContext, FileManagerInterface fileManagerInterface) {
        this.mContext = mContext;
        this.fileManagerInterface = fileManagerInterface;
        this.directoryList = new ArrayList<>();
    }

    public void addStartDirectory(String startDirectory){
        this.directoryList.add(startDirectory);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_directory_item, parent, false);
        return new ViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (directoryList.get(position).equals(AppApplication.externalStorageDir.getAbsolutePath())){
            holder.tvText.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.ivImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobile_phone));
        } else {
            holder.ivImage.setVisibility(View.GONE);
            holder.tvText.setVisibility(View.VISIBLE);
            holder.tvText.setText(returnLastDirectory(directoryList.get(position)));
        }
        holder.cvItem.setOnClickListener(view -> {
            Log.d("myLogs", "directoryAdapter click on position: " + position);
            Log.d("myLogs", "directoryAdapter click, path " + directoryList.get(position));
            fileManagerInterface.returnToPosition(position);

            /*
            if (position != getItemCount()){
                Log.d("myLogs", "directoryAdapter click on position: " + position);
                Log.d("myLogs", "directoryAdapter click, path " + directoryList.get(position));
                removeListToPosition(position);
                notifyDataSetChanged();
                directoryAdapterInterface.moveToDirectoryPosition(position);
            }
             */
        });
    }


    public void removeToPosition(int position){
        removeListToPosition(position);
        notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return directoryList.size();
    }


    public void addDirectory(String directory){
        this.directoryList.add(directory);
        notifyDataSetChanged();
    }

    public String getDirectoryOfListByPosition(int position){
        return directoryList.get(position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvItem)
        CardView cvItem;
        @BindView(R.id.tvText)
        TextView tvText;
        @BindView(R.id.ivImage)
        ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String returnLastDirectory(String path){
        String[] array = path.split("/+");
        return array[array.length - 1];
    }

}