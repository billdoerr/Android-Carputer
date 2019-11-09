package com.billdoerr.android.carputer.activities;

import androidx.fragment.app.Fragment;

import com.billdoerr.android.carputer.fragments.CameraFragmentMjpeg;

/**
 *  * Camera MjPeg streaming activity which extends from SingleFragmentActivity.
 */
public class CameraActivityMjpeg extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMjpeg.newInstance();
    }

}
