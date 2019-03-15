package com.billdoerr.android.carputer;

import androidx.fragment.app.Fragment;

/**
 * Carputer management activity which extends from SingleFragmentActivity.
 */
public class CarputerActivityMgmt extends SingleFragmentActivity {

    private static final String TAG = "CarputerActivityMgmt";

    @Override
    protected Fragment createFragment() {
        return CarputerFragmentMgmt.newInstance();
    }

}
