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

/**
 * Created by Taras on 18.02.2018.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {
    private static final String LOG_TAG = "myLogs";

    public static final String FILE_MANAGER = "FILE_MANAGER";
    public static final String FAVORITE = "FAVORITE";

    private String type;
    private Context mContext;
    FileManagerInterface fileManagerInterface;

    private ArrayList<String> directoryList;
    private String fullDirectory;

    public DirectoryAdapter(String type, Context mContext, FileManagerInterface fileManagerInterface) {
        this.type = type;
        this.mContext = mContext;
        this.fileManagerInterface = fileManagerInterface;
        this.directoryList = new ArrayList<>();
        if (type.equals(FILE_MANAGER)){
            this.directoryList.add(FileManager.getManager(mContext).getStartDirectory());
        }
        if (type.equals(FAVORITE)){
            this.directoryList.add("favorite");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_directory_item, parent, false);
        return new ViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0){
            initZeroPosition(holder);
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

    public String getDirectoryLast(){
        return directoryList.get(directoryList.size() - 1);
    }

    private void initZeroPosition(ViewHolder holder){
        switch (type){
            case FILE_MANAGER:
                holder.tvText.setVisibility(View.GONE);
                holder.ivImage.setVisibility(View.VISIBLE);
                holder.ivImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobile_phone));
                break;
            case FAVORITE:
                holder.tvText.setVisibility(View.GONE);
                holder.ivImage.setVisibility(View.VISIBLE);
                holder.ivImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star));
                break;
        }
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