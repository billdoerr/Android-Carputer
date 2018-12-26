package com.billdoerr.android.carputer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CarputerFragment extends Fragment {

    private static final String TAG = "CarputerFragment";

    //  Motion Eye preferences
    public static final String PREF_CAMERA_MOTIONEYE_URL = "com.billdoerr.android.carputerpoc.settings.SettingsActivity.PREF_CAMERA_MOTIONEYE_URL";
    public static final String PREF_CAMERA_MOTIONEYE_AUTH_USERNAME = "com.billdoerr.android.carputerpoc.settings.SettingsActivity.PREF_CAMERA_MOTIONEYE_AUTH_USERNAME";
    public static final String PREF_CAMERA_MOTIONEYE_AUTH_PASSWORD = "com.billdoerr.android.carputerpoc.settings.SettingsActivity.PREF_CAMERA_MOTIONEYE_AUTH_PASSWORD";

    public static CarputerFragment newInstance() {
        return new CarputerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment, container, false);

        //  Launch the Camera Activity as the default view
        startActivity(new Intent(getActivity(), CameraActivity.class));

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
