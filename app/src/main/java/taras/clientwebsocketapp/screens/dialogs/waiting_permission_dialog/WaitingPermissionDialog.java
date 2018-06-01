package taras.clientwebsocketapp.screens.dialogs.waiting_permission_dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.model.PermissionPackage;

public class WaitingPermissionDialog extends DialogFragment {

    @BindView(R.id.pbProgress) ProgressBar progressBar;
    @BindView(R.id.ivWaitingResult) ImageView ivWaitingResult;
    @BindView(R.id.tvAccept) TextView tvAccept;

    private Unbinder unbinder;

    private PermissionPackage permissionPackage;

    public void setPermissionPackage(PermissionPackage pack){
        this.permissionPackage = pack;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_waiting_permission, container, false);
        unbinder = ButterKnife.bind(this, view);
        render();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void render(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void stop(PermissionPackage pack){
        progressBar.setVisibility(View.GONE);
        if (pack.getIsAllowed().equals("false")){
            ivWaitingResult.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel));
            ivWaitingResult.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue_grey_500), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        ivWaitingResult.setVisibility(View.VISIBLE);
        tvAccept.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tvAccept)
    void clickAccept(View view){
        dismiss();
    }
}
