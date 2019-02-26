package com.billdoerr.android.carputer.settings;

import android.os.Bundle;

import com.billdoerr.android.carputer.R;

import androidx.preference.PreferenceFragmentCompat;

/**
 * The fragment that displays preferences that Node detail
 */
public class SettingsFragmentNodeDetail extends PreferenceFragmentCompat {

    private static final String TAG = "NodeDetail";

    public SettingsFragmentNodeDetail() {
        // Required empty public constructor
    }

    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_node_settings, rootKey);
    }

}

