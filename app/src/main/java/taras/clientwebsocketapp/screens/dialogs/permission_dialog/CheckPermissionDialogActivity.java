package taras.clientwebsocketapp.screens.dialogs.permission_dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.model.PermissionPackageFirst;

/**
 * Created by Taras on 26.03.2018.
 */

public class CheckPermissionDialogActivity extends AppCompatActivity {
    private static final String LOG_TAG = "myLogs";



    /*
    @BindView(R.id.accept)
    Button accept;
    @BindView(R.id.deny)
    Button deny;
    */

    private PermissionPackageFirst permissionPackageFirst;
    private CheckPermissionCallback checkPermissionCallback;

    public CheckPermissionDialogActivity(PermissionPackageFirst permissionPackageFirst, CheckPermissionCallback checkPermissionCallback) {
        this.permissionPackageFirst = permissionPackageFirst;
        this.checkPermissionCallback = checkPermissionCallback;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setFinishOnTouchOutside(false);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }
}
