package com.billdoerr.android.carputer;

import androidx.fragment.app.Fragment;

/**
 * Camera ImageArchive activity which extends from SingleFragmentActivity.
 */
public class CameraActivityImageArchive extends SingleFragmentActivity {

    private static final String TAG = "CameraActivityImageArchive";

    @Override
    protected Fragment createFragment() {
        return CameraFragmentImageArchive.newInstance();
    }

}
