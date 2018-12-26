package com.billdoerr.android.carputer;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CameraFragment extends Fragment {

    private static final String TAG = "CameraFragment";

    //  Fragment arguments
    private static final String ARG_CAMERA_ADDRESS_1 = "CAMERA_ADDRESS_1";
    private static final String ARG_CAMERA_ADDRESS_2 = "CAMERA_ADDRESS_2";

    //  Camera #1 preferences
    public static final String PREF_CAMERA_1_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_1_ENABLED";
    public static final String PREF_CAMERA_1_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_1_URL";
    public static final String PREF_CAMERA_1_FLIP_HORIZONTAL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_1_FLIP_HORIZONTAL";
    public static final String PREF_CAMERA_1_FLIP_VERTICAL= "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_1_FLIP_VERTICAL";
    public static final String PREF_CAMERA_1_ROTATE_DEGREES = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_1_ROTATE_DEGREES";
    public static final String PREF_CAMERA_1_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_1_AUTH_USERNAME";
    public static final String PREF_CAMERA_1_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_1_AUTH_PASSWORD";

    //  Camera #1 preferences
    public static final String PREF_CAMERA_2_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_2_ENABLED";
    public static final String PREF_CAMERA_2_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_2_URL";
    public static final String PREF_CAMERA_2_FLIP_HORIZONTAL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_2_FLIP_HORIZONTAL";
    public static final String PREF_CAMERA_2_FLIP_VERTICAL= "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_2_FLIP_VERTICAL";
    public static final String PREF_CAMERA_2_ROTATE_DEGREES = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_2_ROTATE_DEGREES";
    public static final String PREF_CAMERA_2_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_2_AUTH_USERNAME";
    public static final String PREF_CAMERA_2_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_2_AUTH_PASSWORD";

    //  Camera Two_Pane view preferences
    public static final String PREF_CAMERA_TWO_PANE_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_TWO_PANE_ENABLED";

    private CameraFragmentView mCameraFragmentView;
    private CameraFragmentSnapshot mCameraFragmentSnapshot;
    private CameraFragmentTwoPane mCameraFragmentTwoPane;
    private CameraFragmentSnapshotFileExplorer mCameraFragmentFileExplorer;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        //  Setup action bar
        setupActionBar(view);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        //  Add icons
        addTablayoutIcons();

        mTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected -> " + tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabUnselected" + tab.getText().toString());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabReselected" + tab.getText().toString());
            }
        });

        return view;
    }

    //  TODO:  HIGH PRIORITY ->  Change to passing args
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        //  Camera View fragment
        if (getPreferenceBoolean(PREF_CAMERA_1_ENABLED)) {
            Bundle args = new Bundle();
            args.putString(ARG_CAMERA_ADDRESS_1,getPreferenceString(PREF_CAMERA_1_URL));

            mCameraFragmentView = new CameraFragmentView();
            mCameraFragmentView.setArguments(args);

            adapter.addFragment(mCameraFragmentView, getResources().getString(R.string.tab_camera_one));
        }

        //  Camera #2 fragment
        if (getPreferenceBoolean(PREF_CAMERA_2_ENABLED)) {
            Bundle args = new Bundle();
            args.putString(ARG_CAMERA_ADDRESS_2,getPreferenceString(PREF_CAMERA_2_URL));

            mCameraFragmentSnapshot = new CameraFragmentSnapshot();
            mCameraFragmentSnapshot.setArguments(args);

            adapter.addFragment(mCameraFragmentSnapshot, getResources().getString(R.string.tab_camera_two));
        }

        //  Two-camera view fragment
        if (getPreferenceBoolean(PREF_CAMERA_TWO_PANE_ENABLED)) {
            Bundle args = new Bundle();
            args.putString(ARG_CAMERA_ADDRESS_1,getPreferenceString(PREF_CAMERA_1_URL));
            args.putString(ARG_CAMERA_ADDRESS_2,getPreferenceString(PREF_CAMERA_2_URL));

            mCameraFragmentTwoPane = new CameraFragmentTwoPane();
            mCameraFragmentTwoPane.setArguments(args);

            adapter.addFragment(mCameraFragmentTwoPane, getResources().getString(R.string.tab_camera_two_pane));
    }

        //  Snapshot fragment
        mCameraFragmentFileExplorer = new CameraFragmentSnapshotFileExplorer();
        adapter.addFragment(mCameraFragmentFileExplorer, getResources().getString(R.string.tab_camera_file_explorer));

        viewPager.setAdapter(adapter);
    }

    //  Setup action bar
    private void setupActionBar(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((CameraActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionbar = ((CameraActivity)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
    }

    private void addTablayoutIcons() {

        for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(R.drawable.ic_camera);
        }

    }

    private String getPreferenceString(String key) {
        return PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(key, "");
    }

    private boolean getPreferenceBoolean(String key) {
        return PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getBoolean(key, false );
    }


    // View Adapter
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
