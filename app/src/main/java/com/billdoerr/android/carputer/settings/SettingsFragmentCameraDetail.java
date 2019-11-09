package com.billdoerr.android.carputer.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.billdoerr.android.carputer.R;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * Fragment used to add/edit camera details.
 */
public class SettingsFragmentCameraDetail extends Fragment {

    private static final String ARGS_INDEX = "ARGS_INDEX";
    private static final String ARGS_CAMERA_DETAIL = "ARGS_CAMERA_DETAIL";
    private static final String ARGS_ADD = "ARGS_ADD";

    private Camera mCamera;
    private int mIndex;
    private boolean mAdd = false;

    //  Widgets
    private EditText mTextCameraName;
    private EditText mTextCameraUrl;
    private EditText mTextUsername;
    private EditText mTextPassword;

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
        mAdd = Objects.requireNonNull(args).getBoolean(ARGS_ADD);
        mCamera = (Camera) args.getSerializable(ARGS_CAMERA_DETAIL);
        mIndex = args.getInt(ARGS_INDEX);

        //  Show menu only if not adding device
        if(!mAdd) {
            setHasOptionsMenu(true);
        }

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_camera_detail, null);

        mLblCameraNameRequired = view.findViewById(R.id.lbl_camera_name_required_field);

        mTextCameraName = view.findViewById(R.id.txt_camera_name);
        mTextCameraName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (mTextCameraName.getText().toString().length() == 0) {
                    mLblCameraNameRequired.setVisibility(View.VISIBLE);
                } else {
                    mLblCameraNameRequired.setVisibility(View.GONE);
                }
            }
        });

       mLblCameraUrlRequired = view.findViewById(R.id.lbl_camera_url_required_field);

        mTextCameraUrl = view.findViewById(R.id.txt_camera_url);
        mTextCameraUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (mTextCameraUrl.getText().toString().length() == 0) {
                    mLblCameraUrlRequired.setVisibility(View.VISIBLE);
                } else {
                    mLblCameraUrlRequired.setVisibility(View.GONE);
                }
            }
        });

        mTextUsername = view.findViewById(R.id.txt_username);
        mTextUsername.setEnabled(false);  //  Disable by default

        mTextPassword = view.findViewById(R.id.txt_password);
        mTextPassword.setEnabled(false);  //  Disable by default

        //  TUsername/password currently not support
        Switch switchUseAuthentication = view.findViewById(R.id.switch_use_authentication);
        switchUseAuthentication.setEnabled(false);
        switchUseAuthentication.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //  Enable username/password
                mTextUsername.setEnabled(true);
                mTextPassword.setEnabled(true);
            } else {
                //  Disable username/password
                mTextUsername.setEnabled(false);
                mTextPassword.setEnabled(false);
            }
        });

        //  Save transaction
        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            boolean isMissingData = false;

            //  Not sure if this is needed or issue with emulator.  No issues on real device if not called.
            hideSoftKeyboard();

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
                    sendMessage(SettingsMessageEvent.Action.ADD, mCamera, -1);
                } else {
                    sendMessage(SettingsMessageEvent.Action.UPDATE, mCamera, mIndex);
                }
                Objects.requireNonNull(getActivity()).onBackPressed();
            }

        });

        //  Cancel transaction
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        //  Assign values from passed in arguments
        setCameraDetail(mCamera);

        return view;
    }

    /**
     * Set text fields with values from arguments.
     * @param camera Camera:  Object of type Camera.
     */
    private void setCameraDetail(Camera camera) {
        if (camera != null) {
            mTextCameraName.setText(camera.getName());
            mTextCameraUrl.setText(camera.getUrl());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //  Delete device
        if (item.getItemId() == R.id.action_delete) {
            sendMessage(SettingsMessageEvent.Action.DELETE, mCamera, mIndex);
            Objects.requireNonNull(getActivity()).onBackPressed();
            return true;
        }

        return false;
    }

    /**
     * Get update details.
     * @return Camera:  Object of type Camera.
     */
    private Camera getCameraDetail() {
        Camera camera = new Camera();
        camera.setName(mTextCameraName.getText().toString());
        camera.setUrl(mTextCameraUrl.getText().toString());
        return camera;
    }

    /**
     * Hide soft keyboard.
     */
    private void hideSoftKeyboard() {
        // Check if no view has focus:
        View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Post preference change to EventBus.
     * @param action int: Value for action to be taken.
     * @param camera Camera:  Object of type Camera.
     * @param index int:  Index of list of devices.
     */
    private void sendMessage(int action, Camera camera, int index) {
        SettingsMessageEvent event = new SettingsMessageEvent();
        event.sendMessage(action, camera, index);
        EventBus.getDefault().post(event);
    }

}
