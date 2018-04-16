package taras.clientwebsocketapp.screens.view_holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.file_manager.DirectoryAdapter;
import taras.clientwebsocketapp.screens.file_manager.DirectoryInterface;

import static taras.clientwebsocketapp.screens.file_manager.FileManagerAdapter.CONTENT_FAVORITE;
import static taras.clientwebsocketapp.utils.Constants.CONTENT_USUAL;

public class DirectoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String LOG_TAG = "myLogs";

    @BindView(R.id.cvItem)
    CardView cvItem;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    private int type;
    private String directory;

    public DirectoryHolder(View itemView, int type) {
        super(itemView);
        this.type = type;
        ButterKnife.bind(this, itemView);
    }

    public void bind(int position, String directory){
        this.directory = directory;
        if (position == 0){
            initZeroPosition();
        } else {
            initOtherPositions(directory);
        }
    }

    public void listener(int size, DirectoryInterface listener){
        if (listener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != size - 1){
                        Log.d(LOG_TAG, "itemView.setOnClickListener: " + directory);
                        listener.moveToDirectory(directory);
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
            case CONTENT_USUAL:
                tvText.setVisibility(View.GONE);
                ivImage.setVisibility(View.VISIBLE);
                ivImage.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.ic_mobile_phone));
                break;
            case CONTENT_FAVORITE:
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
}
