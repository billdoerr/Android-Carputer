package com.billdoerr.android.carputer.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.billdoerr.android.carputer.R;

import org.greenrobot.eventbus.EventBus;

import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

public class SettingsFragmentCameraDetail extends DialogFragment {

    private static final String TAG = "CameraDetail";

    private static final String ARGS_PREF_KEY = "ARGS_PREF_KEY";
    private static final String ARGS_CAMERA_DETAIL = "ARGS_CAMERA_DETAIL";
    private static final String ARGS_ADD = "ARGS_ADD";

    private Camera mCamera;
    private String mPrefKey;
    private int mIndex;
    private boolean mAdd = false;

    //  Widgets
    private View view;
    private EditText mTextCameraName;
    private EditText mTextCameraUrl;
    private EditText mTextUsername;
    private EditText mTextPassword;
    private Switch mSwitchUseAuthentication;

    private TextView mLblCameraNameRequired;
    private TextView mLblCameraUrlRequired;

    public SettingsFragmentCameraDetail() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        Bundle args = getArguments();
        mPrefKey = args.getString(ARGS_PREF_KEY);
        mAdd = args.getBoolean(ARGS_ADD);
        mCamera = (Camera) args.getSerializable(ARGS_CAMERA_DETAIL);
        mIndex = getIndex(mPrefKey);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings_camera_detail, container);

        mLblCameraNameRequired = (TextView) view.findViewById(R.id.lbl_camera_name_required_field);

        mTextCameraName = (EditText) view.findViewById(R.id.txt_camera_name);
        mTextCameraName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mTextCameraName.getText().toString().length() == 0) {
                        mLblCameraNameRequired.setVisibility(View.VISIBLE);
                    } else {
                        mLblCameraNameRequired.setVisibility(View.GONE);
                    }
                }
            }
        });

       mLblCameraUrlRequired = (TextView) view.findViewById(R.id.lbl_camera_url_required_field);

        mTextCameraUrl = (EditText) view.findViewById(R.id.txt_camera_url);
        mTextCameraUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mTextCameraUrl.getText().toString().length() == 0) {
                        mLblCameraUrlRequired.setVisibility(View.VISIBLE);
                    } else {
                        mLblCameraUrlRequired.setVisibility(View.GONE);
                    }
                }
            }
        });

        mTextUsername = (EditText) view.findViewById(R.id.txt_username);
        mTextUsername.setEnabled(false);  //  Disable by default

        mTextPassword = (EditText) view.findViewById(R.id.txt_password);
        mTextPassword.setEnabled(false);  //  Disable by default

        //  TUsername/password currently not support
        mSwitchUseAuthentication = (Switch) view.findViewById(R.id.switch_use_authentication);
        mSwitchUseAuthentication.setEnabled(false);
        mSwitchUseAuthentication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //  Enable username/password
                    mTextUsername.setEnabled(true);
                    mTextPassword.setEnabled(true);
                } else {
                    //  Disable username/password
                    mTextUsername.setEnabled(false);
                    mTextPassword.setEnabled(false);
                }
            }
        });

        //  Save transaction
        Button btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isMissingData = false;

                hideKeyboard();

                if (mTextCameraName.getText().toString().length() == 0) {
                    isMissingData = true;
                    mLblCameraNameRequired.setVisibility(View.VISIBLE);
                } else {
                    mLblCameraNameRequired.setVisibility(View.GONE);
                }

                if (mTextCameraUrl.getText().toString().length() == 0) {
                    isMissingData = true;
                    mLblCameraUrlRequired.setVisibility(View.VISIBLE);
                } else {
                    mLblCameraUrlRequired.setVisibility(View.GONE);
                }

                if (!isMissingData) {
                    mCamera = getCameraDetail();
                    if (mAdd) {
                        sendMessage(SettingsMessageEvent.Action.ADD, SettingsMessageEvent.Device.CAMERA, mCamera, -1);
                    } else {
                        sendMessage(SettingsMessageEvent.Action.UPDATE, SettingsMessageEvent.Device.CAMERA, mCamera, mIndex);
                    }
                    getDialog().dismiss();      //  Goodbye
                }

            }
        });

        //  Cancel transaction
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();      //  Goodbye
            }
        });

        // Delete
        Button btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(SettingsMessageEvent.Action.DELETE, SettingsMessageEvent.Device.CAMERA, mCamera, mIndex);
                getDialog().dismiss();      //  Goodbye
            }
        });


        //  Assign values from passed in arguments
        setCameraDetail(mCamera);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        //  Display dialog as full screen
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //  Set text fields with values from arguments
    private void setCameraDetail(Camera camera) {
        if (camera != null) {
            mTextCameraName.setText(camera.getName());
            mTextCameraUrl.setText(camera.getUrl());
        }
    }

    //  Get update details
    private Camera getCameraDetail() {
        Camera camera = new Camera();
        camera.setName(mTextCameraName.getText().toString());
        camera.setUrl(mTextCameraUrl.getText().toString());
        return camera;
    }

    //  Hide soft keyboard
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //  Get array index that is stored in SharedPreference.
    private int getIndex(String prefKey) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int index = appSharedPrefs.getInt(prefKey, -1);
        return index;
    }

    private void sendMessage(int action, int device, Camera camera, int index) {
        //  Post preference change to EventBus
        SettingsMessageEvent event = new SettingsMessageEvent();
        event.sendMessage(action, device, camera, index);
        EventBus.getDefault().post(event);
    }


}
