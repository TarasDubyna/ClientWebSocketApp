package taras.clientwebsocketapp.custom_views.selected_file_view;

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
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.screens.MainActivity;
import taras.clientwebsocketapp.screens.dialogs.scanning_for_sending.ScanningForSendingDialog;
import taras.clientwebsocketapp.screens.scann_network.ScanNetworkFragment;
import taras.clientwebsocketapp.utils.Constants;

/**
 * Created by Taras on 03.03.2018.
 */

public class SelectedFileView extends LinearLayout {

    @BindView(R.id.tvSelectedNum)
    TextView tvSelectedNum;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivCancel)
    ImageView ivCancel;

    private View rootView;
    private SelectedFileViewCallback selectedFileViewCallback;
    private Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, rootView);
    }

    public void setCallback(SelectedFileViewCallback selectedFileViewCallback){
        this.selectedFileViewCallback = selectedFileViewCallback;
    }

    public void setSelectedNum(int count){
        tvSelectedNum.setText(getContext().getString(R.string.selected_files_num, count));
    }

    @OnClick({R.id.ivShare, R.id.ivCancel})
    void clickShare(View view){
        switch (view.getId()){
            case R.id.ivShare:
                selectedFileViewCallback.clickShare();
                break;
            case R.id.ivCancel:
                ivShare.setVisibility(View.VISIBLE);
                selectedFileViewCallback.clickCancel();
                break;
        }

    }

    /*
    @OnClick(R.id.ivCancel)
    void clickCancel(){
        SelectedFileManager.getSelectedFileManager().removeAllSelectedFiles();
        selectedFileViewCallback.removeAllSelectedFiles();
        ivShare.setVisibility(VISIBLE);
        if (!SelectedFileManager.getSelectedFileManager().isSelectedDevicesListEmpty()){
            SelectedFileManager.getSelectedFileManager().removeAllSelectedDevices();
            selectedFileViewCallback.removeAllSelectedDevices();
        }
    }
    */

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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unbinder.unbind();
    }
}
