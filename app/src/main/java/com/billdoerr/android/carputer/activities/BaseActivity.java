package com.billdoerr.android.carputer.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.LayoutRes;

import com.billdoerr.android.carputer.fragments.CameraFragmentImageArchive;
import com.billdoerr.android.carputer.fragments.CameraFragmentMjpeg;
import com.billdoerr.android.carputer.fragments.CameraFragmentMotionEye;
import com.billdoerr.android.carputer.fragments.CarputerFragmentAbout;
import com.billdoerr.android.carputer.fragments.CarputerFragmentMgmt;
import com.billdoerr.android.carputer.utils.GlobalVariables;
import com.billdoerr.android.carputer.R;
import com.billdoerr.android.carputer.settings.SettingsActivity;
import com.billdoerr.android.carputer.utils.FileStorageUtils;
import com.billdoerr.android.carputer.utils.NetworkChangeReceiver;
import com.billdoerr.android.carputer.utils.WifiUtils;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.Objects;

/**
 * Main activity which other activities extend from.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    // Calling Application class (see application tag in AndroidManifest.xml)
    private GlobalVariables mGlobalVariables;

    //  Broadcast receiver
    private NetworkChangeReceiver mNetworkChangeReceiver;

    //  Menu
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    protected abstract Fragment createFragment();

    @SuppressWarnings({"SameReturnValue", "unused"})
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.drawer_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_view);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }

        // Implement Toolbar
        mToolbar = createToolbar();

        // Implement Drawer View
        createDrawerView();

        // Implement NavigationView
        createNavigationView();

        //  Initialization routine
        systemInitialization();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop()
    {
        //  Unregister broadcast receiver
        try {
            this.unregisterReceiver(mNetworkChangeReceiver);
        }
        catch (final Exception exception) {
            // The receiver was not registered.
            // There is nothing to do in that case.
            // Everything is fine.
        }
        super.onStop();
    }

    /**
     * Create Toolbar
     */
    private Toolbar createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        try {
//           actionbar.setDisplayHomeAsUpEnabled(true);
            showBackArrow();
            Objects.requireNonNull(actionbar).setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return toolbar;
    }

    /**
     * actionBar.setHomeButtonEnabled(true) will just make the icon clickable,
     * with the color at the background of the icon as a feedback of the click.
     *
     * actionBar.setDisplayHomeAsUpEnabled(true) will make the icon clickable
     * and add the < at the left of the icon.
     */
    private void showBackArrow() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            // Set whether to include the application home affordance in the action bar.
            // (and put a back mark at icon in ActionBar for "up" navigation)
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            // Enable or disable the "home" button in the corner of the action bar.
            // (clickable or not)
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Implements Drawer View
     */
    private void createDrawerView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    /**
     * Creates the NavigationView for use with DrawerLayout
     */
    @SuppressWarnings("SameReturnValue")
    private void createNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {

                    Fragment fragment;

                    // Set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // Close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Launch activities
                    switch (menuItem.getItemId()) {
                        //  Activity:  Mjpeg
                        case R.id.nav_camera:
                            fragment = new CameraFragmentMjpeg();
                            loadFragmentReplace(fragment);
                            return true;
                        //  Activity:  MotionEye
                        case R.id.nav_motioneye:
                            fragment = new CameraFragmentMotionEye();
                            loadFragmentReplace(fragment);
                            return true;
                        //  Activity:  Image Archive
                        case R.id.nav_image_archive:
                            fragment = new CameraFragmentImageArchive();
                            loadFragmentReplace(fragment);
                            return true;
                        //  Activity:  Carputer Management
                        case R.id.nav_management:
                            fragment = new CarputerFragmentMgmt();
                            loadFragmentReplace(fragment);
                            return true;
                        //  Activity:  Settings
                        case R.id.nav_settings:
                            startActivity(new Intent(BaseActivity.this, SettingsActivity.class));
                            return true;
                        case R.id.nav_about:
                            fragment = new CarputerFragmentAbout();
                            loadFragmentReplace(fragment);
                            return true;
                        default:
                            return true;
                    }
                });
    }

//    /**
//     * Creates fragment.  https://www.simplifiedcoding.net/bottom-navigation-android-example/
//     * @param fragment Fragment
//     */
//    private void loadFragmentAdd(Fragment fragment) {
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.fragment_container, fragment)
//                    .commit();
//        }
//    }

    /**
     * Creates fragment.  https://www.simplifiedcoding.net/bottom-navigation-android-example/
     * @param fragment Fragment
     */
    private void loadFragmentReplace(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragment_container)))
                    .replace(R.id.fragment_container, fragment)
