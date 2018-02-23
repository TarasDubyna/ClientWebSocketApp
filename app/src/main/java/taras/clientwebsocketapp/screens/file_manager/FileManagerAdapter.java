package taras.clientwebsocketapp.screens.file_manager;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import taras.clientwebsocketapp.utils.OpenFileUtils;

/**
 * Created by Taras on 18.02.2018.
 */

public class FileManagerAdapter extends RecyclerView.Adapter<FileManagerAdapter.ViewHolder> {

    public interface FileManagerAdapterInterface{
        void moveToNextFolder(String path);
    }

    private Context mContext;
    FileManagerAdapterInterface fileManagerAdapterInterface;

    private ArrayList<File> fileList;
    private String directory;

    public FileManagerAdapter(Context mContext, FileManagerAdapterInterface fileManagerAdapterInterface,  String directory) {
        this.mContext = mContext;
        this.directory = directory;
        File directoryFile = new File(directory);
        this.fileList = new ArrayList(Arrays.asList(directoryFile.listFiles()));
        this.fileManagerAdapterInterface = fileManagerAdapterInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_file_manager_item, parent, false);
        return new ViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file =fileList.get(position);

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

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileManager fileManager = FileManager.getManager(mContext);
                String absolutePath = file.getAbsolutePath();
                ArrayList<File> files = fileManager.createCurrentFile(absolutePath).getAllFiles();

                if (fileManager.isFile(files)){
                    //file
                    OpenFileUtils.openFile(mContext, file);
                } else {
                    if (fileManager.isEmptyFolder(files)){
                        //empty
                    } else {
                        // not empty
                        fileManagerAdapterInterface.moveToNextFolder(absolutePath);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public void clear(){
        this.fileList = new ArrayList<>();
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvItem)
        CardView cvItem;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.ivImage)
        ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
