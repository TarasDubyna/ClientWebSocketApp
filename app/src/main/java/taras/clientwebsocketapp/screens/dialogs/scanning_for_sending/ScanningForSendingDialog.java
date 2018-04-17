package taras.clientwebsocketapp.screens.dialogs.scanning_for_sending;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.screens.scann_network.ScanningDevicesRecyclerAdapter;
import taras.clientwebsocketapp.utils.EventBusMsg;

public class ScanningForSendingDialog extends DialogFragment {

    @BindView(R.id.rvDevices) RecyclerView rvDevices;

    private ScanningDevicesRecyclerAdapter adapter;

    private Unbinder unbinder;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(500, 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_info_fragment,container, false);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getScanningResult(EventBusMsg<Object> ebMessage) {
        if (ebMessage.getCodeDirection() == EventBusMsg.TO_APP){
            if (ebMessage.getCodeType() == EventBusMsg.PACKAGE_SCANNER){
                ScannerPackage scannerPackage = (ScannerPackage) ebMessage.getModel();
                adapter.addItem(scannerPackage);
            }
        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
    }


    private void initScanningRecycler(){
        adapter = new ScanningDevicesRecyclerAdapter(getContext(), null);
        rvDevices.setHasFixedSize(true);
        rvDevices.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvDevices.setAdapter(adapter);
    }
    private void scanningNetwork(){
        EventBusMsg<String> message =
                new EventBusMsg<String>(EventBusMsg.TO_SERVICE, EventBusMsg.PACKAGE_SCANNER, null);
        EventBus.getDefault().postSticky(message);
    }
}
