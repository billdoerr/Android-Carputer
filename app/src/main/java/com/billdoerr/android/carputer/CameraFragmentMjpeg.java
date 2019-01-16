package com.billdoerr.android.carputer;

import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CameraFragmentMjpeg extends Fragment {

    private static final String TAG = "CameraFragmentMjeg";

    //  Fragment arguments
    private static final String ARG_CAMERA_ADDRESS = "CAMERA_ADDRESS";
    private static final String ARG_CAMERA_ADDRESS_1 = "CAMERA_ADDRESS_1";
    private static final String ARG_CAMERA_ADDRESS_2 = "CAMERA_ADDRESS_2";

    //  Camera #1 preferences
    private static final String PREF_CAMERA_FRONT_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_FRONT_ENABLED";
    private static final String PREF_CAMERA_FRONT_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_FRONT_URL";

    //  Camera #1 preferences
    private static final String PREF_CAMERA_REAR_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_REAR_ENABLED";
    private static final String PREF_CAMERA_REAR_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_REAR_URL";

    //  Camera Two_Pane view preferences
    private static final String PREF_CAMERA_TWO_PANE_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_TWO_PANE_ENABLED";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static CameraFragmentMjpeg newInstance() {
        return new CameraFragmentMjpeg();
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
        addTabLayoutIcons();

        //  TODO : Are these needed or should I just comment out the code?
        mTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Log.d(TAG, "onTabSelected -> " + tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                Log.d(TAG, "onTabUnselected" + tab.getText().toString());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Log.d(TAG, "onTabReselected" + tab.getText().toString());
            }
        });

        return view;
    }

    //  Add fragments to tabs
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        //  TODO : Need to decide if both camera's should use CameraFragmentMjpegView or CameraFragmentMjpegSnapshot?

        //  Camera view fragment
        if (getPreferenceBoolean(PREF_CAMERA_FRONT_ENABLED)) {
            Bundle args = new Bundle();
            args.putString(ARG_CAMERA_ADDRESS,getPreferenceString(PREF_CAMERA_FRONT_URL));

            CameraFragmentMjpegSnapshot cameraFragmentMjpegSnapshot = new CameraFragmentMjpegSnapshot();
            cameraFragmentMjpegSnapshot.setArguments(args);

            adapter.addFragment(cameraFragmentMjpegSnapshot, getResources().getString(R.string.tab_camera_front));
        }

        //  Camera snapshot fragment
        if (getPreferenceBoolean(PREF_CAMERA_REAR_ENABLED)) {
            Bundle args = new Bundle();
            args.putString(ARG_CAMERA_ADDRESS,getPreferenceString(PREF_CAMERA_REAR_URL));

            CameraFragmentMjpegSnapshot cameraFragmentMjpegSnapshot = new CameraFragmentMjpegSnapshot();
            cameraFragmentMjpegSnapshot.setArguments(args);

            adapter.addFragment(cameraFragmentMjpegSnapshot, getResources().getString(R.string.tab_camera_rear));
        }

        //  Dual-camera view fragment
        if (getPreferenceBoolean(PREF_CAMERA_TWO_PANE_ENABLED)) {
            Bundle args = new Bundle();
            args.putString(ARG_CAMERA_ADDRESS_1,getPreferenceString(PREF_CAMERA_FRONT_URL));
            args.putString(ARG_CAMERA_ADDRESS_2,getPreferenceString(PREF_CAMERA_REAR_URL));

            CameraFragmentMjpegDualView cameraFragmentMjpegDualView = new CameraFragmentMjpegDualView();
            cameraFragmentMjpegDualView.setArguments(args);

            adapter.addFragment(cameraFragmentMjpegDualView, getResources().getString(R.string.tab_camera_front_and_rear));
        }

        //  Set adapter to view pager
        viewPager.setAdapter(adapter);
    }

    //  Setup action bar
    private void setupActionBar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((CameraActivityMjpeg)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((CameraActivityMjpeg)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
    }

    private void addTabLayoutIcons() {
        for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
//            mTabLayout.getTabAt(i).setIcon(R.drawable.ic_camera);
            String s = mTabLayout.getTabAt(i).getText().toString();
            //  Front camera
            if (s.equals(getResources().getString(R.string.tab_camera_front).toString())) {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_camera_front_24px);
            //  Camera rear
            } else if (s.equals(getResources().getString(R.string.tab_camera_rear).toString())) {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_camera_rear_24px);
            //  Dual view
            }  else if (s.equals(getResources().getString(R.string.tab_camera_front_and_rear).toString())) {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_linked_camera_24px);
            //  Default
            } else {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_camera_24px);
            }
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

        private ViewPagerAdapter(FragmentManager manager) {
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

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
