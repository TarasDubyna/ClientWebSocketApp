package taras.clientwebsocketapp.screens;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.NotificationService;
import taras.clientwebsocketapp.screens.file_manager.FileManagerFragment;
import taras.clientwebsocketapp.screens.scann_network.ScanNetworkFragment;
import taras.clientwebsocketapp.utils.Constants;
import taras.clientwebsocketapp.utils.GlobalBus;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String LOG_TAG = "myLogs";
    private static final String BACK_STACK_TAG = "BACK_STACK_TAG";



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Switch serverSwitch;


    boolean bound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(LOG_TAG, "MainActivity onServiceConnected");
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
            bound = false;
        }
    };
    MyReceiver myReceiver;
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            String responseData = arg1.getStringExtra("string");
            Toast.makeText(MainActivity.this,"Message: " + responseData, Toast.LENGTH_SHORT).show();
        }
    }

    private ScanNetworkFragment scanNetworkFragment;
    private FileManagerFragment fileManagerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initServiceReceiver();
        ScanNetworkFragment scanNetworkFragment = new ScanNetworkFragment();
        addFragmentToManager(ScanNetworkFragment.getFragment(),true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    public void addFragmentToManager(Fragment fragment, Boolean toBackstack){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, fragment, BACK_STACK_TAG);
        if (toBackstack){
            fragmentTransaction.addToBackStack(BACK_STACK_TAG).commit();
        } else {
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem switchItem = menu.findItem(R.id.toolbar_switcher);
        switchItem.setActionView(R.layout.toolbar_layout);
        serverSwitch = menu.findItem(R.id.toolbar_switcher).getActionView().findViewById(R.id.switchServerToolbar);
        serverSwitch.setOnClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        if (serverSwitch.isChecked()){
            serverSwitch.setChecked(true);
            Snackbar.make(view, getString(R.string.server_on_visible), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            GlobalBus.getBus().post(Constants.SERVER_START);
        } else {
            serverSwitch.setChecked(false);
            Snackbar.make(view, getString(R.string.server_off_invisible), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            GlobalBus.getBus().post(Constants.SERVER_STOP);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_network_manager) {
            if (scanNetworkFragment == null){
                scanNetworkFragment = new ScanNetworkFragment();
            }
            addFragmentToManager(scanNetworkFragment, false);
            // Handle the camera action
        } else if (id == R.id.menu_file_manager) {
            if (fileManagerFragment == null){
                fileManagerFragment = new FileManagerFragment();
            }
            addFragmentToManager(fileManagerFragment, false);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setToolbarTitle(String text){
        getSupportActionBar().setTitle(text);
    }

    private void initServiceReceiver(){
        if (myReceiver == null){
            myReceiver = new MainActivity.MyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(NotificationService.MY_ACTION);
            registerReceiver(myReceiver, intentFilter);
        }
    }

    private void startService(){
        Intent intent = new Intent(MainActivity.this, NotificationService.class);
        //intent.putExtra(NotificationService.TYPE, NotificationService.SCAN_NETWORK);
        //intent.putExtra("ip", etAddress.getText().toString());
        startService(intent);
    }

}
