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
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.model.FileFolder;

/**
 * Created by Taras on 18.02.2018.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {

    private Context mContext;
    private FileManagerInterface fileManagerAdapterInterface;
    private ArrayList<String> directoryList;

    public DirectoryAdapter(Context mContext, FileManagerInterface fileManagerAdapterInterface, ArrayList<String> directoryList) {
        this.mContext = mContext;
        this.fileManagerAdapterInterface = fileManagerAdapterInterface;
        this.directoryList = directoryList;
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
            holder.tvText.setText(" ");
        } else {
            holder.tvText.setText(returnLastDirectory(directoryList.get(position)));
        }
        holder.cvItem.setOnClickListener(view -> {
            Log.d("myLogs", "directoryAdapter click on position: " + position);
            Log.d("myLogs", "directoryAdapter click, path " + directoryList.get(position));
            fileManagerAdapterInterface.getFilePathPosition(position);
        });
    }


    @Override
    public int getItemCount() {
        return directoryList.size();
    }

    public void clear(){
        this.directoryList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addFileFolderList(ArrayList<ArrayList<FileFolder>> directoryMap){
        this.directoryList = directoryList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvItem)
        CardView cvItem;
        @BindView(R.id.tvText)
        TextView tvText;

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