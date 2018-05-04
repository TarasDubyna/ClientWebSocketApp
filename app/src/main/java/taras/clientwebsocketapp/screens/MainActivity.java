package taras.clientwebsocketapp.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import taras.clientwebsocketapp.managers.PermissionManagerClient;
import taras.clientwebsocketapp.managers.PermissionManager;
import taras.clientwebsocketapp.managers.SelectedFileManager;
import taras.clientwebsocketapp.model.PermissionPackage;
import taras.clientwebsocketapp.screens.dialogs.permission_dialog.CheckPermissionDialog;
import taras.clientwebsocketapp.service.BackgroundService;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.FavoriteFilesManager;
import taras.clientwebsocketapp.screens.file_manager.FileManagerFragment;
import taras.clientwebsocketapp.utils.EventBusMsg;

import static taras.clientwebsocketapp.utils.Constants.CONTENT_FAVORITE;
import static taras.clientwebsocketapp.utils.Constants.CONTENT_USUAL;
import static taras.clientwebsocketapp.utils.Constants.FILE_MANAGER_TYPE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String LOG_TAG = "myLogs";
    private static final String BACK_STACK_TAG = "BACK_STACK_TAG";


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    ActionBarDrawerToggle toggle;

    private Drawable navigationIcon;

    private NavigationView navigationView;
    private Switch serverSwitch;
    private TextView toolbarTitle;

    MyReceiver myReceiver;

    private long lastTimeCallExit = System.currentTimeMillis();


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            String responseData = arg1.getStringExtra("string");
            Log.d(LOG_TAG, "onReceive: " + responseData);
        }
    }

    private FileManagerFragment fileManagerFragment;
    private FileManagerFragment favoriteFileManagerFragment;

    private SelectedFileManager selectedFileManager;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        selectedFileManager = new SelectedFileManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "MainActivity onCreate");
        startService();


        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        navigationIcon = toolbar.getNavigationIcon();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if (fileManagerFragment == null){
            fileManagerFragment = new FileManagerFragment();
        }
        Bundle fileManagerBundle = new Bundle();
        fileManagerBundle.putInt(FILE_MANAGER_TYPE, CONTENT_USUAL);
        fileManagerFragment.setArguments(fileManagerBundle);
        addFragmentToManager(fileManagerFragment);


        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setCheckedItem(R.id.menu_network_manager);
        navigationView.setCheckedItem(R.id.menu_file_manager);
        navigationView.setNavigationItemSelectedListener(this);

        FavoriteFilesManager.getInstance();
        //PermissionManagerClient.getPermissionManager();
        PermissionManager.getPermissionManager();
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
            if (lastTimeCallExit + 2000L > System.currentTimeMillis())
                MainActivity.this.finish();
            else {
                lastTimeCallExit = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, getString(R.string.txt_message_close_app), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem switchItem = menu.findItem(R.id.toolbar_switcher);
        switchItem.setActionView(R.layout.toolbar_layout);
        serverSwitch = menu.findItem(R.id.toolbar_switcher).getActionView().findViewById(R.id.switchServerToolbar);
        serverSwitch.setOnClickListener(this);
        //serverSwitch.setChecked(PreferenceUtils.getRunningServerState());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        EventBusMsg<String> message = null;
        if (serverSwitch.isChecked()){
            serverSwitch.setChecked(true);
            Snackbar.make(view, getString(R.string.server_on_visible), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            message = new EventBusMsg<String>(EventBusMsg.TO_SERVICE, EventBusMsg.SERVER_START, null);
        } else {
            serverSwitch.setChecked(false);
            Snackbar.make(view, getString(R.string.server_off_invisible), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            message = new EventBusMsg<String>(EventBusMsg.TO_SERVICE, EventBusMsg.SERVER_STOP, null);
        }
        EventBus.getDefault().postSticky(message);
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
        int id = item.getItemId();

        if (id == R.id.menu_file_manager) {
            if (fileManagerFragment == null){
                fileManagerFragment = new FileManagerFragment();
            }
            Bundle fileManagerBundle = new Bundle();
            fileManagerBundle.putInt(FILE_MANAGER_TYPE, CONTENT_USUAL);
            fileManagerFragment.setArguments(fileManagerBundle);
            addFragmentToManager(fileManagerFragment);
        } else if (id == R.id.menu_favorite) {
            if (favoriteFileManagerFragment == null){
                favoriteFileManagerFragment = new FileManagerFragment();
            }
            Bundle fileManagerBundle = new Bundle();
            fileManagerBundle.putInt(FILE_MANAGER_TYPE, CONTENT_FAVORITE);
            favoriteFileManagerFragment.setArguments(fileManagerBundle);
            addFragmentToManager(favoriteFileManagerFragment);
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







    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onNewPostCreated(EventBusMsg<Object> ebMessage) {
        if (ebMessage.getCodeDirection() == EventBusMsg.TO_APP){
            switch (ebMessage.getCodeType()){
                case EventBusMsg.PACKAGE_PERMISSION:
                    Log.d(LOG_TAG, "PACKAGE_PERMISSION_FIRST");
                    CheckPermissionDialog checkPermissionDialog = new CheckPermissionDialog();
                    checkPermissionDialog.setPermissionPackage((PermissionPackage) ebMessage.getModel());
                    checkPermissionDialog.show(getSupportFragmentManager(), CheckPermissionDialog.class.getSimpleName());
                    break;
                case EventBusMsg.CHECK_IS_SERVER_WORK:
                    setSwitchState((Boolean) ebMessage.getModel());
                    break;
            }
        }
        EventBus.getDefault().removeStickyEvent(ebMessage);
    }

    private void setSwitchState(boolean serverIsRun){
        if (serverSwitch != null){
            Log.d(LOG_TAG, "setSwitchState: " + serverIsRun);
            serverSwitch.setChecked(serverIsRun);
        }
    }

    //work with service
    private void startService(){
        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        startService(intent);
        initServiceReceiver();
        checkServerIsRunning();
    }
    private void initServiceReceiver(){
        if (myReceiver == null){
            myReceiver = new MainActivity.MyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BackgroundService.SERVICE_ACTION);
            registerReceiver(myReceiver, intentFilter);
        }
    }
    private void checkServerIsRunning(){
        EventBusMsg<String> message = new EventBusMsg<String>(EventBusMsg.TO_SERVICE, EventBusMsg.CHECK_IS_SERVER_WORK, null);
        EventBus.getDefault().postSticky(message);
    }
    //----------------------------------------------------------------------------------------------

    public static Context getContext(){
        return getContext();
    }

    public SelectedFileManager getSelectedFileManager() {
        return selectedFileManager;
    }
}
