package com.billdoerr.android.carputer.activities;

import androidx.fragment.app.Fragment;

import com.billdoerr.android.carputer.fragments.CameraFragmentMotionEye;

/**
 * Carputer activity which extends from BaseActivity.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMotionEye.newInstance();
    }

}
