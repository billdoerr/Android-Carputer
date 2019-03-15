package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;

//  TODO:  Ok to remove from application?
/**
 * Child fragment that displays Mjpeg streaming video.
 * Also provides the ability to save the current frame to storage (snapshot).
 * Created by the CameraFragmentMjpeg fragment.
 * Currently not used by application.  Superseded by the use of CameraFragmentMjpegSnapshot.
 */
public class CameraFragmentMjpegView extends Fragment {

    private static final String TAG = "CameraFragmentMjpegView";
    private static final String ARG_CAMERA_ADDRESS = "CAMERA_ADDRESS";
    private static final int TIMEOUT = 5;

    private com.github.niqdev.mjpeg.MjpegView mjpegView;

    private String mCameraAddress;

    public CameraFragmentMjpegView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCameraAddress();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_mjpeg_view, container, false);
        mjpegView = (com.github.niqdev.mjpeg.MjpegView) view.findViewById(R.id.video_view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //  Begin streaming
        loadIpCam(mCameraAddress);
    }

    @Override
    public void onPause() {
        super.onPause();
            }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mjpegView != null) {
            mjpegView.stopPlayback();
        }
    }

    /**
     * MjpegViewHandler
     */
    @SuppressLint("HandlerLeak")
    final Handler MjpegViewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.d("State : ", msg.obj.toString());

            switch (msg.obj.toString()){
                case "DISCONNECTED" :
                    Log.d(TAG, "DISCONNECTED");
                    break;
                case "CONNECTION_PROGRESS" :
                    Log.d(TAG, "CONNECTION_PROGRESS");
                    break;
                case "CONNECTED" :
                    Log.d(TAG, "CONNECTED");
                    break;
                case "CONNECTION_ERROR" :
                    Log.d(TAG, "CONNECTION_ERROR");
                    break;
                case "STOPPING_PROGRESS" :
                    Log.d(TAG, "STOPPING_PROGRESS");
                    break;
            }

        }
    };

    /**
     * Connect to Ip Camera.
     * @param cameraAddress String: Camera Url.
     */
    private void loadIpCam(String cameraAddress) {
        Mjpeg.newInstance()
                .open(cameraAddress, TIMEOUT)
                .subscribe(
                        inputStream -> {
                            mjpegView.setSource(inputStream);
                            mjpegView.setDisplayMode(DisplayMode.BEST_FIT);
                            mjpegView.showFps(false);
                        },
                        throwable -> {
                            Log.e(getClass().getSimpleName(), getResources().getString(R.string.toast_camera_connection_error), throwable);
                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_camera_connection_error), Toast.LENGTH_LONG).show();
                        });
    }


    /**
     * Grab image of current frame.
     */
    private void getCameraAddress() {
        Bundle bundle = getArguments();
        mCameraAddress = bundle.getString(ARG_CAMERA_ADDRESS);
    }

}