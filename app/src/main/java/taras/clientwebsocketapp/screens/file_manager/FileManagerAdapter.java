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

import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.FavoriteFilesManager;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.manager.FileManager;
import taras.clientwebsocketapp.screens.view_holders.FileManagerHolder;
import taras.clientwebsocketapp.screens.view_holders.FooterHolder;
import taras.clientwebsocketapp.utils.FileUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

import static taras.clientwebsocketapp.utils.Constants.CONTENT_FAVORITE;
import static taras.clientwebsocketapp.utils.Constants.CONTENT_USUAL;

/**
 * Created by Taras on 18.02.2018.
 */

public class FileManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = "myLogs";

    public static final int CONTENT_USUAL = 0;
    public static final int CONTENT_FAVORITE = 1;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private Context context;
    private FileManagerAdapterInterface fileManagerAdapterInterface;

    private boolean isPrepareToSend = false;

    private ArrayList<File> fileList;
    private File directoryFile;

    private boolean isFooterVisible = false;
    public FileManagerAdapter(Context context, FileManagerAdapterInterface fileManagerAdapterInterface) {
        this.fileList = new ArrayList<>();
        this.context = context;
        this.fileManagerAdapterInterface = fileManagerAdapterInterface;
    }

    public void setType(int type){
        if (type == CONTENT_USUAL){
            File file = new File(FileManager.getManager(context).getStartDirectory());
            this.fileList.clear();
            this.fileList = new ArrayList<File>(Arrays.asList(file.listFiles()));
        }
        if (type == CONTENT_FAVORITE){
            this.fileList.clear();
            List<String> favoriteDirectories = FavoriteFilesManager.getInstance().getAllStringFavorites();
            for (String directory: favoriteDirectories){
                this.fileList.add(new File(directory));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_file_manager_item, parent, false);
            return new FileManagerHolder(view);
        } else if(viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_file_manager_footer, parent, false);
            return new FooterHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FileManagerHolder){
            ((FileManagerHolder)holder).bind(fileList.get(position));
            ((FileManagerHolder)holder).onRowClicked(fileList.get(position),new FileManagerInterface() {
                @Override
                public void longItemClick(int position) {
                    fileManagerAdapterInterface.longClick(position);

                    if (SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                        SelectedFileManager.getSelectedFileManager()
                                .insertToSelectedFilesList(fileList.get(position));
                        setFooterVisible(true);
                        ((FileManagerHolder)holder).fillBackground(true);
                    } else {
                        SelectedFileManager.getSelectedFileManager()
                                .removeAllSelectedFiles();
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void shortItemClick(int position) {
                    if (!SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                        SelectedFileManager.getSelectedFileManager().insertToSelectedFilesList(fileList.get(position));
                        if (SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                            setFooterVisible(false);
                            notifyDataSetChanged();
                        } else {
                            notifyItemChanged(position);
                        }
                    } else {
                        if (fileList.get(position).listFiles() == null){
                            //file
                            Log.d(LOG_TAG, fileList.get(position).getAbsolutePath() + " is file");
                            FileUtils.openFile(holder.itemView.getContext(), fileList.get(position));
                        } else {
                            Log.d(LOG_TAG, fileList.get(position).getAbsolutePath() + " is folder");
                            fileManagerAdapterInterface.moveNextDirectory(fileList.get(position).getAbsolutePath());
                        }
                    }
                }

                @Override
                public void moveNextDirectory(String directory) {

                }

                @Override
                public void callFileInfo(File file) {
                    fileManagerAdapterInterface.callFileInfo(file);
                }
            });
        }
        if (holder instanceof FooterHolder){
            setFooterVisibility(holder, SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty());
        }
    }


    public void setFavoritesDirectory(List<String> favoritesDirectories){
        for (int i = 0; i < favoritesDirectories.size(); i++){
            this.fileList.add(new File(favoritesDirectories.get(i)));
        }
        notifyDataSetChanged();
    }

    public void setNewDirectory(String newDirectory){
        this.directoryFile = new File(newDirectory);
        this.fileList = new ArrayList<File>(Arrays.asList(directoryFile.listFiles()));
        notifyDataSetChanged();
    }
    public void updateRecycler(){
        this.fileList = new ArrayList<File>(Arrays.asList(new File(this.directoryFile.getPath()).listFiles()));
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

    public File getItem(int position){
        return fileList.get(position);
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
    private void setFooterVisibility(RecyclerView.ViewHolder holder, boolean isFooterVisible){
        if (isFooterVisible){
            ((FooterHolder)holder).itemView.setVisibility(View.GONE);
        } else {
            ((FooterHolder)holder).itemView.setVisibility(View.VISIBLE);
        }
    }
}
