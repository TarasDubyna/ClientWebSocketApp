package taras.clientwebsocketapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.utils.PreferenceUtils;

/**
 * Created by Taras on 15.02.2018.
 */


public class StartActivity extends AppCompatActivity {

    @BindView(R.id.etName)
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (selectStartFragment()){
            setContentView(R.layout.fragment_start);
            Unbinder unbinder = ButterKnife.bind(this);
            etName.setOnEditorActionListener((etName, i, keyEvent) -> {
                if(i == EditorInfo.IME_ACTION_DONE){
                    PreferenceUtils.saveDeviceName(etName.getText().toString());
                    Intent intent = new Intent(this, MainActivity.class);
                    unbinder.unbind();
                    startActivity(intent);
                    finish();
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
}
