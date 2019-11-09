package com.billdoerr.android.carputer.activities;

import androidx.fragment.app.Fragment;

import com.billdoerr.android.carputer.fragments.CameraFragmentMotionEye;

/**
 * Camera motionEye activity which extends from SingleFragmentActivity.
 */
public class CameraActivityMotionEye extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMotionEye.newInstance();
    }

}