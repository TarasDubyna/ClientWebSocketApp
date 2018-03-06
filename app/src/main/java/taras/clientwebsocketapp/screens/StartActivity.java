package taras.clientwebsocketapp.screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.utils.FileUtils;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 15.02.2018.
 */


public class StartActivity extends AppCompatActivity {
    private static final String LOG_TAG = "myLogs";

    @BindView(R.id.etName)
    EditText etName;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (selectStartFragment()){
            setContentView(R.layout.fragment_start);
            unbinder = ButterKnife.bind(this);
            etName.setOnEditorActionListener((etName, i, keyEvent) -> {
                if(i == EditorInfo.IME_ACTION_DONE){
                    String text = etName.getText().toString().trim();
                    if (text.length() > 0){
                        PreferenceUtils.saveDeviceName(etName.getText().toString());
                        allowPermissions();
                    } else {
                        Snackbar.make(etName, "Name field - empty!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }
                return false;
            });
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean selectStartFragment(){
        if (PreferenceUtils.getDeviceName().equals("")){
            return true;
        } else {
            return false;
        }
    }



    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        unbinder.unbind();
        createSavedFolder();
        startActivity(intent);
        this.finish();
    }

    private void createSavedFolder(){
        Log.d(LOG_TAG, "saving folder path: " + PreferenceUtils.getSavingFolderPath());
        if (!PreferenceUtils.isSavingFolderPathCreated()){
            FileUtils.createFolderForSaving(this);
        }
    }





    //work with permissions
    private List<String> notGrantedPermission = new ArrayList<>();
    private List<String> permissionsList = new ArrayList<String>(){{
        add(Manifest.permission.READ_EXTERNAL_STORAGE);
        add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }};
    private void allowPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String item: permissionsList){
                checkWriteExternalPermission(item);
            }
            if (notGrantedPermission.size()>0){
                Log.d(LOG_TAG, "notGrantedPermission size = " + notGrantedPermission.size());
                String[] stockArr = new String[notGrantedPermission.size()];
                stockArr = notGrantedPermission.toArray(stockArr);
                ActivityCompat.requestPermissions(StartActivity.this, stockArr, 1);
            } else {
                Log.d(LOG_TAG, "notGrantedPermission size = 0, start activity");
                startMainActivity();
            }
        } else {
            startMainActivity();
        }
    }
    private void checkWriteExternalPermission(String permission){
        int res = this.checkCallingOrSelfPermission(permission);
        if (res != PackageManager.PERMISSION_GRANTED){
            if (notGrantedPermission == null){
                notGrantedPermission = new ArrayList<>();
            }
            notGrantedPermission.add(permission);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                Log.d(LOG_TAG, "onRequestPermissionsResult");
                //permissionsNums++;
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "onRequestPermissionsResult, permission is granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Log.d(LOG_TAG, "onRequestPermissionsResult, permission is not granted");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(SplashActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }

                //Log.d(LOG_TAG, "permission num: " + permissionsNums + ", notGrantedPermission.size(): " + notGrantedPermission.size());
                startMainActivity();
                return;
            }
        }
    }


}
