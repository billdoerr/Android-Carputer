package com.billdoerr.android.carputer.activities;

import androidx.fragment.app.Fragment;

import com.billdoerr.android.carputer.fragments.CarputerFragment;

/**
 * Carputer activity which extends from SingleFragmentActivity.
 */
public class CarputerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CarputerFragment.newInstance();
    }

}
