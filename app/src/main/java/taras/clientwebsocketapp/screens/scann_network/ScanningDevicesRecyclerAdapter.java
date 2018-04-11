package taras.clientwebsocketapp.screens.scann_network;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.custom_views.SelectedFileView;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.file_manager.FileManagerAdapter;
import taras.clientwebsocketapp.screens.interfaces.RecyclerClickListener;
import taras.clientwebsocketapp.screens.view_holders.ScanningDeviceHolder;

/**
 * Created by Taras on 17.02.2018.
 */

public class ScanningDevicesRecyclerAdapter extends RecyclerView.Adapter<ScanningDeviceHolder> implements RecyclerClickListener {

    private Context mContext;
    private List<ScannerPackage> scannerPackagesList;


    public ScanningDevicesRecyclerAdapter(Context mContext, List<ScannerPackage> scannerPackagesList) {
        this.mContext = mContext;
        initDataList(scannerPackagesList);
    }

    @Override
    public ScanningDeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScanningDeviceHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_device_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ScanningDeviceHolder holder, int position) {
        holder.bind(scannerPackagesList.get(position));
        holder.bindRow(this, position);
    }

    @Override
    public int getItemCount() {
        return scannerPackagesList.size();
    }

    @Override
    public void onRowClicked(int position) {
        notifyItemChanged(position);
    }

    public void clear(){
        this.scannerPackagesList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addItem(ScannerPackage scannerPackage){
        this.scannerPackagesList.add(scannerPackage);
        notifyDataSetChanged();
    }

    private void initDataList(List<ScannerPackage> scannerPackagesList){
        if (scannerPackagesList == null){
            this.scannerPackagesList = new ArrayList<>();
        } else {
            this.scannerPackagesList = scannerPackagesList;
        }
    }


}
