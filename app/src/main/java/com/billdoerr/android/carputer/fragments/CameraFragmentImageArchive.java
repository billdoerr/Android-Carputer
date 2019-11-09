package com.billdoerr.android.carputer.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.billdoerr.android.carputer.utils.GlobalVariables;
import com.billdoerr.android.carputer.R;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  Fragment which contains a tab layout will host a child fragment to displays list of saved images (snapshots).
 */
public class CameraFragmentImageArchive extends Fragment {

    private static final String ARGS_IMAGE_ARCHIVE_URL = "ARGS_IMAGE_ARCHIVE_URL";

    private String mImageArchiveUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calling Application class (see application tag in AndroidManifest.xml)
        final GlobalVariables mGlobalVariables = (GlobalVariables) Objects.requireNonNull(getActivity()).getApplicationContext();

        mImageArchiveUrl = mGlobalVariables.getImageArchiveUrl();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_view_pager, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //  Add icons
        addTabLayoutIcons(viewPager, tabLayout);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Change the toolbar title text
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.activity_title_camera_image_archive);

    }

    /**
     * Add fragments to tabs.
     * @param viewPager ViewPager: Layout manager adapter will be assigned.
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(Objects.requireNonNull(getChildFragmentManager()));

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
     * Add icons to tabs.
     */
    private void addTabLayoutIcons(ViewPager viewPager, TabLayout tabLayout) {
        for (int i = 0; i < Objects.requireNonNull(viewPager.getAdapter()).getCount(); i++) {
            Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(R.drawable.ic_baseline_visibility_24px);
        }
    }

    /**
     * View Adapter Class.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
