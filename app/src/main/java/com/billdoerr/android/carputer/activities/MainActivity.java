package com.billdoerr.android.carputer.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.billdoerr.android.carputer.fragments.CameraFragmentMotionEye;
import com.billdoerr.android.carputer.utils.GlobalVariables;

/**
 * Carputer activity which extends from BaseActivity.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMotionEye.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalVariables globalVariables = (GlobalVariables) getApplicationContext();
        boolean enabled = globalVariables.isBottomNavigationEnabled();

        // Display BottomNavigationView
        setBottomNavigationViewVisibility(enabled);
    }

}
