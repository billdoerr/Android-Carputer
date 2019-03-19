package com.billdoerr.android.carputer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.billdoerr.android.carputer.settings.SettingsActivity;
import com.billdoerr.android.carputer.utils.FileStorageUtils;
import com.billdoerr.android.carputer.utils.WiFiUtils;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.WindowManager;

import java.util.Arrays;

/**
 * Main activity which other activities extend from.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String TAG = "SingleFragmentActivity";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    // Calling Application class (see application tag in AndroidManifest.xml)
    private GlobalVariables mGlobalVariables;

    //  Networking
    private WiFiUtils mWiFiUtils;

    //  Menu
    private DrawerLayout mDrawerLayout;

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_view);

        //  System logging
        FileStorageUtils.initializeSystemLog(this.getApplicationContext(), mGlobalVariables.SYS_LOG);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // Set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // Close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Launch activities
                        switch (menuItem.getItemId()) {
                            //  Activity:  Mjpeg
                            case R.id.nav_camera:
                                startActivity(new Intent(SingleFragmentActivity.this, CameraActivityMjpeg.class));
                                return true;
                            //  Activity:  MotionEye
                            case R.id.nav_motioneye:
                                startActivity(new Intent(SingleFragmentActivity.this, CameraActivityMotionEye.class));
                                return true;
                            //  Activity:  Image Archive
                            case R.id.nav_image_archive:
                                startActivity(new Intent(SingleFragmentActivity.this, CameraActivityImageArchive.class));
                                return true;
                            //  Activity:  Carputer Management
                            case R.id.nav_management:
                                startActivity(new Intent(SingleFragmentActivity.this, CarputerActivityMgmt.class));
                                return true;
                            //  Activity:  Settings
                            case R.id.nav_settings:
                                startActivity(new Intent(SingleFragmentActivity.this, SettingsActivity.class));
                                return true;
                            case R.id.nav_about:
                                startActivity(new Intent(SingleFragmentActivity.this, CarputerActivityAbout.class));
                            default:
                                return true;
                        }
                    }
                });

        //  Initialization routine
        systemInitialization();

        //  Connect to network if enabled in shared preferences
        if (mGlobalVariables.isNetworkEnabled()) {
            WiFiConnect();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Format shared preferences to be used for output to the system log.
     * @return String:  Formatted string of shared preferences.
     */
    private String formatSharedPreferences() {

        String entry = TAG + ": Shared preferences" + LINE_SEPARATOR;

        entry = entry + mGlobalVariables.PREF_KEY_CAMERAS + ":\t"  + LINE_SEPARATOR + Arrays.toString(mGlobalVariables.getCameras().toArray()) + LINE_SEPARATOR;
        entry = entry + mGlobalVariables.PREF_KEY_NODES + ":\t"  + LINE_SEPARATOR + Arrays.toString(mGlobalVariables.getNodes().toArray()) + LINE_SEPARATOR;

        entry = entry + mGlobalVariables.PREF_KEY_NETWORK_ENABLED + ":\t" + mGlobalVariables.isNetworkEnabled() + LINE_SEPARATOR;
        entry = entry + mGlobalVariables.PREF_KEY_NETWORK_NAME + ":\t" + mGlobalVariables.getNetworkName() + LINE_SEPARATOR;
        entry = entry + mGlobalVariables.PREF_KEY_NETWORK_PASSPHRASE + ":\t" + mGlobalVariables.getNetworkPassphrase() + LINE_SEPARATOR;
        entry = entry + mGlobalVariables.PREF_KEY_KEEP_DEVICE_AWAKE + ":\t" + mGlobalVariables.isKeepDeviceAwake() + LINE_SEPARATOR;

        return entry;
    }

    /**
     * Application startup routine.
     */
    protected void systemInitialization() {

        // Calling Application class (see application tag in AndroidManifest.xml)
        mGlobalVariables = (GlobalVariables) getApplicationContext();

        //  TODO:  How to only do this once rather than having to override in each activity?
        FileStorageUtils.writeSystemLog(TAG + ": Application starting.");
        FileStorageUtils.writeSystemLog(TAG + formatSharedPreferences());

        //  Goal is to prevent network from being dropped.  Plus we always want the application to never timeout.  Always viewable.
        //  https://developer.android.com/training/scheduling/wakelock
        if (mGlobalVariables.isKeepDeviceAwake()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            FileStorageUtils.writeSystemLog(TAG + ": Keep device awake enabled.");
        }

    }

    /**
     * Connect to network (WPA).
     */
    private void WiFiConnect() {

        boolean isConnected = false;

        //  Networking
        mWiFiUtils = WiFiUtils.getInstance(getApplicationContext());

        if (!mWiFiUtils.isConnected()) {
            isConnected = mWiFiUtils.connectWPA(getApplicationContext(), mGlobalVariables.getNetworkName(), mGlobalVariables.getNetworkPassphrase());
        }

        String log = "";
        if (isConnected) {
            log = getString(R.string.msg_network_connection_success) + ": " + mGlobalVariables.getNetworkName() + "\n";
        } else {
            log = getString(R.string.msg_network_connection_fail) + ": " + mGlobalVariables.getNetworkName() + "\n";
        }
        FileStorageUtils.writeSystemLog(TAG + ": " + log);

    }

}
