package com.billdoerr.android.carputer;

import androidx.fragment.app.Fragment;

/**
 * Camera motionEye activity which extends from SingleFragmentActivity.
 */
public class CameraActivityMotionEye extends SingleFragmentActivity {

    private static final String TAG = "CameraActivityMotionEye";

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMotionEye.newInstance();
    }

}