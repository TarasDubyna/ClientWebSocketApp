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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.model.PermissionPackageFirst;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.screens.interfaces.RecyclerClickListener;
import taras.clientwebsocketapp.screens.scann_network.ScanningDevicesRecyclerAdapter;
import taras.clientwebsocketapp.utils.AnimationUtils;
import taras.clientwebsocketapp.utils.EventBusMsg;

public class ScanningForSendingDialog extends DialogFragment implements RecyclerClickListener {

    private static final String LOG_TAG = ScanningForSendingDialog.class.getSimpleName();

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
        initScanningRecycler();
        //testData();
        scanningNetwork();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_scanning_for_sending,container, false);
        unbinder = ButterKnife.bind(this, dialogView);
        return dialogView;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        ivRefresh.clearAnimation();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    private void initScanningRecycler(){
        adapter = new ScanningDevicesRecyclerAdapter(getContext(), this,null);
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
            if (ebMessage.getCodeType() == EventBusMsg.SCANNING_NETWORK_END){
                ivRefresh.clearAnimation();
                ivRefresh.setClickable(true);
                Log.d(LOG_TAG, "End scanning network");
            }
        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
    }

    private void scanningNetwork(){
        Log.d(LOG_TAG, "Start scanning network");
        ivRefresh.setClickable(false);
        ivRefresh.post(new Runnable() {
            @Override
            public void run() {
                AnimationUtils.rotation(ivRefresh);
            }
        });
        EventBusMsg<String> message =
                new EventBusMsg<String>(EventBusMsg.TO_SERVICE, EventBusMsg.PACKAGE_SCANNER, null);
        EventBus.getDefault().postSticky(message);
    }

    private void testData(){
        tvNoDevices.setVisibility(View.GONE);
        rvDevices.setVisibility(View.VISIBLE);
        for (int i = 0; i < 6; i ++){
            ScannerPackage scannerPackage = new ScannerPackage();
            scannerPackage.setServerIp("192.168.1." + i);
            scannerPackage.setServerName("server name");
            adapter.addItem(scannerPackage);
        }
    }

    @Override
    public void onRowClicked(int position) {
        if (!adapter.isEmpty()){
            llSend.setVisibility(View.GONE);
        } else {
            llSend.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.llSend)
    void sendPermission(View view){
        PermissionPackageFirst permissionPackageFirst = new PermissionPackageFirst();
        permissionPackageFirst.setServerIp(adapter.getSelectedDevices().get(0).getServerIp());
        permissionPackageFirst.setFilesName(SelectedFileManager.getSelectedFileManager().getAllSelectedFilesNames());

        EventBusMsg<PermissionPackageFirst> message =
                new EventBusMsg<PermissionPackageFirst>(EventBusMsg.TO_SERVICE, EventBusMsg.PACKAGE_PERMISSION_FIRST, permissionPackageFirst);
        EventBus.getDefault().postSticky(message);
    }


}