//                    .addToBackStack( fragment.getClass().getSimpleName() )
                    .commit();
        }
    }

    /**
     * Format shared preferences to be used for output to the system log.
     * @return String:  Formatted string of shared preferences.
     */
    private String formatSharedPreferences() {

        String entry = TAG + ": Shared preferences" + FileStorageUtils.LINE_SEPARATOR;

        entry = entry + GlobalVariables.PREF_KEY_CAMERAS + ":\t"  + FileStorageUtils.LINE_SEPARATOR + Arrays.toString(mGlobalVariables.getCameras().toArray()) + FileStorageUtils.LINE_SEPARATOR;
        entry = entry + GlobalVariables.PREF_KEY_NODES + ":\t"  + FileStorageUtils.LINE_SEPARATOR + Arrays.toString(mGlobalVariables.getNodes().toArray()) + FileStorageUtils.LINE_SEPARATOR;

        entry = entry + GlobalVariables.PREF_KEY_NETWORK_ENABLED + ":\t" + mGlobalVariables.isNetworkEnabled() + FileStorageUtils.LINE_SEPARATOR;
        entry = entry + GlobalVariables.PREF_KEY_NETWORK_NAME + ":\t" + mGlobalVariables.getNetworkName() + FileStorageUtils.LINE_SEPARATOR;
        entry = entry + GlobalVariables.PREF_KEY_NETWORK_PASSPHRASE + ":\t" + mGlobalVariables.getNetworkPassphrase() + FileStorageUtils.LINE_SEPARATOR;
        entry = entry + GlobalVariables.PREF_KEY_KEEP_DEVICE_AWAKE + ":\t" + mGlobalVariables.isKeepDeviceAwake() + FileStorageUtils.LINE_SEPARATOR;

        return entry;
    }

    /**
     * Application startup routine.
     */
    private void systemInitialization() {

        // Calling Application class (see application tag in AndroidManifest.xml)
        mGlobalVariables = (GlobalVariables) getApplicationContext();

        if (!mGlobalVariables.getIsInitialized()) {
            mGlobalVariables.setIsInitialized(true);
            writeSystemLog(TAG + ":\t" + getString(R.string.msg_application_starting) + FileStorageUtils.LINE_SEPARATOR);
            writeSystemLog(TAG + formatSharedPreferences());

            //  Connect to network if enabled in shared preferences
            if (mGlobalVariables.isNetworkEnabled()) {
                WiFiConnect();
            }

        }

        //  This is kept outside the above 'if (!mGlobalVariables.getIsInitialized()' so it is run by each activity.
        // Goal is to prevent network from being dropped.  Plus we always want the application to never timeout.  Always viewable.
        //  https://developer.android.com/training/scheduling/wakelock
        if (mGlobalVariables.isKeepDeviceAwake()) {
            writeSystemLog(TAG + ":\t" + getString(R.string.msg_keep_device_awake) + FileStorageUtils.LINE_SEPARATOR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        //  TODO:  v1.4.1  WORK-IN-PROGRESS
        //  Start network broadcast receiver
        startNetworkChangeReceiver();

    }

    /**
     * Connect to network (WPA).
     */
    private void WiFiConnect() {

        boolean isConnected = false;
        String msg;

        //  Networking
        WifiUtils wifiUtils = WifiUtils.getInstance();

        //  Prepare system log and console message
        writeSystemLog(TAG + ":\t" + getString(R.string.msg_network_connecting_now) + FileStorageUtils.LINE_SEPARATOR);

        if (!wifiUtils.isConnected()) {
            isConnected = wifiUtils.connectWPA(getApplicationContext(), mGlobalVariables.getNetworkName(), mGlobalVariables.getNetworkPassphrase());
        }


        if (isConnected) {
            msg = getString(R.string.msg_network_connection_success) + ": " + mGlobalVariables.getNetworkName() + FileStorageUtils.LINE_SEPARATOR;
        } else {
            msg = getString(R.string.msg_network_connection_fail) + ": " + mGlobalVariables.getNetworkName() + FileStorageUtils.LINE_SEPARATOR;
        }
        writeSystemLog(TAG + ":\t" + msg);

    }

    //  Output to system log
    private void writeSystemLog(String msg) {
        FileStorageUtils.writeSystemLog(this.getApplicationContext(), GlobalVariables.SYS_LOG,TAG + FileStorageUtils.TABS + msg + FileStorageUtils.LINE_SEPARATOR);
    }

    /**
     * Register Broadcast Receiver
     */
    private void startNetworkChangeReceiver() {
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        final IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        this.registerReceiver(mNetworkChangeReceiver, filters);
    }

}
