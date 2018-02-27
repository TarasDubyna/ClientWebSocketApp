package taras.clientwebsocketapp.screens.dialogs;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;

/**
 * Created by Taras on 27.02.2018.
 */

public class FileInfoDialog extends android.support.v4.app.DialogFragment {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvDirectory)
    TextView tvDirectory;
    @BindView(R.id.tvDate)
    TextView tvDate;

    private Context mContext;
    private File file;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View dialogView = inflater.inflate(R.layout.dialog_info_fragment,container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(500, 1000);

        ButterKnife.bind(this, dialogView);
        setInfo();

        return dialogView;
    }

    public void setFileForInfo(File file){
        this.file = file;
    }

    private void setInfo(){
        tvName.setText(file.getName());
        tvDirectory.setText(file.getAbsolutePath());
    }

}
