package com.billdoerr.android.carputer;

import android.support.v4.app.Fragment;

public class CameraActivityMotionEye extends SingleFragmentActivity {

    private static final String TAG = "CameraActivityMotionEye";

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMotionEye.newInstance();
    }

}