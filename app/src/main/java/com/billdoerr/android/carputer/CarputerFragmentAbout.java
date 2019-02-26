package com.billdoerr.android.carputer;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class CarputerFragmentAbout extends Fragment {

    private static final String TAG = "CarputerFragmentAbout";

    public CarputerFragmentAbout() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CarputerFragmentAbout newInstance() {
        return new CarputerFragmentAbout();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_carputer_about, container, false);

        String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
        String versionName = BuildConfig.VERSION_NAME;

        TextView txtVersion = (TextView) v.findViewById(R.id.txtVersion);
        txtVersion.setText(versionName + " (" + versionCode + ")");

        return v;
    }

}
