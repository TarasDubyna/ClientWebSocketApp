package taras.clientwebsocketapp.screens.dialogs.permission_dialog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.screens.view_holders.ScanningDeviceHolder;

public class PermissionFilesRecyclerAdapter extends RecyclerView.Adapter<PermissionFilesRecyclerAdapter.PermissionFileHolder> {

    private List<String> filesList;

    public PermissionFilesRecyclerAdapter(List<String> filesList) {
        this.filesList = filesList;
    }

    @Override
    public PermissionFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PermissionFileHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_file_to_send_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PermissionFileHolder holder, int position) {
        holder.bind(filesList.get(position));
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    class PermissionFileHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvFileName) TextView tvFileName;

        public PermissionFileHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String fileName){
            tvFileName.setText(fileName);
        }

    }

}
