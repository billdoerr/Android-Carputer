package com.billdoerr.android.carputer.activities;

import androidx.fragment.app.Fragment;

import com.billdoerr.android.carputer.fragments.CameraFragmentImageArchive;

/**
 * Camera ImageArchive activity which extends from SingleFragmentActivity.
 */
public class CameraActivityImageArchive extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CameraFragmentImageArchive.newInstance();
    }

}
