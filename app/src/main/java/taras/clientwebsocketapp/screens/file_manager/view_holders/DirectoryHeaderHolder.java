package taras.clientwebsocketapp.screens.file_manager.view_holders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.file_manager.DirectoryAdapter;
import taras.clientwebsocketapp.screens.file_manager.DirectoryInterface;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.ConverterUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

public class DirectoryHeaderHolder extends RecyclerView.ViewHolder {

    private static final String LOG_TAG = DirectoryHeaderHolder.class.getSimpleName();

    @BindView(R.id.cvItem) CardView cvItem;
    @BindView(R.id.ivMainIcon) ImageView ivMainIcon;
    @BindView(R.id.ivSubIcon) ImageView ivSubIcon;

    @BindDrawable(R.drawable.ic_mobile_phone) Drawable phone;
    @BindDrawable(R.drawable.ic_card_sd) Drawable cardSD;
    @BindDrawable(R.drawable.ic_star) Drawable favorite;


    public static final int TYPE_PHONE = 0;
    public static final int TYPE_SD = 1;

    private int type;
    private int memoryType = TYPE_PHONE;


    public DirectoryHeaderHolder(View itemView, int type, int memoryType) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.type = type;
        this.memoryType = memoryType;
        initImagesLayoutParams();
    }

    public void onRowClicked(int directoriesSize, DirectoryInterface listener){
        cvItem.setOnClickListener(v -> {
            if (type == Constants.CONTENT_FAVORITE){
                listener.goToZeroPosition("favorite");
            }
            if (type == Constants.CONTENT_USUAL){
                if (PreferenceUtils.getSDStorageDirection() != null){
                    if (directoriesSize == 1){
                        System.out.println("directoriesSize == 1, change memory type");
                        String zeroNewDirectory = changeMemoryType();
                        listener.changeTypeMemory(zeroNewDirectory, memoryType);
                    } else {
                        System.out.println("directoriesSize > " + directoriesSize + " , goToZeroPosition");
                        listener.goToZeroPosition(getDirectory());
                    }
                } else {
                    listener.goToZeroPosition(getDirectory());
                }
            }
        });
    }

    private void initImagesLayoutParams(){
        if (type == Constants.CONTENT_USUAL){
            if (PreferenceUtils.getSDStorageDirection() != null){
                ivMainIcon.setVisibility(View.VISIBLE);
                ivSubIcon.setVisibility(View.VISIBLE);
                if (memoryType == TYPE_PHONE){
                    ivMainIcon.setImageDrawable(phone);
                    ivSubIcon.setImageDrawable(cardSD);
                }
                if (memoryType == TYPE_SD){
                    ivMainIcon.setImageDrawable(cardSD);
                    ivSubIcon.setImageDrawable(phone);
                }
            } else {
                ivMainIcon.setVisibility(View.VISIBLE);
                ivSubIcon.setVisibility(View.GONE);
                ivMainIcon.setImageDrawable(phone);
            }
        }

        if (type == Constants.CONTENT_FAVORITE){
            ivMainIcon.setImageDrawable(favorite);
            ivMainIcon.setVisibility(View.VISIBLE);
            ivSubIcon.setVisibility(View.GONE);
        }
    }

    private String changeMemoryType(){
        if(PreferenceUtils.getSDStorageDirection() != null){
            if (memoryType == TYPE_PHONE){
                memoryType = TYPE_SD;
                setSelectedMainType(memoryType);
                return PreferenceUtils.getSDStorageDirection();
            } else {
                memoryType = TYPE_PHONE;
                setSelectedMainType(memoryType);
                return PreferenceUtils.getLocalStorageDirection();
            }
        } else {
            return null;
        }
    }

    private String getDirectory(){
        if (memoryType == TYPE_PHONE){
            return PreferenceUtils.getSDStorageDirection();
        } else {
            return PreferenceUtils.getLocalStorageDirection();
        }
    }


    private void setSelectedMainType(int type){
        switch (type){
            case TYPE_PHONE:
                ivMainIcon.setImageDrawable(phone);
                ivSubIcon.setImageDrawable(cardSD);
                break;
            case TYPE_SD:
                ivSubIcon.setImageDrawable(phone);
                ivMainIcon.setImageDrawable(cardSD);
                break;
        }
    }

}
