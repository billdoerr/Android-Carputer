package com.billdoerr.android.carputer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CarputerFragment extends Fragment {

    private static final String TAG = "CarputerFragment";
    private static final String PREF_CAMERA_MOTIONEYE_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_CAMERA_MOTIONEYE_URL";

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

        //  Default view
        startActivity(new Intent(getActivity(), CameraActivityMjpeg.class));

        String s = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(PREF_CAMERA_MOTIONEYE_URL, null);
        if (s != null) {
            Uri uri = Uri.parse(s);

            Intent i = CameraActivityMotionEye.newIntent(getActivity(), uri);
            startActivity(i);
        }

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
