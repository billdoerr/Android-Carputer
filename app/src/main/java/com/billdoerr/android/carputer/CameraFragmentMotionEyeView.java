package com.billdoerr.android.carputer;

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

import com.billdoerr.android.carputer.settings.Node;
import com.billdoerr.android.carputer.utils.ImageStorage;

/**
 *
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
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_motioneye_view, container, false);

        //  Display the motioneye admin console web page
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mMotionEyeUrl);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_options_menu_snapshot, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.action_snapshot:
                takeSnapshot();
                return true;
            default:
                return true;
        }
    }

    //  Grab screenshot of current frame
    private void takeSnapshot() {
        // Take screen shot
        Bitmap bitmap = Bitmap.createBitmap(mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mWebView.draw(canvas);
        try {
            new ImageStorage().saveImage(getActivity(), bitmap);
        } catch (ImageStorage.FreeSpaceException e) {
            //  Handle exception
            Log.i(TAG, e.getMessage() );
        }

        Toast.makeText(getActivity(), getResources().getString(R.string.toast_image_saved), Toast.LENGTH_LONG).show();
    }

    /**
     * Get fragment arguments
     */
    private void getArgs() {
        Bundle args = getArguments();
        Node node = (Node) args.getSerializable(ARGS_NODE_DETAIL);
        mMotionEyeUrl = node.getMotionEyeUrl();
    }

}
