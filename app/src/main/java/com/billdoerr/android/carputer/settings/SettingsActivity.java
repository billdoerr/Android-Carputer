package com.billdoerr.android.carputer.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.billdoerr.android.carputer.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String TAG = "SettingsActivity";

    private static final String ARGS_PREF_KEY = "ARGS_PREF_KEY";
    private static final String ARGS_CAMERA_DETAIL = "ARGS_CAMERA_DETAIL";
    private static final String ARGS_NODE_DETAIL = "ARGS_NODE_DETAIL";
    private static final String ARGS_ADD = "ARGS_ADD";

    private static List<Camera> mCameras = new ArrayList<Camera>();
    private static List<Node> mNodes = new ArrayList<Node>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    //  Retrieve list of camera's that are stored in SharedPreferences as a JSON string
    private static List<Camera> getCamerasFromSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(Camera.PrefKey.PREF_KEY_CAMERAS, "");
        mCameras = gson.fromJson(json, new TypeToken<ArrayList<Camera>>(){}.getType());
        if (mCameras == null) {
            mCameras = new ArrayList<Camera>();
        }
        return mCameras;
    }

    //  Save list of camera's that are stored in SharedPreferences as a JSON string
    private static void saveCamerasToSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mCameras); //tasks is an ArrayList instance variable
        prefsEditor.putString(Camera.PrefKey.PREF_KEY_CAMERAS, json);
        prefsEditor.commit();
    }

    //  Retrieve list of node's that are stored in SharedPreferences as a JSON string
    private static List<Node> getNodesFromSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(Node.PrefKey.PREF_KEY_NODES, "");
        mNodes = gson.fromJson(json, new TypeToken<ArrayList<Node>>(){}.getType());
        if (mNodes == null) {
            mNodes = new ArrayList<Node>();
        }
        return mNodes;
    }

    //  Save list of node's that are stored in SharedPreferences as a JSON string
    private static void saveNodesToSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mNodes); //tasks is an ArrayList instance variable
        prefsEditor.putString(Node.PrefKey.PREF_KEY_NODES, json);
        prefsEditor.commit();
    }




    /**
     * The root preference fragment that displays preferences that link to the other preference
     * fragments below.
     */
    private static class SettingsFragment extends PreferenceFragmentCompat {

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

            //  TODO :  Remove once AddCamera functionality is working
            //  Generate test data
//            generateTestData();

            //  Retrieve list of camera's that are stored in SharedPreferences as a JSON string
            getCamerasFromSharedPrefs(getActivity());

            //  Retrieve list of node's that are stored in SharedPreferences as a JSON string
            getNodesFromSharedPrefs(getActivity());

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

            Preference pref = (Preference) findPreference(Camera.PrefKey.PREF_KEY_CAMERAS);
            pref.setSummary(size + " camera\'s configured.");

        }

        //  Update Node's preference summary
        private void updateNodePreferenceSummary(Context context) {
            int size = 0;
            if (mNodes != null) {
                size = mNodes.size();
            }

            Preference pref = (Preference) findPreference(Node.PrefKey.PREF_KEY_NODES);
            pref.setSummary(size + " node\'s configured.");
        }

        //  TODO :  Remove once add device functionality is working
        private void generateTestData() {
//            generateTestDataCameras();
//            generateTestDataNodes();
        }

        //  TODO :  Remove once add device functionality is working
        //  Generate test data
        private void generateTestDataCameras() {
            mCameras = new ArrayList<Camera>();

            Camera c = new Camera();
            c.setName("Front");
            c.setUrl(getResources().getString(R.string.pref_default_camera_url));
            c.setUseAuthentication(false);

            mCameras.add(c);

            c = new Camera();
            c.setName("Rear");
            c.setUrl(getResources().getString(R.string.pref_default_camera_url));
            c.setUseAuthentication(false);
            mCameras.add(c);

            //  Save to SharedPreferences
            saveCamerasToSharedPrefs(getActivity());
        }

        //  TODO :  Remove once add device functionality is working
        //  Generate test data
        private void generateTestDataNodes() {
            mNodes = new ArrayList<Node>();

            Node n = new Node();
            n.setName("RaspberryPi Hub");
            n.setIp(getResources().getString(R.string.pref_default_node_ip));
            n.setSSHPort(getString(R.string.pref_default_node_ssh_port));
            n.setUseAuthentication(false);
            n.setUsePhpSysInfo(true);
            n.setPhpSysInfoUrl(getResources().getString(R.string.pref_default_node_physysinfo_url));
            n.setUseMotionEye(true);
            n.setMotionEyeUrl(getResources().getString(R.string.pref_default_node_motioneye_url));
            mNodes.add(n);

            n = new Node();
            n.setName("RaspberryPi Rear Camera");
            n.setIp(getResources().getString(R.string.pref_default_node_ip));
            n.setSSHPort(getString(R.string.pref_default_node_ssh_port));
            n.setUseAuthentication(false);
            n.setUsePhpSysInfo(true);
            n.setPhpSysInfoUrl(getResources().getString(R.string.pref_default_node_physysinfo_url));
            n.setUseMotionEye(true);
            n.setMotionEyeUrl(getResources().getString(R.string.pref_default_node_motioneye_url));
            mNodes.add(n);

            //  Save to SharedPreferences
            saveNodesToSharedPrefs(getActivity());
        }

    }

    /**
     * The fragment that displays list of configured camera's
     */
    private static class SettingsFragmentCameras extends PreferenceFragmentCompat {

        private static final String TAG = "SettingsFragmentCameras";

        private List<String> mPrefKeyList = new ArrayList<>();

        public SettingsFragmentCameras() {
            // Required empty public constructor
        }

        @Override
        public void onAttach(Context context) {
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
            //  Delete temp pref key's that are used to store the index in the Cammera ArrayList
            deletePrefKeyList(mPrefKeyList);
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
            saveCamerasToSharedPrefs(getActivity());
        }

        //  Dynamically create PreferenceScreen of configured camera's
        private void createPreferences() {

            //  Delete temp pref key's that are used to store the index in the Cammera ArrayList
            deletePrefKeyList(mPrefKeyList);

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
                String prefKey = Camera.PrefKey.PREF_KEY_CAMERA_NUM + i;
                prefCamera.setKey(prefKey);
                prefCamera.setDefaultValue(Integer.toString(i));
                prefCamera.setSummary(mCameras.get(i).getUrl());

                //  Add preferences to PreferenceCategory
                camerasConfiguredCategory.addPreference(prefCamera);

                //  Launch fragment when preference is clicked.
                prefCamera.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        //  Set camera details to SharedPreferences.  This is used as temp storage.
                        int index = getIndex(preference.getKey());
                        Camera camera = mCameras.get(index);

                        Bundle args = new Bundle();
                        args.putString(ARGS_PREF_KEY, preference.getKey());
                        args.putBoolean(ARGS_ADD, false);
                        args.putSerializable(ARGS_CAMERA_DETAIL, camera);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("Camera_Detail");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        DialogFragment settingsFragmentCameraDetail = new SettingsFragmentCameraDetail();
                        settingsFragmentCameraDetail.setArguments(args);
                        settingsFragmentCameraDetail.show(ft, "Camera_Detail");

                        return true;
                    }
                });

                //  Write array index to SharedPreferences
                setIndex(prefKey, i);

                //  Add prefKey to list.  These are temp pref keys and will be deleted when fragment is destroyed.
                mPrefKeyList.add(prefKey);

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
                    args.putString(ARGS_PREF_KEY, ARGS_ADD);    //  Put some value since there is no index
                    args.putBoolean(ARGS_ADD, true);    //  Add device flag
                    args.putSerializable(ARGS_CAMERA_DETAIL, camera);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("Camera_Detail");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    DialogFragment settingsFragmentCameraDetail = new SettingsFragmentCameraDetail();
                    settingsFragmentCameraDetail.setArguments(args);
                    settingsFragmentCameraDetail.show(ft, "Camera_Detail");

                    return true;
                }
            });

            //  Set the root of the preference hierarchy that this fragment is showing.
            setPreferenceScreen(screen);
        }

        //  Get array index that is stored in SharedPreference.
        private int getIndex(String prefKey) {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int index = appSharedPrefs.getInt(prefKey, -1);
            return index;
        }

        //  Set array index that is stored in SharedPreference.
        private void setIndex(String prefKey, int index) {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(prefKey, index);
            prefsEditor.commit();
        }

        //  Delete temp pref key's that are used to store the index in the Cammera ArrayList
        private void deletePrefKeyList(List<String> list) {
            for (int i =0; i < list.size(); i++) {
                String key = list.get(i);
                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                appSharedPrefs.edit().remove(key).clear();
            }
        }

    }

    /**
     * The fragment that displays list of configured Node's
     */
    private static class SettingsFragmentNodes extends PreferenceFragmentCompat {

        private static final String TAG = "SettingsFragmentNodes";

        private List<String> mPrefKeyList = new ArrayList<>();

        public SettingsFragmentNodes() {
            // Required empty public constructor
        }

        @Override
        public void onAttach(Context context) {
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
            //  Delete temp pref key's that are used to store the index in the Cammera ArrayList
            deletePrefKeyList(mPrefKeyList);
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

        // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
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
            saveNodesToSharedPrefs(getActivity());
        }

        //  Dynamically create PreferenceScreen of configured Node's
        private void createPreferences() {

            //  Delete temp pref key's that are used to store the index in the Cammera ArrayList
            deletePrefKeyList(mPrefKeyList);

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
                String prefKey = Node.PrefKey.PREF_KEY_NODE_NUM + i;
                prefNode.setKey(prefKey);
                prefNode.setDefaultValue(Integer.toString(i));
                prefNode.setSummary(mNodes.get(i).getIp());

                //  Add preferences to PreferenceCategory
                nodessConfiguredCategory.addPreference(prefNode);

                //  Launch fragment when preference is clicked.
                prefNode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        //  Set Node details to SharedPreferences.  This is used as temp storage.
                        int index = getIndex(preference.getKey());
                        Node node = mNodes.get(index);

                        Bundle args = new Bundle();
                        args.putString(ARGS_PREF_KEY, preference.getKey());
                        args.putBoolean(ARGS_ADD, false);
                        args.putSerializable(ARGS_NODE_DETAIL, node);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("Node_Detail");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        DialogFragment settingsFragmentNodeDetail = new SettingsFragmentNodeDetail();
                        settingsFragmentNodeDetail.setArguments(args);
                        settingsFragmentNodeDetail.show(ft, "Node_Detail");

                        return true;
                    }
                });

                //  Write array index to SharedPreferences
                setIndex(prefKey, i);

                //  Add prefKey to list.  These are temp pref keys and will be deleted when fragment is destroyed.
                mPrefKeyList.add(prefKey);

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
                    args.putString(ARGS_PREF_KEY, ARGS_ADD);    //  Put some value since there is no index
                    args.putBoolean(ARGS_ADD, true);    //  Add device flag
                    args.putSerializable(ARGS_NODE_DETAIL, node);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("Node_Detail");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    DialogFragment settingsFragmentNodeDetail = new SettingsFragmentNodeDetail();
                    settingsFragmentNodeDetail.setArguments(args);
                    settingsFragmentNodeDetail.show(ft, "Node_Detail");

                    return true;
                }
            });

            //  Set the root of the preference hierarchy that this fragment is showing.
            setPreferenceScreen(screen);
        }

        //  Get array index that is stored in SharedPreference.
        private int getIndex(String prefKey) {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int index = appSharedPrefs.getInt(prefKey, -1);
            return index;
        }

        //  Set array index that is stored in SharedPreference.
        private void setIndex(String prefKey, int index) {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(prefKey, index);
            prefsEditor.commit();
        }

        //  Delete temp pref key's that are used to store the index in the Cammera ArrayList
        private void deletePrefKeyList(List<String> list) {
            for (int i =0; i < list.size(); i++) {
                String key = list.get(i);
                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                appSharedPrefs.edit().remove(key).clear();
            }
        }

    }

}
