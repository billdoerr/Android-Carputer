package com.billdoerr.android.carputer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.billdoerr.android.carputer.settings.SettingsActivity;
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
import android.view.View;


public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String TAG = "SingleFragmentActivity";

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

}
