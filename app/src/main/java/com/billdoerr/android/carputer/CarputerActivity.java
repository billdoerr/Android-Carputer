package com.billdoerr.android.carputer;

import android.support.v4.app.Fragment;

public class CarputerActivity extends SingleFragmentActivity {

    private static final String TAG = "CarputerActivity";

    @Override
    protected Fragment createFragment() {
        return CarputerFragment.newInstance();
    }

}
