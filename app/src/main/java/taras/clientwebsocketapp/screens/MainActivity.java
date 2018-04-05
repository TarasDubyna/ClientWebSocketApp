package taras.clientwebsocketapp.screens;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import taras.clientwebsocketapp.BackgroundService;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.custom_views.SelectedFileView;
import taras.clientwebsocketapp.managers.FavoriteFilesManager;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.screens.favorite.FavoriteFragment;
import taras.clientwebsocketapp.screens.file_manager.FileManagerFragment;
import taras.clientwebsocketapp.screens.scann_network.ScanNetworkFragment;
import taras.clientwebsocketapp.utils.EventBusMsg;
import taras.clientwebsocketapp.utils.PreferenceUtils;
import taras.clientwebsocketapp.utils.StorageOptions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String LOG_TAG = "myLogs";
    private static final String BACK_STACK_TAG = "BACK_STACK_TAG";


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.selectedFileView)
    SelectedFileView selectedFileView;
    ActionBarDrawerToggle toggle;


    private Drawable navigationIcon;

    private NavigationView navigationView;
    private Switch serverSwitch;
    private TextView toolbarTitle;

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
    private FavoriteFragment favoriteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService();

        StorageOptions.determineStorageOptions();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        navigationIcon = toolbar.getNavigationIcon();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setSubtitle(R.string.network_manager);

        initServiceReceiver();
        addFragmentToManager(ScanNetworkFragment.getFragment());


        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.menu_network_manager);
        navigationView.setNavigationItemSelectedListener(this);


        FavoriteFilesManager.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void addFragmentToManager(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, fragment);
        fragmentTransaction.commit();
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
            System.out.println();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem switchItem = menu.findItem(R.id.toolbar_switcher);
        switchItem.setActionView(R.layout.toolbar_layout);
        serverSwitch = menu.findItem(R.id.toolbar_switcher).getActionView().findViewById(R.id.switchServerToolbar);
        if (PreferenceUtils.isServerRunning()){
            serverSwitch.setChecked(true);
        }
        serverSwitch.setOnClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        if (serverSwitch.isChecked()){
            serverSwitch.setChecked(true);
            Snackbar.make(view, getString(R.string.server_on_visible), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            EventBusMsg<String> message = new EventBusMsg<String>(EventBusMsg.TO_SERVICE, EventBusMsg.SERVER_START, null);
        } else {
            serverSwitch.setChecked(false);
            Snackbar.make(view, getString(R.string.server_off_invisible), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            EventBusMsg<String> message = new EventBusMsg<String>(EventBusMsg.TO_SERVICE, EventBusMsg.SERVER_STOP, null);
            EventBus.getDefault().post(message);
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
            addFragmentToManager(scanNetworkFragment);
            // Handle the camera action
        } else if (id == R.id.menu_file_manager) {
            if (fileManagerFragment == null){
                fileManagerFragment = new FileManagerFragment();
            }
            addFragmentToManager(fileManagerFragment);
        } else if (id == R.id.menu_favorite) {
            if (favoriteFragment == null){
                favoriteFragment = new FavoriteFragment();
            }
            addFragmentToManager(favoriteFragment);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setToolbarTitle(String text){
        getSupportActionBar().setTitle(text);
    }


    public SelectedFileView getSelectedFileView(){
        return selectedFileView;
    }

    public void setDrawerLayoutLocked(){

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        toggle.syncState();

    }
    public void setDrawerLayoutOpened(){
        toolbar.setNavigationIcon(navigationIcon);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);

        toggle.syncState();
    }


    public NavigationView getNavigationView() {
        return navigationView;
    }

    //work with service
    private void initServiceReceiver(){
        if (myReceiver == null){
            myReceiver = new MainActivity.MyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BackgroundService.MY_ACTION);
            registerReceiver(myReceiver, intentFilter);
        }
    }
    private void startService(){
        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        startService(intent);
    }




    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onNewPostCreated(EventBusMsg<Object> ebMessage) {
        if (ebMessage.getCodeDirection() == EventBusMsg.TO_APP){
            switch (ebMessage.getCodeType()){
                case EventBusMsg.PACKAGE_PERMISSION:
                    break;
                case EventBusMsg.PACKAGE_SCANNER:
                    break;
                case EventBusMsg.PACKAGE_SERVER_STATE:
                    break;
            }
        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
    }


    public static Context getContext(){
        return getContext();
    }

}
