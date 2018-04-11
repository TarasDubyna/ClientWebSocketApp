package taras.clientwebsocketapp.screens.view_holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.screens.interfaces.RecyclerClickListener;

public class ScanningDeviceHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cvItem)
    CardView cvItem;
    @BindView(R.id.tvDeviceIp)
    TextView tvDeviceIp;
    @BindView(R.id.tvDeviceName)
    TextView tvDeviceName;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    private ScannerPackage scannerPackage;

    public ScanningDeviceHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ScannerPackage scannPackage) {
        this.scannerPackage = scannPackage;
        tvDeviceIp.setText(scannerPackage.getServerIp());
        tvDeviceName.setText(scannerPackage.getServerName());

        if (SelectedFileManager.getSelectedFileManager() != null){
            if (SelectedFileManager.getSelectedFileManager().isDeviceSelected(scannerPackage.getServerIp())){
                cvItem.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.blue_grey_500));
            } else {
                cvItem.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.blue_grey_300));
            }
        }

        tvDeviceName.setText(scannerPackage.getServerName());
        tvDeviceIp.setText(scannerPackage.getServerIp());
    }

    public void bindRow(final RecyclerClickListener listener, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    if (!SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
                        SelectedFileManager.getSelectedFileManager().insertToSelectedDevicesList(scannerPackage.getServerIp());
                        if (SelectedFileManager.getSelectedFileManager().isDeviceSelected(scannerPackage.getServerIp())){
                            cvItem.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.blue_grey_500));
                        }
                        listener.onRowClicked(position);
                    }
                }


            }
        });
    }
}
