package com.billdoerr.android.carputer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CarputerFragment extends Fragment {

    private static final String TAG = "CarputerFragment";

    private static final String PREF_RASPBERRYPI_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_ENABLED";
    private static final String PREF_RASPBERRYPI_IP = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_IP";
    private static final String PREF_RASPBERRYPI_SSH_PORT = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_SSH_PORT";
    private static final String PREF_RASPBERRYPI_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_USERNAME";
    private static final String PREF_RASPBERRYPI_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_PASSWORD";


    public static CarputerFragment newInstance() {
        return new CarputerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment, container, false);

        startActivity(new Intent(getActivity(), CameraActivityMotionEye.class));
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
