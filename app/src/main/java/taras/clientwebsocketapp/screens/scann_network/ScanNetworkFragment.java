package taras.clientwebsocketapp.screens.scann_network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.custom_views.selected_file_view.SelectedFileView;
import taras.clientwebsocketapp.model.ScannerPackage;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.interfaces.RecyclerClickListener;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.EventBusMsg;

/**
 * Created by Taras on 14.02.2018.
 */

public class ScanNetworkFragment extends Fragment implements RecyclerClickListener {
    private static final String LOG_TAG = "myLogs";

    private View rootView;

    @BindView(R.id.btnScannNetworkDevices)
    Button btnScannNetworkDevices;
    @BindView(R.id.rvNetworkDevices)
    RecyclerView rvNetworkDevices;
    @BindView(R.id.tvNoDevices)
    TextView tvNoDevices;

    private ScanningDevicesRecyclerAdapter devicesRecyclerAdapter;

    private static ScanNetworkFragment scanNetworkFragment;
    public static ScanNetworkFragment getFragment(){
        if (scanNetworkFragment == null){
            scanNetworkFragment = new ScanNetworkFragment();
        }
        return scanNetworkFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "ScanNetworkFragment, onStop");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "ScanNetworkFragment, onCreate");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "ScanNetworkFragment, onResume");
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getBoolean(Constants.START_SCANNING_FOR_FILE, false)){
            btnScannNetworkDevices.performClick();
            //((MainActivity) getActivity()).getSelectedFileView().initRemoveAllDeviceFromSelected(this);
        }
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.network));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "ScanNetworkFragment, onCreateView");
        rootView = inflater.inflate(R.layout.fragment_network, container, false);
        ButterKnife.bind(this, rootView);
        initScanningRecycler();
        return rootView;
    }

    @OnClick(R.id.btnScannNetworkDevices)
    void scanningNetwork(){
        tvNoDevices.setVisibility(View.VISIBLE);
        devicesRecyclerAdapter.clear();
        EventBusMsg<String> message =
                new EventBusMsg<String>(EventBusMsg.TO_SERVICE, EventBusMsg.PACKAGE_SCANNER, null);
        EventBus.getDefault().postSticky(message);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getScanningResult(EventBusMsg<Object> ebMessage) {
        if (ebMessage.getCodeDirection() == EventBusMsg.TO_APP){
            if (ebMessage.getCodeType() == EventBusMsg.PACKAGE_SCANNER){
                Log.d(LOG_TAG, "getScanningResult, message: " + ((ScannerPackage)ebMessage.getModel()).toJson());
                ScannerPackage scannerPackage = (ScannerPackage) ebMessage.getModel();
                devicesRecyclerAdapter.addItem(scannerPackage);
                tvNoDevices.setVisibility(View.GONE);
                rvNetworkDevices.setVisibility(View.VISIBLE);
            }
        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
    }

    private void initScanningRecycler(){
        devicesRecyclerAdapter = new ScanningDevicesRecyclerAdapter(getContext(), this, null);

        rvNetworkDevices.setHasFixedSize(true);
        rvNetworkDevices.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvNetworkDevices.setAdapter(devicesRecyclerAdapter);

        tvNoDevices.setVisibility(View.VISIBLE);
        rvNetworkDevices.setVisibility(View.GONE);
    }

    @Override
    public void onRowClicked(int position) {

    }
}
