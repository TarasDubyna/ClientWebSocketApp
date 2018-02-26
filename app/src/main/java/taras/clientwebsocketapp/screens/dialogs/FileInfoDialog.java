package taras.clientwebsocketapp.screens.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import taras.clientwebsocketapp.R;

/**
 * Created by Taras on 27.02.2018.
 */

public class FileInfoDialog extends android.support.v4.app.DialogFragment {

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View dialogView = inflater.inflate(R.layout.dialog_info_fragment,container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(200, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialogView;
    }
}
