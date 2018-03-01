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
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.manager.FileManager;
import taras.clientwebsocketapp.utils.FileUtils;

/**
 * Created by Taras on 18.02.2018.
 */

public class FileManagerAdapter extends RecyclerView.Adapter<FileManagerAdapter.ViewHolder> {
    private static final String LOG_TAG = "myLogs";

    private Context mContext;
    FileManagerInterface fileManagerInterface;

    private ArrayList<File> fileList;
    private File directoryFile;

    public FileManagerAdapter(Context mContext, FileManagerInterface fileManagerInterface, String externalDirectory) {
        this.mContext = mContext;
        this.fileList = new ArrayList<>();
        //this.fileList.add(new File(externalDirectory));
        File file = new File(externalDirectory);
        this.fileList = new ArrayList<File>(Arrays.asList(file.listFiles()));
        this.fileManagerInterface = fileManagerInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_file_manager_item, parent, false);
        return new ViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file = fileList.get(position);
        String type = FileManager.getTypeFileFolder(file);// is file or folder

        switch (type){
            case FileManager.TYPE_FILE:
                holder.ivImage.setVisibility(View.GONE);
                break;
            case FileManager.TYPE_FOLDER:
                holder.ivImage.setVisibility(View.VISIBLE);
                holder.ivImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_folder));
                break;
        }
        holder.tvName.setText(file.getName());
        holder.cvItem.setOnClickListener(view -> {
            if (file.listFiles() == null){
                //file
                Log.d(LOG_TAG, file.getAbsolutePath() + " is file");
                FileUtils.openFile(mContext, file);
            } else {
                Log.d(LOG_TAG, file.getAbsolutePath() + " is folder");
                fileManagerInterface.moveNextDirectory(file.getAbsolutePath());
            }
        });
        holder.cvItem.setOnLongClickListener(view -> {
            return true;
        });
        holder.ivMore.setOnClickListener(view -> {
            fileManagerInterface.callFileInfo(file);
        });
    }


    public void setCurrentDirectory(String directory){
        directoryFile = new File(directory);
        this.fileList = new ArrayList<File>(Arrays.asList(directoryFile.listFiles()));
        notifyDataSetChanged();
    }
    public void setCurrentDirectory(File file){
        this.directoryFile = file;
        this.fileList = new ArrayList<File>(Arrays.asList(directoryFile.listFiles()));
        notifyDataSetChanged();
    }

    public void updateRecycler(){
        String directory = directoryFile.getPath();
        this.fileList = new ArrayList<File>(Arrays.asList(new File(directory).listFiles()));
        notifyDataSetChanged();
    }


    public void removeFilesList(int position){
        Log.d(LOG_TAG, "ArrayList<String> list, size: " + fileList.size());
        Log.d(LOG_TAG, "Position: " + position);
        ArrayList<File> newFileList = new ArrayList<>();
        for (int i = 0; i <= position; i++){
            newFileList.add(fileList.get(i));
        }
        fileList = newFileList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvItem)
        CardView cvItem;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.ivMore)
        ImageView ivMore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
