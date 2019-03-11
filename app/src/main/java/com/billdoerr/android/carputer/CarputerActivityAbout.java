package com.billdoerr.android.carputer;

import androidx.fragment.app.Fragment;

/**
 *
 */
public class CarputerActivityAbout extends SingleFragmentActivity {

    private static final String TAG = "CarputerActivityAbout";

    @Override
    protected Fragment createFragment() {
        return CarputerFragmentAbout.newInstance();
    }
}
