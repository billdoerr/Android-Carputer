package com.billdoerr.android.carputer.settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.billdoerr.android.carputer.GlobalVariables;
import com.billdoerr.android.carputer.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

/**
 * Main preference settings activity.
 */
public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String TAG = "SettingsActivity";

    private static final String ARGS_CAMERA_DETAIL = "ARGS_CAMERA_DETAIL";
    private static final String ARGS_NODE_DETAIL = "ARGS_NODE_DETAIL";
    private static final String ARGS_ADD = "ARGS_ADD";
    private static final String ARGS_INDEX = "ARGS_INDEX";

    // Calling Application class (see application tag in AndroidManifest.xml)
    private static GlobalVariables mGlobalVariables;

    //  Device objects
    private static List<Camera> mCameras = new ArrayList<Camera>();
    private static List<Node> mNodes = new ArrayList<Node>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);

        // Calling Application class (see application tag in AndroidManifest.xml)
        mGlobalVariables = (GlobalVariables) getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getString(R.string.pref_title_activity_settings));
        actionbar.setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new SettingsFragment())
                .commit();

    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate (
                getClassLoader(),
                pref.getFragment(),
                args);
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * The root preference fragment that displays preferences that link to the other preference
     * fragments below.
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {

        private static final String TAG = "SettingsFragment";

        public SettingsFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onResume() {
            super.onResume();

            //  Update preference summary
            updateCameraPreferenceSummary(getActivity());
            updateNodePreferenceSummary(getActivity());
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_root, rootKey);

            //  Retrieve list of camera's that are stored in SharedPreferences as a JSON string
            mCameras = mGlobalVariables.getCameras();

            //  Retrieve list of node's that are stored in SharedPreferences as a JSON string
            mNodes = mGlobalVariables.getNodes();

            //  Update preference summary
            updateCameraPreferenceSummary(getActivity());
            updateNodePreferenceSummary(getActivity());

        }

        //  Update Camera's preference summary
        private void updateCameraPreferenceSummary(Context context) {
            int size = 0;
            if (mCameras != null) {
                size = mCameras.size();
            }

            Preference pref = findPreference(Camera.PrefKey.PREF_KEY_CAMERAS);
            pref.setSummary(size + " camera\'s configured.");

        }

        //  Update Node's preference summary
        private void updateNodePreferenceSummary(Context context) {
            int size = 0;
            if (mNodes != null) {
                size = mNodes.size();
            }

            Preference pref = findPreference(Node.PrefKey.PREF_KEY_NODES);
            pref.setSummary(size + " node\'s configured.");
        }

    }

    /**
     * The fragment that displays list of configured camera's.
     */
    public static class SettingsFragmentCameras extends PreferenceFragmentCompat {

        private static final String TAG = "SettingsFragmentCameras";

        private List<String> mPrefKeyList = new ArrayList<>();

        public SettingsFragmentCameras() {
            // Required empty public constructor
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            EventBus.getDefault().register(this);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onResume() {
            super.onResume();
//            saveCamerasToSharedPrefs(getActivity());
        }

        @Override
        public void onDetach() {
            //  Remove subscription to event bus
            EventBus.getDefault().unregister(this);
            //  Goodbye
            super.onDetach();
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            //  Dynamically create PreferenceScreen of configured camera's
            createPreferences();
        }

        // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMessageEvent(SettingsMessageEvent event) {
            switch (event.getAction()) {
                case SettingsMessageEvent.Action.ADD: {
                    mCameras.add(event.getCamera());
                    //  Recreate preference screen.  Not optimal.  Complete HACK!
                    createPreferences();
                    break;
                }
                case SettingsMessageEvent.Action.UPDATE: {
                    mCameras.set(event.getIndex(), event.getCamera());
                    //  Recreate preference screen.  Not optimal.  Complete HACK!
                    createPreferences();
                    break;
                }
                case SettingsMessageEvent.Action.DELETE: {
                    mCameras.remove(event.getIndex());
                    //  Recreate preference screen.  Not optimal.  Complete HACK!
                    createPreferences();
                    break;
                }
                default:
                    //  Nothing to do here.  Move along.
                    break;
            }
            //  Update shared preferences with devices
            mGlobalVariables.setCameras(mCameras);
        }

        /**
         * Dynamically create PreferenceScreen of configured camera's.
         */
        private void createPreferences() {

            Context context = getPreferenceManager().getContext();
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

            PreferenceCategory camerasConfiguredCategory = new PreferenceCategory(context);
            camerasConfiguredCategory.setTitle(getResources().getString(R.string.pref_category_cameras_configured));
            screen.addPreference(camerasConfiguredCategory);

            //  Dynamically create preferences
            for (int i = 0; i < mCameras.size(); i++) {
                Log.i(TAG, "Dynamically create preferences: " + i);

                Preference prefCamera = new Preference(context);
                prefCamera.setTitle(mCameras.get(i).getName());
                prefCamera.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_baseline_camera_24px));
                prefCamera.setDefaultValue(Integer.toString(i));
                prefCamera.setSummary(mCameras.get(i).getUrl());

                //  Add list index to preference extras
                Bundle extras = prefCamera.getExtras();
                extras.putInt(ARGS_INDEX, i);

                //  Add preferences to PreferenceCategory
                camerasConfiguredCategory.addPreference(prefCamera);

                //  Launch fragment when preference is clicked.
                prefCamera.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        //  Get list index from preference extras
                        Bundle extras = prefCamera.getExtras();
                        int index = extras.getInt(ARGS_INDEX);
                        Camera camera = mCameras.get(index);

                        //  Pass args to fragment
                        Bundle args = new Bundle();
                        args.putInt(ARGS_INDEX, index);
                        args.putBoolean(ARGS_ADD, false);
                        args.putSerializable(ARGS_CAMERA_DETAIL, camera);

                        Fragment settingsFragmentCameraDetail = new SettingsFragmentCameraDetail();
                        settingsFragmentCameraDetail.setArguments(args);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, settingsFragmentCameraDetail)
                                .addToBackStack(null)
                                .commit();

                        return true;
                    }
                });

            }

            //  Create AddCamera preference
            PreferenceCategory addCamerasCategory = new PreferenceCategory(context);
            addCamerasCategory.setTitle(getResources().getString(R.string.pref_category_camera_add));
            screen.addPreference(addCamerasCategory);

            Preference addCamera = new Preference(context);
            addCamera.setTitle(getResources().getString(R.string.pref_title_camera_add));
            addCamera.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_baseline_add_24px));
            addCamerasCategory.addPreference(addCamera);

            //  Launch fragment when preference is clicked.
            addCamera.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    //  Assign default values
                    Camera camera = new Camera();
                    camera.setName(getString(R.string.pref_default_camera_name));
                    camera.setUrl(getString(R.string.pref_default_camera_url));

                    Bundle args = new Bundle();
                    args.putBoolean(ARGS_ADD, true);    //  Add device flag
                    args.putSerializable(ARGS_CAMERA_DETAIL, camera);

                    Fragment settingsFragmentCameraDetail = new SettingsFragmentCameraDetail();
                    settingsFragmentCameraDetail.setArguments(args);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, settingsFragmentCameraDetail)
                            .addToBackStack(null)
                            .commit();

                    return true;
                }
            });

            //  Set the root of the preference hierarchy that this fragment is showing.
            setPreferenceScreen(screen);
        }

    }

    /**
     * The fragment that displays list of configured Node's.
     */
    public static class SettingsFragmentNodes extends PreferenceFragmentCompat {

        private static final String TAG = "SettingsFragmentNodes";

        private List<String> mPrefKeyList = new ArrayList<>();

        public SettingsFragmentNodes() {
            // Required empty public constructor
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            EventBus.getDefault().register(this);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onDetach() {
            //  Remove subscription to event bus
            EventBus.getDefault().unregister(this);
            //  Goodbye
            super.onDetach();
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            //  Dynamically create PreferenceScreen of configured Node's
            createPreferences();
        }

        /**
         * This method will be called when a MessageEvent is posted in the UI thread
         * @param event SettingsMessageEvent object.
         */
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMessageEvent(SettingsMessageEvent event) {
            switch (event.getAction()) {
                case SettingsMessageEvent.Action.ADD: {
                    mNodes.add(event.getNode());
                    //  Recreate preference screen.  Not optimal.  Complete HACK!
                    createPreferences();
                    break;
                }
                case SettingsMessageEvent.Action.UPDATE: {
                    mNodes.set(event.getIndex(), event.getNode());
                    //  Recreate preference screen.  Not optimal.  Complete HACK!
                    createPreferences();
                    break;
                }
                case SettingsMessageEvent.Action.DELETE: {
                    mNodes.remove(event.getIndex());
                    //  Recreate preference screen.  Not optimal.  Complete HACK!
                    createPreferences();
                    break;
                }
                default:
                    //  Nothing to do here.  Move along.
                    break;
            }
            //  Update shared preferences with devices
            mGlobalVariables.setNodes(mNodes);
        }

        /**
         * Dynamically create PreferenceScreen of configured Node's.
         */
        private void createPreferences() {

            Context context = getPreferenceManager().getContext();
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

            PreferenceCategory nodessConfiguredCategory = new PreferenceCategory(context);
            nodessConfiguredCategory.setTitle(getResources().getString(R.string.pref_category_nodes_configured));
            screen.addPreference(nodessConfiguredCategory);

            //  Dynamically create preferences
            for (int i = 0; i < mNodes.size(); i++) {
                Log.i(TAG, "Dynamically create preferences: " + i);

                Preference prefNode = new Preference(context);
                prefNode.setTitle(mNodes.get(i).getName());
                prefNode.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_raspberry_pi_24px));
                prefNode.setDefaultValue(Integer.toString(i));
                prefNode.setSummary(mNodes.get(i).getIp());

                //  Add list index to preference extras
                Bundle extras = prefNode.getExtras();
                extras.putInt(ARGS_INDEX, i);

                //  Add preferences to PreferenceCategory
                nodessConfiguredCategory.addPreference(prefNode);

                //  Launch fragment when preference is clicked.
                prefNode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        //  Get list index from preference extras
                        Bundle extras = prefNode.getExtras();
                        int index = extras.getInt(ARGS_INDEX);
                        Node node = mNodes.get(index);

                        //  Pass args to fragment
                        Bundle args = new Bundle();
                        args.putInt(ARGS_INDEX, index);
                        args.putBoolean(ARGS_ADD, false);
                        args.putSerializable(ARGS_NODE_DETAIL, node);

                        Fragment settingsFragmentNodeDetail = new SettingsFragmentNodeDetail();
                        settingsFragmentNodeDetail.setArguments(args);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, settingsFragmentNodeDetail)
                                .addToBackStack(null)
                                .commit();

                        return true;
                    }
                });

            }

            //  Create addNode preference
            PreferenceCategory addNodesCategory = new PreferenceCategory(context);
            addNodesCategory.setTitle(getResources().getString(R.string.pref_category_nodes_add));
            screen.addPreference(addNodesCategory);

            Preference addNode = new Preference(context);
            addNode.setTitle(getResources().getString(R.string.pref_title_node_add));
            addNode.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_baseline_add_24px));
            addNodesCategory.addPreference(addNode);

            //  Launch fragment when preference is clicked.
            addNode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    //  Assign default values
                    Node node = new Node();
                    node.setName(getString(R.string.pref_default_node_name));
                    node.setIp(getString(R.string.pref_default_node_ip));
                    node.setSSHPort(getString(R.string.pref_default_node_ssh_port));

                    node.setUseAuthentication(true);
                    node.setUser("pi");
                    node.setPassword("");

                    node.setUsePhpSysInfo(false);
                    node.setPhpSysInfoUrl(getString(R.string.pref_default_node_physysinfo_url));

                    node.setUseMotionEye(true);
                    node.setMotionEyeUrl(getString(R.string.pref_default_node_motioneye_url));

                    Bundle args = new Bundle();
                    args.putBoolean(ARGS_ADD, true);    //  Add device flag
                    args.putSerializable(ARGS_NODE_DETAIL, node);

                    Fragment settingsFragmentNodeDetail = new SettingsFragmentNodeDetail();
                    settingsFragmentNodeDetail.setArguments(args);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, settingsFragmentNodeDetail)
                            .addToBackStack(null)
                            .commit();

                    return true;
                }
            });

            //  Set the root of the preference hierarchy that this fragment is showing.
            setPreferenceScreen(screen);
        }

    }

}
