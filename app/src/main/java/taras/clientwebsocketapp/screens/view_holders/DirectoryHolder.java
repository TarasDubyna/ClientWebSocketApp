package taras.clientwebsocketapp.screens.view_holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.file_manager.DirectoryAdapter;

public class DirectoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.cvItem)
    CardView cvItem;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    private int type;

    public DirectoryHolder(View itemView, int type) {
        super(itemView);
        this.type = type;
        ButterKnife.bind(this, itemView);
    }

    public void bind(int position, String directory){
        if (position == 0){
            initZeroPosition();
        } else {
            initOtherPositions(directory);
        }
    }

    public void listener(int size, DirectoryHolderInterface listener){
        if (listener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != size - 1){
                        listener.moveOnDirectory();
                    }
                }
            });
        }
    }

    private static String cutDirectory(String path){
        String[] array = path.split("/+");
        return array[array.length - 1];
    }

    private void initZeroPosition(){
        switch (type){
            case DirectoryAdapter.FILE_MANAGER:
                tvText.setVisibility(View.GONE);
                ivImage.setVisibility(View.VISIBLE);
                ivImage.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.ic_mobile_phone));
                break;
            case DirectoryAdapter.FAVORITE:
                tvText.setVisibility(View.GONE);
                ivImage.setVisibility(View.VISIBLE);
                ivImage.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.ic_star));
                break;
        }
    }
    private void initOtherPositions(String directory){
        ivImage.setVisibility(View.GONE);
        tvText.setVisibility(View.VISIBLE);
        tvText.setText(cutDirectory(directory));
    }

    @Override
    public void onClick(View v) {

    }


    public interface DirectoryHolderInterface{
        void moveOnDirectory();
    }
}
