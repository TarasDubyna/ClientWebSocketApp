package taras.clientwebsocketapp.screens.dialogs.permission_dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.FavoriteFilesManager;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.screens.dialogs.FileInfoDialogInterface;
import taras.clientwebsocketapp.utils.FileUtils;

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

    private PermissionPackage permissionPackage;
    private CheckPermissionCallback checkPermissionCallback;

    public CheckPermissionDialogActivity(PermissionPackage permissionPackage, CheckPermissionCallback checkPermissionCallback) {
        this.permissionPackage = permissionPackage;
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
