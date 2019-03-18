package com.billdoerr.android.carputer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.billdoerr.android.carputer.settings.Camera;
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

/**
 * Fragment that will host child fragments in a tab layout.
 * The child fragments display the streaming Mjpeg video.
 * Created by the CameraActivityMjpeg class.
 */
public class CameraFragmentMjpeg extends Fragment {

    private static final String TAG = "CameraFragmentMjeg";

    private static final String ARGS_CAMERA_DETAIL = "ARGS_CAMERA_DETAIL";

    private static List<Camera> mCameras = new ArrayList<Camera>();

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static CameraFragmentMjpeg newInstance() {
        return new CameraFragmentMjpeg();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calling Application class (see application tag in AndroidManifest.xml)
        final GlobalVariables mGlobalVariables = (GlobalVariables) getActivity().getApplicationContext();

        //  Get list of devices
        mCameras = mGlobalVariables.getCameras();
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

    /**
     * Add fragments to tabs.
     * @param viewPager ViewPager:  Adapter will be assigned the child fragments.
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        for (int i = 0; i < mCameras.size(); i++) {
            Bundle args = new Bundle();
            args.putSerializable(ARGS_CAMERA_DETAIL, mCameras.get(i));

            CameraFragmentMjpegSnapshot cameraFragmentMjpegSnapshot = new CameraFragmentMjpegSnapshot();
            cameraFragmentMjpegSnapshot.setArguments(args);
            adapter.addFragment(cameraFragmentMjpegSnapshot, mCameras.get(i).getName());
        }

        //  Set adapter to view pager
        viewPager.setAdapter(adapter);
    }

    /**
     * Setup action bar.
     * @param view View:  Container for the toolbar.
     */
    private void setupActionBar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((CameraActivityMjpeg)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((CameraActivityMjpeg)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
    }

    /**
     * Add icons to tab.
     */
    private void addTabLayoutIcons() {
        for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_camera_24px);
        }
    }

    /**
     * View Adapter Class.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
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
