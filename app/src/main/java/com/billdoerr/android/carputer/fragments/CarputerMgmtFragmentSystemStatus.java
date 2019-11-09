package com.billdoerr.android.carputer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.billdoerr.android.carputer.R;

//  TODO:  UNDER CONSTRUCTION - SystemStatus

/**
 * https://guides.codepath.com/android/using-dialogfragment
 */
public class CarputerMgmtFragmentSystemStatus extends DialogFragment {

    public CarputerMgmtFragmentSystemStatus() {
        //  Required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_computer_mgmt_system_status, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        // Get field from view
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
//        // Show soft keyboard automatically and request focus to field
//        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
