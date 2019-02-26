package com.billdoerr.android.carputer.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import androidx.appcompat.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.billdoerr.android.carputer.R;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    //  Camera #1 preferences
    private static final String PREF_CAMERA_FRONT_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_FRONT_ENABLED";
    private static final String PREF_CAMERA_FRONT_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_FRONT_URL";
    private static final String PREF_CAMERA_FRONT_FLIP_HORIZONTAL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_FRONT_FLIP_HORIZONTAL";
    private static final String PREF_CAMERA_FRONT_FLIP_VERTICAL= "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_FRONT_FLIP_VERTICAL";
    private static final String PREF_CAMERA_FRONT_ROTATE_DEGREES = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_FRONT_ROTATE_DEGREES";
    private static final String PREF_CAMERA_FRONT_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_FRONT_AUTH_USERNAME";
    private static final String PREF_CAMERA_FRONT_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_FRONT_AUTH_PASSWORD";

    //  Camera #2 preferences
    private static final String PREF_CAMERA_REAR_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_REAR_ENABLED";
    private static final String PREF_CAMERA_REAR_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_REAR_URL";
    private static final String PREF_CAMERA_REAR_FLIP_HORIZONTAL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_REAR_FLIP_HORIZONTAL";
    private static final String PREF_CAMERA_REAR_FLIP_VERTICAL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_REAR_FLIP_VERTICAL";
    private static final String PREF_CAMERA_REAR_ROTATE_DEGREES = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_REAR_ROTATE_DEGREES";
    private static final String PREF_CAMERA_REAR_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_REAR_AUTH_USERNAME";
    private static final String PREF_CAMERA_REAR_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_REAR_AUTH_PASSWORD";

    //  Camera Two_Pane view preferences
    private static final String PREF_CAMERA_TWO_PANE_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_TWO_PANE_ENABLED";

    //  Motion Eye preferences
    private static final String PREF_CAMERA_MOTIONEYE_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_MOTIONEYE_URL";
    private static final String PREF_CAMERA_MOTIONEYE_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_MOTIONEYE_AUTH_USERNAME";
    private static final String PREF_CAMERA_MOTIONEYE_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_MOTIONEYE_AUTH_PASSWORD";

    //  Carputer Management preferences
    private static final String PREF_RASPBERRYPI_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_ENABLED";
    private static final String PREF_RASPBERRYPI_IP = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_IP";
    private static final String PREF_RASPBERRYPI_SSH_PORT = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_SSH_PORT";
    private static final String PREF_RASPBERRYPI_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_USERNAME";
    private static final String PREF_RASPBERRYPI_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_PASSWORD";
    private static final String PREF_RASPBERRYPI_PHPSYSINFO_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_PHPSYSINFO_ENABLED";
    private static final String PREF_RASPBERRYPI_PHPSYSINFO_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_PHPSYSINFO_URL";


    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

            } else if ( preference.getKey().equals(PREF_CAMERA_FRONT_AUTH_PASSWORD)
                    || preference.getKey().equals(PREF_CAMERA_REAR_AUTH_PASSWORD)
                    || preference.getKey().equals(PREF_CAMERA_MOTIONEYE_AUTH_PASSWORD)
                    || preference.getKey().equals(PREF_RASPBERRYPI_AUTH_PASSWORD) ) {
                if (!TextUtils.isEmpty(stringValue)) {
                    preference.setSummary(R.string.pref_default_hidden_password);
                }
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
//        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
//                PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
        if (preference instanceof SwitchPreference)  {  // Added special handling as boolean
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext()).getBoolean(preference.getKey(),true));
        }
        else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }


    // Trigger the listener immediately with the preference's
    // current value.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //  Setup action bar
        setupActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    //  Setup action bar
    private void setupActionBar() {
        // get the root container of the preferences list
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        setSupportActionBar(toolbar);
        root.addView(toolbar, 0); // insert at top

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //  Adds padding so toolbar doesn't overlap list
    private void padListView() {
        //  https://gldraphael.com/blog/adding-a-toolbar-to-preference-activity/
        int horizontalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int verticalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) getResources().getDimension(R.dimen.activity_vertical_margin) + 50, getResources().getDisplayMetrics());
        getListView().setPadding(horizontalMargin, topMargin, horizontalMargin, verticalMargin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || CameraFrontPreferenceFragment.class.getName().equals(fragmentName)
                || CameraRearPreferenceFragment.class.getName().equals(fragmentName)
                || CameraMotionEyePreferenceFragment.class.getName().equals(fragmentName)
                || CarputerMgmtPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows Camera #1 preferences only.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CameraFrontPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_camera_front);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_FRONT_ENABLED));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_FRONT_URL));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_FRONT_FLIP_HORIZONTAL));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_FRONT_FLIP_VERTICAL));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_FRONT_ROTATE_DEGREES));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_FRONT_AUTH_USERNAME));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_FRONT_AUTH_PASSWORD));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows Camera #2 preferences only.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CameraRearPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_camera_rear);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_REAR_ENABLED));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_REAR_URL));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_REAR_FLIP_HORIZONTAL));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_REAR_FLIP_VERTICAL));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_REAR_ROTATE_DEGREES));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_REAR_AUTH_USERNAME));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_REAR_AUTH_PASSWORD));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_TWO_PANE_ENABLED));
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows MotionEye preferences only.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CameraMotionEyePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_camera_motioneye);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_MOTIONEYE_URL));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_MOTIONEYE_AUTH_USERNAME));
            bindPreferenceSummaryToValue(findPreference(PREF_CAMERA_MOTIONEYE_AUTH_PASSWORD));
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows Carputer Management preferences only.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CarputerMgmtPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_carputer_mgmt);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(PREF_RASPBERRYPI_ENABLED));
            bindPreferenceSummaryToValue(findPreference(PREF_RASPBERRYPI_IP));
            bindPreferenceSummaryToValue(findPreference(PREF_RASPBERRYPI_SSH_PORT));
            bindPreferenceSummaryToValue(findPreference(PREF_RASPBERRYPI_AUTH_USERNAME));
            bindPreferenceSummaryToValue(findPreference(PREF_RASPBERRYPI_AUTH_PASSWORD));
            bindPreferenceSummaryToValue(findPreference(PREF_RASPBERRYPI_PHPSYSINFO_ENABLED));
            bindPreferenceSummaryToValue(findPreference(PREF_RASPBERRYPI_PHPSYSINFO_URL));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
