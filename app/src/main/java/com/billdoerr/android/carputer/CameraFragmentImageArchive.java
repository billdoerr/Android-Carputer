package com.billdoerr.android.carputer;

import android.os.Bundle;
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

/**
 *  Fragment which contains a tab layout will host a child fragment to displays list of saved images (snapshots).
 *  Created by the CameraActivityImageArchive class.
 */
public class CameraFragmentImageArchive extends Fragment {

    private static final String TAG = "CameraFragmentImageArchive";
    private static final String ARGS_IMAGE_ARCHIVE_URL = "ARGS_IMAGE_ARCHIVE_URL";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String mImageArchiveUrl;

    public static CameraFragmentImageArchive newInstance() {
        return new CameraFragmentImageArchive();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calling Application class (see application tag in AndroidManifest.xml)
        final GlobalVariables mGlobalVariables = (GlobalVariables) getActivity().getApplicationContext();

        mImageArchiveUrl = mGlobalVariables.getImageArchiveUrl();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        //  Setup action bar
        setupActionBar(view);

        mViewPager = view.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        //  Add icons
        addTablayoutIcons();

        return view;
    }

    /**
     * Add fragments to tabs.
     * @param viewPager ViewPager: Layout manager adapter will be assigned.
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        //  Image archive fragment
        adapter.addFragment(new CameraFragmentSnapshotViewer(), getResources().getString(R.string.tab_camera_file_explorer));

        //  Image archive fragment
        Bundle args = new Bundle();
        args.putString(ARGS_IMAGE_ARCHIVE_URL, mImageArchiveUrl);
        CameraFragmentImageArchiveWebViewer cameraFragmentImageArchiveWebViewer = new CameraFragmentImageArchiveWebViewer();
        cameraFragmentImageArchiveWebViewer.setArguments(args);
        adapter.addFragment(cameraFragmentImageArchiveWebViewer, getResources().getString(R.string.tab_camera_image_archive_web_view));

        //  Set adapter to view pager
        viewPager.setAdapter(adapter);
    }

    /**
     * Setup action bar.
     * @param view View: Container for toolbar.
     */
    private void setupActionBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((CameraActivityImageArchive)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((CameraActivityImageArchive)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
    }

    /**
     * Add icons to tabs.
     */
    private void addTablayoutIcons() {
        for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_photo_library_24px);
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

        /**
         * Add fragment to tab.
         * @param fragment Fragment: Fragment to be added to tab layout.
         * @param title String:  Fragment title.
         */
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
