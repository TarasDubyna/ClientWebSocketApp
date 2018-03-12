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

    @BindView(R.id.tvSelectedNum)
    TextView tvSelectedNum;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivCancel)
    ImageView ivCancel;

    private View rootView;
    private SelectedFileViewInterface selectedFileViewInterface;

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

    public void setSelectedNum(int count){
        tvSelectedNum.setText(getContext().getString(R.string.selected_files_num, count));
    }

    @OnClick(R.id.ivShare)
    void clickShare(){
        //GlobalBus.getBus().post(SelectedFileManager.getSelectedFileManager().getAllSelectedDirectoriesFilesList());
        //SelectedFileManager.getSelectedFileManager().removeAllSelectedFiles();
        //selectedFileViewInterface.removeAllFromSelectedFiles();
        tvSelectedNum.setText(R.string.select_device);
        ivShare.setVisibility(INVISIBLE);
        ScanNetworkFragment scanNetworkFragment = new ScanNetworkFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.START_SCANNING_FOR_FILE, true);
        scanNetworkFragment.setArguments(bundle);
        ((MainActivity) getContext()).addFragmentToManager(scanNetworkFragment);
    }
    @OnClick(R.id.ivCancel)
    void clickCancel(){
        SelectedFileManager.getSelectedFileManager().removeAllSelectedFiles();
        selectedFileViewInterface.removeAllFromSelectedFiles();
    }
}
