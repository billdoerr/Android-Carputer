package com.billdoerr.android.carputer.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.billdoerr.android.carputer.R;
import com.billdoerr.android.carputer.activities.CarputerActivityMgmt;

/**
 * Carputer fragment created by the CarputerActivity class.
 */
public class CarputerFragment extends Fragment {

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

        startActivity(new Intent(getActivity(), CarputerActivityMgmt.class));

        return view;
    }

}
