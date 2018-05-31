package taras.clientwebsocketapp.screens.view_holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.file_manager.DirectoryInterface;

import static taras.clientwebsocketapp.screens.file_manager.FileManagerAdapter.CONTENT_FAVORITE;
import static taras.clientwebsocketapp.utils.Constants.CONTENT_USUAL;

public class DirectoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String LOG_TAG = DirectoryHolder.class.getSimpleName();

    @BindView(R.id.cvItem)
    CardView cvItem;
    @BindView(R.id.tvText)
    TextView tvText;

    //private int type;
    private String directory;

    public DirectoryHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String directory){
        this.directory = directory;
        tvText.setVisibility(View.VISIBLE);
        tvText.setText(cutDirectory(directory));
    }

    public void onRowClicked(DirectoryInterface listener){
        cvItem.setOnClickListener(v -> {
            Log.d(LOG_TAG, "itemView.setOnClickListener: " + directory);
            listener.moveToDirectory(directory);});
    }

    private static String cutDirectory(String path){
        String[] array = path.split("/+");
        return array[array.length - 1];
    }

    private void initOtherPositions(String directory){
        tvText.setVisibility(View.VISIBLE);
        tvText.setText(cutDirectory(directory));
    }

    @Override
    public void onClick(View v) {

    }
}
