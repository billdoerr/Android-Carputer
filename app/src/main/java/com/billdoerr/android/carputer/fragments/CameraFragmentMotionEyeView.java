package com.billdoerr.android.carputer.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.billdoerr.android.carputer.utils.GlobalVariables;
import com.billdoerr.android.carputer.R;
import com.billdoerr.android.carputer.settings.Node;
import com.billdoerr.android.carputer.utils.FileStorageUtils;

import java.util.Objects;

/**
 *  Child fragment of CameraFragmentMotionEye.
 *  Displays the motionEye web page if enabled in shared preferences.
 *  motionEye must be installed on the node.
 */
public class CameraFragmentMotionEyeView extends Fragment {

    private static final String TAG = "CameraFragMotionEyeView";
    private static final String ARGS_NODE_DETAIL = "ARGS_NODE_DETAIL";

    private WebView mWebView;
    private String mMotionEyeUrl;

    public CameraFragmentMotionEyeView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getArgs();

        // Calling Application class (see application tag in AndroidManifest.xml)
//        mGlobalVariables = (GlobalVariables) Objects.requireNonNull(getActivity()).getApplicationContext();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_motioneye_view, container, false);

        //  Display the motioneye admin console web page
        mWebView = view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mMotionEyeUrl);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_options_menu_snapshot, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.action_snapshot) {
            takeSnapshot();
            return true;
        }
        return true;
    }

    //  Grab screenshot of current frame
    private void takeSnapshot() {
        // Take screen shot
        Bitmap bitmap = Bitmap.createBitmap(mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mWebView.draw(canvas);
        try {
            FileStorageUtils.saveImage(Objects.requireNonNull(getActivity()), bitmap);
            writeSystemLog(FileStorageUtils.TABS + getString(R.string.toast_image_saved));
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_image_saved), Toast.LENGTH_LONG).show();
        } catch (FileStorageUtils.FreeSpaceException e) {
            //  Handle exception
            Log.i(TAG, e.getMessage() );
            writeSystemLog(e.getMessage());
        }

    }

    /**
     * Get fragment arguments.
     */
    private void getArgs() {
        Bundle args = getArguments();
        Node node = (Node) Objects.requireNonNull(args).getSerializable(ARGS_NODE_DETAIL);
        mMotionEyeUrl = Objects.requireNonNull(node).getMotionEyeUrl();
    }

    //  Output to system log
    private void writeSystemLog(String msg) {
        FileStorageUtils.writeSystemLog(getActivity(), GlobalVariables.SYS_LOG, TAG + FileStorageUtils.TABS + msg + FileStorageUtils.LINE_SEPARATOR);
    }

}
