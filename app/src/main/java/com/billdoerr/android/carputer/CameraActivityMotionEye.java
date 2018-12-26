package com.billdoerr.android.carputer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

public class CameraActivityMotionEye extends SingleFragmentActivity {

    private static final String TAG = "CameraActivityMotionEye";

    public static Intent newIntent(Context context, Uri uri) {
        Intent intent = new Intent(context, CameraActivityMotionEye.class);
        intent.setData(uri);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return CameraFragmentMotionEye.newInstance(getIntent().getData());
    }

}