package com.billdoerr.android.carputer;

import android.support.v4.app.Fragment;

public class CameraActivityImageArchive extends SingleFragmentActivity {

    private static final String TAG = "CameraActivityImageArchive";

    @Override
    protected Fragment createFragment() {
        return CameraFragmentImageArchive.newInstance();
    }

}
