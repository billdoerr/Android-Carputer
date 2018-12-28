package com.billdoerr.android.carputer;

import android.support.v4.app.Fragment;

public class CameraActivityMjpeg extends SingleFragmentActivity {

    private static final String TAG = "CameraActivityMjpeg";

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMjpeg.newInstance();
    }

}
