package taras.clientwebsocketapp.screens.dialogs.permission_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.PermissionManagerServer;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.utils.ConstatsLogTag;

public class CheckPermissionDialog extends DialogFragment {

    private static final int PERMISSION_TIME = 10000;

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvIp) TextView tvIp;
    @BindView(R.id.rvFilesToSend) RecyclerView rvFilesToSend;
    @BindView(R.id.tvAccept) TextView tvAccept;
    @BindView(R.id.pbTimeout) ProgressBar pbTimeout;
    @BindView(R.id.tvDeny) TextView tvDeny;
    //@BindView(R.id.tvTimer) TextView tvTimer;

    private CountDownTimer countDownTimer;
    private int progressCount = 0;

    private PermissionFilesRecyclerAdapter adapter;
    private PermissionPackage permissionPackage;

    private Unbinder unbinder;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
            lWindowParams.gravity = Gravity.CENTER;
            lWindowParams.copyFrom(getDialog().getWindow().getAttributes());
            lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT; // this is where the magic happens
            lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lWindowParams);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        fillData();
        countDownTimer = new CountDownTimer(PERMISSION_TIME, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                String seconds = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                //tvTimer.setText(seconds);
                progressCount++;
                pbTimeout.setProgress(progressCount);
            }
            public void onFinish() {
                pbTimeout.setProgress(progressCount);
                if (permissionPackage.getIsAllowed() == null){
                    PermissionManagerServer.getPermissionManager().setAcceptPermission(permissionPackage, false);
                }
                Log.d(ConstatsLogTag.CheckPermissionDialog, "countDownTimer, onFinish");
                dismiss();
                //todo end time to send permission
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_check_permission,container, false);
        unbinder = ButterKnife.bind(this, dialogView);
        return dialogView;
    }

    @Override
    public void onStop() {
        Log.d(ConstatsLogTag.CheckPermissionDialog, "onStop");
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(ConstatsLogTag.CheckPermissionDialog, "onDetach");
        unbinder.unbind();
    }

    private void initScanningRecycler(){
        rvFilesToSend.setHasFixedSize(true);
        rvFilesToSend.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvFilesToSend.setAdapter(adapter);
    }

    private void fillData(){
        tvName.setText(permissionPackage.getClientName());
        tvIp.setText(permissionPackage.getClientIp());
        adapter = new PermissionFilesRecyclerAdapter(permissionPackage.getFilesName());
        initScanningRecycler();

        pbTimeout.setMax(10);
        pbTimeout.setBackgroundColor(getResources().getColor(R.color.white));
        pbTimeout.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue_grey_400), PorterDuff.Mode.MULTIPLY);
    }

    public void setPermissionPackage(PermissionPackage permissionPackage) {
        this.permissionPackage = permissionPackage;
    }

    @OnClick({R.id.tvAccept, R.id.tvDeny})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tvAccept:
                Log.d(ConstatsLogTag.CheckPermissionDialog, "Accept");
                PermissionManagerServer.getPermissionManager().setAcceptPermission(permissionPackage, true);
                countDownTimer.cancel();
                dismiss();
                break;
            case R.id.tvDeny:
                Log.d(ConstatsLogTag.CheckPermissionDialog, "Deny");
                PermissionManagerServer.getPermissionManager().setAcceptPermission(permissionPackage, false);
                countDownTimer.cancel();
                dismiss();
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        PermissionManagerServer.getPermissionManager().setAcceptPermission(permissionPackage, false);
        super.onDismiss(dialog);
    }
}
