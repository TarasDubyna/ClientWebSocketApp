package taras.clientwebsocketapp.screens.dialogs.scanning_for_sending;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.screens.scann_network.ScanningDevicesRecyclerAdapter;
import taras.clientwebsocketapp.utils.EventBusMsg;

public class ScanningForSendingDialog extends DialogFragment {

    @BindView(R.id.rvDevices) RecyclerView rvDevices;
    @BindView(R.id.ivRefresh) ImageView ivRefresh;
    @BindView(R.id.tvNoDevices) TextView tvNoDevices;
    @BindView(R.id.llSend) LinearLayout llSend;

    private ScanningDevicesRecyclerAdapter adapter;

    private Unbinder unbinder;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
            lWindowParams.gravity = Gravity.CENTER;
            lWindowParams.copyFrom(getDialog().getWindow().getAttributes());
            lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT; // this is where the magic happens
            lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lWindowParams);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_scanning_for_sending,container, false);
        unbinder = ButterKnife.bind(this, dialogView);
        initScanningRecycler();
        scanningNetwork();
        return dialogView;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    private void initScanningRecycler(){
        adapter = new ScanningDevicesRecyclerAdapter(getContext(), null);
        rvDevices.setHasFixedSize(true);
        rvDevices.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvDevices.setAdapter(adapter);
    }

    @OnClick({R.id.ivRefresh, R.id.llSend})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ivRefresh:
                rvDevices.setVisibility(View.GONE);
                tvNoDevices.setVisibility(View.VISIBLE);
                adapter.clear();
                scanningNetwork();
                break;
            case R.id.llSend:
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getScanningResult(EventBusMsg<Object> ebMessage) {
        Log.d("myLogs", "getScanningResult in dialog");
        if (ebMessage.getCodeDirection() == EventBusMsg.TO_APP){
            if (ebMessage.getCodeType() == EventBusMsg.PACKAGE_SCANNER){
                ScannerPackage scannerPackage = (ScannerPackage) ebMessage.getModel();
                adapter.addItem(scannerPackage);
                tvNoDevices.setVisibility(View.GONE);
                rvDevices.setVisibility(View.VISIBLE);
            }
        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
    }

    private void scanningNetwork(){
        EventBusMsg<String> message =
                new EventBusMsg<String>(EventBusMsg.TO_SERVICE, EventBusMsg.PACKAGE_SCANNER, null);
        EventBus.getDefault().postSticky(message);
    }
}
