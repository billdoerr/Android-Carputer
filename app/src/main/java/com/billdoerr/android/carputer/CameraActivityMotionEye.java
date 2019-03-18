package com.billdoerr.android.carputer;

import android.os.Bundle;

import com.billdoerr.android.carputer.utils.FileStorageUtils;

import androidx.fragment.app.Fragment;

/**
 * Camera motionEye activity which extends from SingleFragmentActivity.
 */
public class CameraActivityMotionEye extends SingleFragmentActivity {

    private static final String TAG = "CameraActivityMotionEye";

    // Calling Application class (see application tag in AndroidManifest.xml)
    private GlobalVariables mGlobalVariables;

    //  System logging
    FileStorageUtils mSystemLog = new FileStorageUtils();

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMotionEye.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startUp();
    }

    @Override
    protected void startUp() {

        // Calling Application class (see application tag in AndroidManifest.xml)
        mGlobalVariables = (GlobalVariables) getApplicationContext();

        //  System logging
        mSystemLog.initializeSystemLog(getApplicationContext(), mGlobalVariables.SYS_LOG);

        mSystemLog.writeSystemLog(TAG + ": Application starting.");
    }

}