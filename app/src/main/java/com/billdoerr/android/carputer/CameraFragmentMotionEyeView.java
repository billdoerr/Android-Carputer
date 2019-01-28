package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.billdoerr.android.carputer.utils.ImageStorage;

public class CameraFragmentMotionEyeView extends Fragment {

    private static final String TAG = "CameraFragmentMotionEyeView";
    private static final String ARG_URI = "MOTIONEYE_URI";

    private WebView mWebView;
    private String mCameraAddress;

    public CameraFragmentMotionEyeView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getArgs();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_motioneye_view, container, false);

        //  Display the motioneye admin console web page
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mCameraAddress);

        //  Not needed anymore since snapshot was implemented with a button click
//        mWebView.setOnTouchListener(new View.OnTouchListener() {
//
//            final static int FINGER_RELEASED = 0;
//            final static int FINGER_TOUCHED = 1;
//            final static int FINGER_DRAGGING = 2;
//            final static int FINGER_UNDEFINED = 3;
//
//            private int fingerState = FINGER_RELEASED;
//
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                switch (motionEvent.getAction()) {
//
//                    case MotionEvent.ACTION_DOWN:
//                        if (fingerState == FINGER_RELEASED) fingerState = FINGER_TOUCHED;
//                        else fingerState = FINGER_UNDEFINED;
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        if(fingerState != FINGER_DRAGGING) {
//                            fingerState = FINGER_RELEASED;
//
//                        //  Archive image
////                        takeSnapshot();
//
//                        }
//                        else if (fingerState == FINGER_DRAGGING) fingerState = FINGER_RELEASED;
//                        else fingerState = FINGER_UNDEFINED;
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//                        if (fingerState == FINGER_TOUCHED || fingerState == FINGER_DRAGGING) fingerState = FINGER_DRAGGING;
//                        else fingerState = FINGER_UNDEFINED;
//                        break;
//
//                    default:
//                        fingerState = FINGER_UNDEFINED;
//
//                }
//
//                return false;
//            }
//        });

        return view;
    }

    //  Setup action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_options_menu_snapshot, menu);
    }

    //  Options menu callback
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_snapshot:
                takeSnapshot();
                return true;
            default:
                return true;
        }
    }

    private void takeSnapshot() {
        // Take screen shot
        Bitmap bitmap = Bitmap.createBitmap(mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mWebView.draw(canvas);

        new ImageStorage().saveImage(getActivity(), bitmap);
        Toast.makeText(getActivity(), getResources().getString(R.string.toast_image_saved), Toast.LENGTH_LONG).show();
    }

    private void getArgs() {
        Bundle bundle = getArguments();
        mCameraAddress = bundle.getString(ARG_URI);
    }

}
