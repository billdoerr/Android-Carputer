package com.billdoerr.android.carputer;

import androidx.fragment.app.Fragment;

public class CarputerActivity extends SingleFragmentActivity {

    private static final String TAG = "CarputerActivity";

    @Override
    protected Fragment createFragment() {
        return CarputerFragment.newInstance();
    }

}
