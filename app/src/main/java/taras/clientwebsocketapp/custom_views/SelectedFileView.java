package taras.clientwebsocketapp.custom_views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.scann_network.ScanNetworkFragment;
import taras.clientwebsocketapp.utils.Constants;

/**
 * Created by Taras on 03.03.2018.
 */

public class SelectedFileView extends LinearLayout {

    public interface SelectedFileViewInterface{
        void removeAllFromSelectedFiles();
    }
    public interface SelectedDeviceViewInterface{
        void removeAllFromSelectedDevices();
    }

    @BindView(R.id.tvSelectedNum)
    TextView tvSelectedNum;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivCancel)
    ImageView ivCancel;

    private View rootView;
    private SelectedFileViewInterface selectedFileViewInterface;
    private SelectedDeviceViewInterface selectedDeviceViewInterface;

    public SelectedFileView(Context context) {
        super(context);
        init(context);
    }

    public SelectedFileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectedFileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SelectedFileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        rootView = inflate(context, R.layout.selected_file_view, this);
        rootView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this, rootView);
    }

    public void initRemoveAllFilesFromSelected(SelectedFileViewInterface selectedFileViewInterface){
        this.selectedFileViewInterface = selectedFileViewInterface;
    }
    public void initRemoveAllDeviceFromSelected(SelectedDeviceViewInterface selectedDeviceViewInterface){
        this.selectedDeviceViewInterface = selectedDeviceViewInterface;
    }

    public void setSelectedNum(int count){
        tvSelectedNum.setText(getContext().getString(R.string.selected_files_num, count));
    }

    @OnClick(R.id.ivShare)
    void clickShare(){
        if (SelectedFileManager.getSelectedFileManager().isSelectedDevicesListEmpty()){
            tvSelectedNum.setText(R.string.select_device);
            ivShare.setVisibility(INVISIBLE);
            ScanNetworkFragment scanNetworkFragment = new ScanNetworkFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.START_SCANNING_FOR_FILE, true);
            scanNetworkFragment.setArguments(bundle);
            ((MainActivity) getContext()).addFragmentToManager(scanNetworkFragment);
        } else {
            SelectedFileManager.getSelectedFileManager().sendDataToService();
            selectedFileViewInterface.removeAllFromSelectedFiles();
        }
    }
    @OnClick(R.id.ivCancel)
    void clickCancel(){
        SelectedFileManager.getSelectedFileManager().removeAllSelectedFiles();
        selectedFileViewInterface.removeAllFromSelectedFiles();
        ivShare.setVisibility(VISIBLE);
        if (!SelectedFileManager.getSelectedFileManager().isSelectedDevicesListEmpty()){
            SelectedFileManager.getSelectedFileManager().removeAllSelectedDevices();
            selectedDeviceViewInterface.removeAllFromSelectedDevices();
        }
    }

    public TextView getTvSelectedNum() {
        return tvSelectedNum;
    }
    public void setTvSelectedNum(TextView tvSelectedNum) {
        this.tvSelectedNum = tvSelectedNum;
    }

    public ImageView getIvShare() {
        return ivShare;
    }
    public void setIvShare(ImageView ivShare) {
        this.ivShare = ivShare;
    }

    public ImageView getIvCancel() {
        return ivCancel;
    }
    public void setIvCancel(ImageView ivCancel) {
        this.ivCancel = ivCancel;
    }
}
