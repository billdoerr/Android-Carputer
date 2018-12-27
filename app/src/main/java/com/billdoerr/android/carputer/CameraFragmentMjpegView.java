package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;

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

    //  TODO :  Is this needed or can I comment out the code
    @SuppressLint("HandlerLeak")
    final Handler MjpegViewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.d("State : ", msg.obj.toString());

            switch (msg.obj.toString()){
                case "DISCONNECTED" :
                    // TODO : When video stream disconnected
                    Log.d(TAG, "DISCONNECTED");
                    break;
                case "CONNECTION_PROGRESS" :
                    // TODO : When connection progress
                    Log.d(TAG, "CONNECTION_PROGRESS");
                    break;
                case "CONNECTED" :
                    // TODO : When video streaming connected
                    Log.d(TAG, "CONNECTED");
                    break;
                case "CONNECTION_ERROR" :
                    // TODO : When connection error
                    Log.d(TAG, "CONNECTION_ERROR");
                    break;
                case "STOPPING_PROGRESS" :
                    // TODO : When MjpegViewer is in stopping progress
                    Log.d(TAG, "STOPPING_PROGRESS");
                    break;
            }

        }
    };

    private void loadIpCam(String cameraAddress) {
        Mjpeg.newInstance()
                .open(cameraAddress, TIMEOUT)
                .subscribe(
                        inputStream -> {
                            mjpegView.setSource(inputStream);
                            mjpegView.setDisplayMode(DisplayMode.STANDARD);
                            mjpegView.showFps(false);
                        },
                        throwable -> {
                            Log.e(getClass().getSimpleName(), getResources().getString(R.string.toast_camera_connection_error), throwable);
                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_camera_connection_error), Toast.LENGTH_LONG).show();
                        });
    }


    private void getCameraAddress() {
        Bundle bundle = getArguments();
        mCameraAddress = bundle.getString(ARG_CAMERA_ADDRESS);
    }

}