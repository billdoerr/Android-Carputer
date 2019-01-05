package com.billdoerr.android.carputer;

import android.support.v4.app.Fragment;

import com.billdoerr.android.carputer.SSHFragment;
import com.billdoerr.android.carputer.SingleFragmentActivity;

public class CarputerActivityMgmt extends SingleFragmentActivity {

    private static final String TAG = "CarputerActivityMgmt";

    @Override
    protected Fragment createFragment() {
        return CarputerFragmentMgmt.newInstance();
    }

}
