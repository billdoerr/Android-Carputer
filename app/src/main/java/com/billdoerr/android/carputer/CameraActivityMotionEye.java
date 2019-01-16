package com.billdoerr.android.carputer;

import androidx.fragment.app.Fragment;

public class CameraActivityMotionEye extends SingleFragmentActivity {

    private static final String TAG = "CameraActivityMotionEye";

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMotionEye.newInstance();
    }

}