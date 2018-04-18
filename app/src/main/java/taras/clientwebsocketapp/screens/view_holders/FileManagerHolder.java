package taras.clientwebsocketapp.screens.view_holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.FavoriteFilesManager;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.model.FileManagerHolderClickCallback;
import taras.clientwebsocketapp.screens.manager.FileManager;

public class FileManagerHolder extends RecyclerView.ViewHolder{

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

    public FileManagerHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    //TODO
    public void bind(File file){
        checkIsFavorite(file);
        setItemType(file);
        fillBackgroundColor(file);

        tvName.setText(file.getName());
    }

    public void onRowClicked(File file, FileManagerHolderClickCallback listener){
        ivMore.setOnClickListener(v -> { listener.moreInfoClick(file);});
        itemView.setOnClickListener(v -> { listener.shortClick(getAdapterPosition());});
        itemView.setOnLongClickListener(v -> {
            listener.longClick(getAdapterPosition());
            return false;
        });
    }

    private void checkIsFavorite(File file){
        if (FavoriteFilesManager.getInstance().isFavorite(file)){
            ivFavorite.setVisibility(View.VISIBLE);
        } else {
            ivFavorite.setVisibility(View.INVISIBLE);
        }
    }
    private void setItemType(File file){
        String type = FileManager.getTypeFileFolder(file);// is file or folder
        switch (type){
            case FileManager.TYPE_FILE:
                ivImage.setVisibility(View.GONE);
                break;
            case FileManager.TYPE_FOLDER:
                ivImage.setVisibility(View.VISIBLE);
                ivImage.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.ic_folder));
                break;
        }
    }


    private void fillBackgroundColor(File file){
        if (SelectedFileManager.getSelectedFileManager().isFileSelected(file)){
            cvItem.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.blue_grey_500));
        } else {
            cvItem.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.blue_grey_300));
        }
    }

}