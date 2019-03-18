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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.billdoerr.android.carputer.R;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

/**
 * Fragment used to add/edit node details.
 */
public class SettingsFragmentNodeDetail extends Fragment {

    private static final String TAG = "NodeDetail";

    private static final String ARGS_INDEX = "ARGS_INDEX";
    private static final String ARGS_NODE_DETAIL = "ARGS_NODE_DETAIL";
    private static final String ARGS_ADD = "ARGS_ADD";

    private Node mNode;
    private int mIndex;
    private boolean mAdd = false;

    //  Widgets
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
        mAdd = args.getBoolean(ARGS_ADD);
        mNode = (Node) args.getSerializable(ARGS_NODE_DETAIL);
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

//        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(),R.style.AppTheme_PreferenceSettingsOverlay);


        View view = inflater.inflate(R.layout.fragment_settings_node_detail, null);

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

                //  Not sure if this is needed or issue with emulator.  No issues on real device if not called.
                hideSoftKeyboard();

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
                        sendMessage(SettingsMessageEvent.Action.ADD, mNode, -1);
                    } else {
                        sendMessage(SettingsMessageEvent.Action.UPDATE, mNode, mIndex);
                    }
                    getActivity().onBackPressed();      //  Goodbye
                }

            }
        });

        //  Cancel transaction
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();      //  Goodbye
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            //  Delete device
            case R.id.action_delete:
                sendMessage(SettingsMessageEvent.Action.DELETE, mNode, mIndex);
                getActivity().onBackPressed();      //  Goodbye
                return true;
            default:
                break;
        }

        return false;
    }

    /**
     * Set text fields with values from arguments.
     * @param node Node:  Object of type Node
     */
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

    /**
     * Get update details.
     * @return Node:  Object of type Node.
     */
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

    /**
     * Hide soft keyboard.
     */
    private void hideSoftKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Post preference change to EventBus.
     * @param action int: Value for action to be taken.
     * @param node Node:  Object of type Node.
     * @param index int:  Index of list of devices.
     */
    private void sendMessage(int action, Node node, int index) {
        SettingsMessageEvent event = new SettingsMessageEvent();
        event.sendMessage(action, node, index);
        EventBus.getDefault().post(event);
    }


}

