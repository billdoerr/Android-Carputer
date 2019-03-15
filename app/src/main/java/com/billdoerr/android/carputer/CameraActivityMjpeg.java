package com.billdoerr.android.carputer;

import androidx.fragment.app.Fragment;

/**
 *  * Camera MjPeg streaming activity which extends from SingleFragmentActivity.
 */
public class CameraActivityMjpeg extends SingleFragmentActivity {

    private static final String TAG = "CameraActivityMjpeg";

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMjpeg.newInstance();
    }

}
