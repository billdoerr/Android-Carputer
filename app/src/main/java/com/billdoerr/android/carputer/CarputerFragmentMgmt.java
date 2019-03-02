package com.billdoerr.android.carputer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.billdoerr.android.carputer.settings.Node;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    private static final String ARGS_NODE_DETAIL = "ARGS_NODE_DETAIL";

    private static List<Node> mNodes = new ArrayList<Node>();

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static CarputerFragmentMgmt newInstance() {
        return new CarputerFragmentMgmt();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Get devices
        mNodes = getNodesFromSharedPrefs(getActivity());
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

        return view;
    }

    //  Add fragments to tabs
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        //  Fragment:  Node SSH
        SSHFragment sshFragment = new SSHFragment();
        adapter.addFragment(sshFragment, getResources().getString(R.string.tab_carputer_mgmt_ssh));

        //  Fragment:  Node phpSysInfo
        for (int i = 0; i < mNodes.size(); i++) {
            if (mNodes.get(i).isUsePhpSysInfo()) {
                Bundle args = new Bundle();
                args.putSerializable(ARGS_NODE_DETAIL, mNodes.get(i));
                CarputerFragmentMgmtPhySysInfoView carputerFragmentMgmtPhySysInfoView = new CarputerFragmentMgmtPhySysInfoView();
                carputerFragmentMgmtPhySysInfoView.setArguments(args);
                adapter.addFragment(carputerFragmentMgmtPhySysInfoView, getString(R.string.tab_carputer_mgmt_phpsysinfo) + ": " + mNodes.get(i).getName());
            }
        }

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

    //  Display tab icons
    private void addTabLayoutIcons() {
        for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
            String sentence = mTabLayout.getTabAt(i).getText().toString();
            mTabLayout.getTabAt(i).setIcon(R.drawable.ic_camera);
            String s = mTabLayout.getTabAt(i).getText().toString();
            //  SSH
            if (s.equals(getResources().getString(R.string.tab_carputer_mgmt_ssh).toString())) {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_ssh_24px);
            //  phpSysInfo
            } else if ( sentence.toLowerCase().indexOf(getString(R.string.tab_carputer_mgmt_phpsysinfo).toString().toLowerCase() ) != -1 )  {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_phpsysinfo_24px);
            //  Default
            } else {
                mTabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_developer_board_24px);
            }
        }
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

    //  Retrieve list of node's that are stored in SharedPreferences as a JSON string
    private static List<Node> getNodesFromSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(Node.PrefKey.PREF_KEY_NODES, "");
        List<Node> nodes = gson.fromJson(json, new TypeToken<ArrayList<Node>>(){}.getType());
        if (nodes == null) {
            nodes = new ArrayList<Node>();
        }
        return nodes;
    }

}

