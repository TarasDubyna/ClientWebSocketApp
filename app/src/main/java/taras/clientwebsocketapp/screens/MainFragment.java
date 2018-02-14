package taras.clientwebsocketapp.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taras.clientwebsocketapp.R;

/**
 * Created by Taras on 14.02.2018.
 */

public class MainFragment extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_client, container, false);
        return rootView;
    }
}
