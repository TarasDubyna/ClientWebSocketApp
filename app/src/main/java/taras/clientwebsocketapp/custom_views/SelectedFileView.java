package taras.clientwebsocketapp.custom_views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import taras.clientwebsocketapp.R;

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

    public void setSelectedNum(int count){
        tvSelectedNum.setText(getContext().getString(R.string.selected_files_num, count));
    }

    @OnClick(R.id.ivShare)
    void clickShare(){
        Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.ivCancel)
    void clickCancel(){
        Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
    }
}
