package com.billdoerr.android.carputer;

import android.support.v4.app.Fragment;

public class CameraActivity extends SingleFragmentActivity {

    private static final String TAG = "CameraActivity";

    @Override
    protected Fragment createFragment() {
        return CameraFragment.newInstance();
    }

}
