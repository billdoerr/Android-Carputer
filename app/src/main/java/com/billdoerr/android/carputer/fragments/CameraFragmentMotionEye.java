package com.billdoerr.android.carputer.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.billdoerr.android.carputer.utils.GlobalVariables;
import com.billdoerr.android.carputer.R;
import com.billdoerr.android.carputer.settings.Node;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fragment that contains tab layout hosting motionEye child fragments.
 */
public class CameraFragmentMotionEye extends Fragment {

    private static final String ARGS_NODE_DETAIL = "ARGS_NODE_DETAIL";

    private static List<Node> mNodes = new ArrayList<>();

    public static CameraFragmentMotionEye newInstance() {
        return new CameraFragmentMotionEye();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // Calling Application class (see application tag in AndroidManifest.xml)
        final GlobalVariables mGlobalVariables = (GlobalVariables) Objects.requireNonNull(getActivity()).getApplicationContext();

        //  Get devices
        mNodes = mGlobalVariables.getNodes();
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
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.activity_title_camera_motioneye);

    }

    /**
     * Add fragments to tabs.
     * @param viewPager ViewPager: Adapter that fragments will be added.
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(Objects.requireNonNull(getChildFragmentManager()));

        //  MotionEye cameras
        for (int i = 0; i < mNodes.size(); i++) {
            Bundle args = new Bundle();
            args.putSerializable(ARGS_NODE_DETAIL, mNodes.get(i));

            CameraFragmentMotionEyeView cameraFragmentMotionEyeView = new CameraFragmentMotionEyeView();
            cameraFragmentMotionEyeView.setArguments(args);
            adapter.addFragment(cameraFragmentMotionEyeView, mNodes.get(i).getName());
        }

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
    class ViewPagerAdapter extends FragmentStatePagerAdapter {

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

