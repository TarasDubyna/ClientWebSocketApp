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
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.file_manager.FileManagerAdapter;

/**
 * Created by Taras on 17.02.2018.
 */

public class DevicesRecyclerAdapter extends RecyclerView.Adapter<DevicesRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ScannerPackage> scannerPackagesList;

    private String selectedIp;

    private boolean toSend = false;

    public DevicesRecyclerAdapter(Context mContext, ArrayList<ScannerPackage> scannerPackagesList) {
        this.mContext = mContext;
        this.scannerPackagesList = scannerPackagesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_device_item, parent, false);
        return new ViewHolder(rootView);
    }

    public void isToSend(boolean toSend){
        this.toSend = toSend;
        if (!toSend){
            selectedIp = null;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScannerPackage.ServerData device = scannerPackagesList.get(position).getServerData();

        if (selectedIp != null && selectedIp.equals(device.getServerIp())){
            holder.cvItem.setCardBackgroundColor(mContext.getResources().getColor(R.color.blue_grey_500));
        } else {
            holder.cvItem.setCardBackgroundColor(mContext.getResources().getColor(R.color.blue_grey_300));
        }

        holder.tvDeviceName.setText(device.getServerName());
        holder.tvDeviceIp.setText(device.getServerIp());
        holder.cvItem.setOnClickListener(view -> {
            if (toSend){
                holder.cvItem.setCardBackgroundColor(mContext.getResources().getColor(R.color.blue_grey_500));
                selectedIp = device.getServerIp();
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return scannerPackagesList.size();
    }

    public void clear(){
        this.scannerPackagesList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addDevicesList(ArrayList<ScannerPackage> scannerPackagesList){
        this.scannerPackagesList = scannerPackagesList;
        notifyDataSetChanged();
    }

    public void addDevice(ScannerPackage scannerPackage){
        this.scannerPackagesList.add(scannerPackage);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvItem)
        CardView cvItem;
        @BindView(R.id.tvDeviceIp)
        TextView tvDeviceIp;
        @BindView(R.id.tvDeviceName)
        TextView tvDeviceName;
        @BindView(R.id.ivImage)
        ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
