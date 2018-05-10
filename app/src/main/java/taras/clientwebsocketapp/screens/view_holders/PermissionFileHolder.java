package taras.clientwebsocketapp.screens.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.utils.ConverterUtils;

public class PermissionFileHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tvFileName)
    TextView tvFileName;

    public PermissionFileHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String fileName){
        tvFileName.setText(ConverterUtils.getFileNameFromDirectory(fileName));
    }

}
