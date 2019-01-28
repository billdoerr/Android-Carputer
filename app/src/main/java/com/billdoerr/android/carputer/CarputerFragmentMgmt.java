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

public class CarputerFragmentMgmt extends Fragment {

    private static final String TAG = "CarputerFragmentMgmt";
    private static final String PREF_RASPBERRYPI_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_ENABLED";
    private static final String PREF_RASPBERRYPI_IP = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_IP";
    private static final String PREF_RASPBERRYPI_SSH_PORT = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_SSH_PORT";
    private static final String PREF_RASPBERRYPI_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_USERNAME";
    private static final String PREF_RASPBERRYPI_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_PASSWORD";
    private static final String PREF_RASPBERRYPI_PHPSYSINFO_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_PHPSYSINFO_ENABLED";
    private static final String PREF_RASPBERRYPI_PHPSYSINFO_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_PHPSYSINFO_URL";

    //  TODO :  Hack until I get SettingsActivity to be more robust
    private static final String PREF_RASPBERRYPI_IP_2 = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_IP_2";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static CarputerFragmentMgmt newInstance() {
        return new CarputerFragmentMgmt();
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

        //  Fragment:  RaspberryPi SSH
        if ( getPreferenceBoolean(PREF_RASPBERRYPI_ENABLED) ) {
            Bundle args = new Bundle();
            //  TODO :  Hack until I get SettingsActivity to be more robust
            args.putString(PREF_RASPBERRYPI_IP_2,"192.168.4.5");

            args.putString(PREF_RASPBERRYPI_IP,getPreferenceString(PREF_RASPBERRYPI_IP));
            args.putString(PREF_RASPBERRYPI_SSH_PORT,getPreferenceString(PREF_RASPBERRYPI_SSH_PORT));
            args.putString(PREF_RASPBERRYPI_AUTH_USERNAME,getPreferenceString(PREF_RASPBERRYPI_AUTH_USERNAME));
            args.putString(PREF_RASPBERRYPI_AUTH_PASSWORD,getPreferenceString(PREF_RASPBERRYPI_AUTH_PASSWORD));
            SSHFragment sshFragment = new SSHFragment();
            sshFragment.setArguments(args);
            adapter.addFragment(sshFragment, getResources().getString(R.string.tab_carputer_mgmt_ssh));
        }

        //  Fragment:  RaspberryPi phpSysInfo
        if ( getPreferenceBoolean(PREF_RASPBERRYPI_ENABLED) && getPreferenceBoolean(PREF_RASPBERRYPI_PHPSYSINFO_ENABLED) ) {
            Bundle args = new Bundle();
            args.putString(PREF_RASPBERRYPI_PHPSYSINFO_URL,getPreferenceString(PREF_RASPBERRYPI_PHPSYSINFO_URL));
            CarputerFragmentMgmtPhySysInfoView carputerFragmentMgmtPhySysInfoView = new CarputerFragmentMgmtPhySysInfoView();
            carputerFragmentMgmtPhySysInfoView.setArguments(args);
            adapter.addFragment(carputerFragmentMgmtPhySysInfoView, getResources().getString(R.string.tab_carputer_mgmt_phpsysinfo));
        }

        //  Fragment:  Add additional fragments here
        //  ...

        //  Set adapter to view pager
        viewPager.setAdapter(adapter);
    }

    //  Setup action bar
    private void setupActionBar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((CarputerActivityMgmt)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((CarputerActivityMgmt)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
    }

    private void addTabLayoutIcons() {
        for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(R.drawable.ic_camera);
            String s = mTabLayout.getTabAt(i).getText().toString();
            //  SSH
            if (s.equals(getResources().getString(R.string.tab_carputer_mgmt_ssh).toString())) {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_ssh_24px);
            //  phpSysInfo
            } else if (s.equals(getResources().getString(R.string.tab_carputer_mgmt_phpsysinfo).toString())) {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_phpsysinfo_24px);
            //  Default
            } else {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_developer_board_24px);
            }
        }
    }

    //  Get string shared preference
    private String getPreferenceString(String key) {
        return PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(key, "");
    }

    //  Get boolean shared preference
    private Boolean getPreferenceBoolean(String key) {
        return PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getBoolean(key, false);
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

