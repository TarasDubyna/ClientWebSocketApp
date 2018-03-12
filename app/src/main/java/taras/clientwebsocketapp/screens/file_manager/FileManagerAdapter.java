package taras.clientwebsocketapp.screens.file_manager;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.manager.FileManager;
import taras.clientwebsocketapp.managers.FavoriteFilesManager;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.utils.FileUtils;

/**
 * Created by Taras on 18.02.2018.
 */

public class FileManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = "myLogs";

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private Context mContext;
    FileManagerInterface fileManagerInterface;

    private ArrayList<File> fileList;
    private File directoryFile;

    private boolean isFooterVisible = false;
    public FileManagerAdapter(Context mContext, FileManagerInterface fileManagerInterface, String externalDirectory) {
        this.mContext = mContext;
        this.fileList = new ArrayList<>();
        File file = new File(externalDirectory);
        this.fileList = new ArrayList<File>(Arrays.asList(file.listFiles()));
        this.fileManagerInterface = fileManagerInterface;
    }
    public FileManagerAdapter(Context mContext, FileManagerInterface fileManagerInterface, List<String> favoriteDirectoriesList) {
        this.mContext = mContext;
        this.fileList = new ArrayList<>();
        for (int i = 0; i < favoriteDirectoriesList.size(); i++){
            fileList.add(new File(favoriteDirectoriesList.get(i)));
        }
        this.fileManagerInterface = fileManagerInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_file_manager_item, parent, false);
            return new ItemViewHolder(view);
        } else if(viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_file_manager_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            File file = fileList.get(position);
            checkIsFavorite(holder, file);
            setItemType(holder, file);
            ((ItemViewHolder)holder).tvName.setText(file.getName());

            if (SelectedFileManager.getSelectedFileManager().isFileSelected(file)){
                ((ItemViewHolder)holder).cvItem.setCardBackgroundColor(mContext.getResources().getColor(R.color.blue_grey_500));
            } else {
                ((ItemViewHolder)holder).cvItem.setCardBackgroundColor(mContext.getResources().getColor(R.color.blue_grey_300));
            }

            ((ItemViewHolder)holder).ivMore.setOnClickListener(view -> {
                fileManagerInterface.callFileInfo(file);
            });
            ((ItemViewHolder)holder).cvItem.setOnClickListener(view -> {
                if (!SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                    SelectedFileManager.getSelectedFileManager().insertToSelectedFilesList(file);
                    if (SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                        setFooterVisible(false);
                        notifyDataSetChanged();
                    } else {
                        notifyItemChanged(position);
                    }
                } else {
                    if (file.listFiles() == null){
                        //file
                        Log.d(LOG_TAG, file.getAbsolutePath() + " is file");
                        FileUtils.openFile(mContext, file);
                    } else {
                        Log.d(LOG_TAG, file.getAbsolutePath() + " is folder");
                        fileManagerInterface.moveNextDirectory(file.getAbsolutePath());
                    }
                }
            });
            ((ItemViewHolder)holder).cvItem.setOnLongClickListener(view -> {
                if (SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                    SelectedFileManager.getSelectedFileManager().insertToSelectedFilesList(file);
                    setFooterVisible(true);
                    ((ItemViewHolder)holder).cvItem.setCardBackgroundColor(((ItemViewHolder)holder).cvItem.getContext().getResources().getColor(R.color.blue_grey_500));
                } else {
                    SelectedFileManager.getSelectedFileManager().removeAllSelectedFiles();
                    notifyDataSetChanged();
                }
                return true;
            });
        }
        if (holder instanceof FooterViewHolder){
            if (!SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                ((FooterViewHolder)holder).View.setVisibility(View.VISIBLE);
            } else {
                ((FooterViewHolder)holder).View.setVisibility(View.GONE);
            }
        }
    }

    private void checkIsFavorite(RecyclerView.ViewHolder holder, File file){
        if (FavoriteFilesManager.getInstance().isFavorite(file)){
            ((ItemViewHolder)holder).ivFavorite.setVisibility(View.VISIBLE);
        } else {
            ((ItemViewHolder)holder).ivFavorite.setVisibility(View.INVISIBLE);
        }
    }
    private void setItemType(RecyclerView.ViewHolder holder, File file){
        String type = FileManager.getTypeFileFolder(file);// is file or folder
        switch (type){
            case FileManager.TYPE_FILE:
                ((ItemViewHolder)holder).ivImage.setVisibility(View.GONE);
                break;
            case FileManager.TYPE_FOLDER:
                ((ItemViewHolder)holder).ivImage.setVisibility(View.VISIBLE);
                ((ItemViewHolder)holder).ivImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_folder));
                break;
        }
    }

    public void setFavoritesDirectory(List<String> favoritesDirectories){
        for (int i = 0; i < favoritesDirectories.size(); i++){
            this.fileList.add(new File(favoritesDirectories.get(i)));
        }
        //notifyDataSetChanged();
    }
    public void setCurrentDirectory(String directory){
        this.directoryFile = new File(directory);
        this.fileList = new ArrayList<File>(Arrays.asList(directoryFile.listFiles()));
        notifyDataSetChanged();
    }
    public void updateRecycler(){
        //String directory = this.directoryFile.getPath();
        this.fileList = new ArrayList<File>(Arrays.asList(new File(this.directoryFile.getPath()).listFiles()));
        notifyDataSetChanged();
    }
    public void setNewFileList(ArrayList<File> fileList){
        this.fileList = fileList;
        notifyDataSetChanged();
    }


    public void setFooterVisible(boolean visible){
        this.isFooterVisible = visible;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (isFooterVisible){
            return fileList.size() + 1;
        } else {
            return fileList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)){
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionFooter(int position) {
        if (!SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty() && (position == getItemCount() - 1)){
            return true;
        } else return false;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.llMain)
        LinearLayout llMain;
        @BindView(R.id.cvItem)
        CardView cvItem;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.ivFavorite)
        ImageView ivFavorite;
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.ivMore)
        ImageView ivMore;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public View View;
        public FooterViewHolder(View v) {
            super(v);
            View = v;
            // Add your UI Components here
        }

    }
}
