package com.billdoerr.android.carputer.activities;

import androidx.fragment.app.Fragment;

import com.billdoerr.android.carputer.fragments.CarputerFragmentMgmt;

/**
 * Carputer management activity which extends from SingleFragmentActivity.
 */
public class CarputerActivityMgmt extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CarputerFragmentMgmt.newInstance();
    }

}
