package taras.clientwebsocketapp.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.screens.scann_network.ScanNetworkFragment;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 14.02.2018.
 */

public class StartFragment extends Fragment {

    @BindView(R.id.etName)
    EditText etName;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        etName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                PreferenceUtils.saveDeviceName(textView.toString());
                ((MainActivity)getActivity()).addFragmentToManager(new ScanNetworkFragment(), true);
                return true;
            }
            return false;
        });

        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
