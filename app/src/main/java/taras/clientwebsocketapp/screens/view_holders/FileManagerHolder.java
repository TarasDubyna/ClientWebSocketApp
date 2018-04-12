package taras.clientwebsocketapp.screens.view_holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;

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
}