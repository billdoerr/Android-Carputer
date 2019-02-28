package com.billdoerr.android.carputer.settings;

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

/**
 * The fragment that displays preferences that Node detail
 */
public class SettingsFragmentNodeDetail extends DialogFragment {

    private static final String TAG = "NodeDetail";

    private static final String ARGS_PREF_KEY = "ARGS_PREF_KEY";
    private static final String ARGS_NODE_DETAIL = "ARGS_NODE_DETAIL";
    private static final String ARGS_ADD = "ARGS_ADD";

    private Node mNode;
    private String mPrefKey;
    private int mIndex;
    private boolean mAdd = false;

    //  Widgets
    private View view;
    private EditText mTextNodeName;
    private EditText mTextNodeIp;
    private EditText mTextSSHPort;
    private EditText mTextUsername;
    private EditText mTextPassword;
    private EditText mTextPhpSysInfoUrl;
    private EditText mTextMotionEyeUrl;

    private boolean mUseAuthentication = false;
    private boolean mUsePhpSysInfo = false;
    private boolean mUseMotionEye = false;

    private Switch mSwitchUseAuthentication;
    private Switch mSwitchUsePhpSysInfo;
    private Switch mSwitchUseMotionEye;

    private TextView mLblNodeNameRequired;
    private TextView mLblNodeIpRequired;

    public SettingsFragmentNodeDetail()  {
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
        mNode = (Node) args.getSerializable(ARGS_NODE_DETAIL);
        mIndex = getIndex(mPrefKey);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings_node_detail, container);

        mLblNodeNameRequired = (TextView) view.findViewById(R.id.lbl_node_name_required_field);

        mTextNodeName = (EditText) view.findViewById(R.id.txt_node_name);
        mTextNodeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mTextNodeName.getText().toString().length() == 0) {
                        mLblNodeNameRequired.setVisibility(View.VISIBLE);
                    } else {
                        mLblNodeNameRequired.setVisibility(View.GONE);
                    }
                }
            }
        });

        mLblNodeIpRequired = (TextView) view.findViewById(R.id.lbl_node_ip_required_field);

        mTextNodeIp = (EditText) view.findViewById(R.id.txt_node_ip);
        mTextNodeIp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mTextNodeIp.getText().toString().length() == 0) {
                        mLblNodeIpRequired.setVisibility(View.VISIBLE);
                    } else {
                        mLblNodeIpRequired.setVisibility(View.GONE);
                    }
                }
            }
        });

        mTextSSHPort = (EditText) view.findViewById(R.id.txt_node_ssh_port);

        mTextUsername = (EditText) view.findViewById(R.id.txt_username);
        mTextUsername.setEnabled(false);  //  Disable by default

        mTextPassword = (EditText) view.findViewById(R.id.txt_password);
        mTextPassword.setEnabled(false);  //  Disable by default

        //  Use authentication
        mSwitchUseAuthentication = (Switch) view.findViewById(R.id.switch_use_authentication);
        mSwitchUseAuthentication.setEnabled(true);
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

        mTextPhpSysInfoUrl = (EditText) view.findViewById(R.id.txt_node_phpsysinfo_url);
        mTextPhpSysInfoUrl.setEnabled(false);

        //  Use phpSysInfo
        mSwitchUsePhpSysInfo = (Switch) view.findViewById(R.id.switch_node_use_phpsysinfo);
        mSwitchUsePhpSysInfo.setEnabled(true);
        mSwitchUsePhpSysInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //  Enable username/password
                    mTextPhpSysInfoUrl.setEnabled(true);
                } else {
                    //  Disable username/password
                    mTextPhpSysInfoUrl.setEnabled(false);
                }
            }
        });

        mTextMotionEyeUrl = (EditText) view.findViewById(R.id.txt_node_motioneye_url);
        mTextMotionEyeUrl.setEnabled(false);

        //  Use motionEye
        mSwitchUseMotionEye = (Switch) view.findViewById(R.id.switch_node_use_motioneye);
        mSwitchUseMotionEye.setEnabled(true);
        mSwitchUseMotionEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //  Enable username/password
                    mTextMotionEyeUrl.setEnabled(true);
                } else {
                    //  Disable username/password
                    mTextMotionEyeUrl.setEnabled(false);
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

                if (mTextNodeName.getText().toString().length() == 0) {
                    isMissingData = true;
                    mLblNodeIpRequired.setVisibility(View.VISIBLE);
                } else {
                    mLblNodeIpRequired.setVisibility(View.GONE);
                }

                if (mTextNodeIp.getText().toString().length() == 0) {
                    isMissingData = true;
                    mLblNodeIpRequired.setVisibility(View.VISIBLE);
                } else {
                    mLblNodeIpRequired.setVisibility(View.GONE);
                }

                if (!isMissingData) {
                    mNode = getNodeDetail();
                    if (mAdd) {
                        sendMessage(SettingsMessageEvent.Action.ADD, SettingsMessageEvent.Device.CAMERA, mNode, -1);
                    } else {
                        sendMessage(SettingsMessageEvent.Action.UPDATE, SettingsMessageEvent.Device.CAMERA, mNode, mIndex);
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
                sendMessage(SettingsMessageEvent.Action.DELETE, SettingsMessageEvent.Device.NODE, mNode, mIndex);
                getDialog().dismiss();      //  Goodbye
            }
        });

        //  Assign values from passed in arguments
        setNodeDetail(mNode);

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
    private void setNodeDetail(Node node) {
        if (node != null) {
            mTextNodeName.setText(node.getName());
            mTextNodeIp.setText(node.getIp());
            mTextSSHPort.setText(node.getSSHPort());
            mSwitchUseAuthentication.setChecked(node.isUseAuthentication());
            mTextUsername.setText(node.getUser());
            mTextPassword.setText(node.getPassword());
            mSwitchUsePhpSysInfo.setChecked(node.isUsePhpSysInfo());
            mTextPhpSysInfoUrl.setText(node.getPhpSysInfoUrl());
            mSwitchUseMotionEye.setChecked(node.isUseMotionEye());
            mTextMotionEyeUrl.setText(node.getMotionEyeUrl());
        }
    }

    //  Get update details
    private Node getNodeDetail() {
        Node node = new Node();
        node.setName(mTextNodeName.getText().toString());
        node.setIp(mTextNodeIp.getText().toString());
        node.setSSHPort(mTextSSHPort.getText().toString());

        node.setUseAuthentication(mSwitchUseAuthentication.isChecked());
        node.setUser(mTextUsername.getText().toString());
        node.setPassword(mTextPassword.getText().toString());

        node.setUsePhpSysInfo(mSwitchUsePhpSysInfo.isChecked());
        node.setPhpSysInfoUrl(mTextPhpSysInfoUrl.getText().toString());

        node.setUseMotionEye(mSwitchUseMotionEye.isChecked());
        node.setMotionEyeUrl(mTextMotionEyeUrl.getText().toString());

        return node;
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

    private void sendMessage(int action, int device, Node node, int index) {
        //  Post preference change to EventBus
        SettingsMessageEvent event = new SettingsMessageEvent();
        event.sendMessage(action, device, node, index);
        EventBus.getDefault().post(event);
    }


}

