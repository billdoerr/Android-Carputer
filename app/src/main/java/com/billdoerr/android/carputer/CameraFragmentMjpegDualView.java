package com.billdoerr.android.carputer;

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

public class CameraFragmentMjpegDualView extends Fragment {

    private static final String TAG = "MjpegDualView";
    private static final String ARG_CAMERA_ADDRESS_1 = "CAMERA_ADDRESS_1";
    private static final String ARG_CAMERA_ADDRESS_2 = "CAMERA_ADDRESS_2";
    private static final int TIMEOUT = 5;

    private com.github.niqdev.mjpeg.MjpegView mjpegView1;
    private com.github.niqdev.mjpeg.MjpegView mjpegView2;

    private String mCameraAddress1;
    private String mCameraAddress2;

    public CameraFragmentMjpegDualView() {
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
        View view = inflater.inflate(R.layout.fragment_camera_mjpeg_dual_view, container, false);

        mjpegView1 = (com.github.niqdev.mjpeg.MjpegView) view.findViewById(R.id.mjpegViewDefault1);
        mjpegView2 = (com.github.niqdev.mjpeg.MjpegView) view.findViewById(R.id.mjpegViewDefault2);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mjpegView1.stopPlayback();
        mjpegView2.stopPlayback();
    }

    @Override
    public void onResume() {
        super.onResume();
        //  Begin streaming
        loadIpCam1(mCameraAddress1);
        loadIpCam2(mCameraAddress2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mjpegView1 != null) {
            mjpegView1.stopPlayback();
        }
        if (mjpegView2 != null) {
            mjpegView2.stopPlayback();
        }
    }

    //  TODO : Do I need this or can I comment out the code?
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

    private void loadIpCam1(String cameraAddress) {
        Mjpeg.newInstance()
                .open(cameraAddress, TIMEOUT)
                .subscribe(
                        inputStream -> {
                            mjpegView1.setSource(inputStream);
                            mjpegView1.setDisplayMode(DisplayMode.BEST_FIT);
                            mjpegView1.showFps(false);
                        },
                        throwable -> {
                            Log.e(getClass().getSimpleName(), getResources().getString(R.string.toast_camera_connection_error), throwable);
                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_camera_connection_error), Toast.LENGTH_LONG).show();
                        });
    }

    private void loadIpCam2(String cameraAddress) {
        Mjpeg.newInstance()
                .open(cameraAddress, TIMEOUT)
                .subscribe(
                        inputStream -> {
                            mjpegView2.setSource(inputStream);
                            mjpegView2.setDisplayMode(DisplayMode.BEST_FIT);
                            mjpegView2.showFps(false);
                        },
                        throwable -> {
                            Log.e(getClass().getSimpleName(), getResources().getString(R.string.toast_camera_connection_error), throwable);
                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_camera_connection_error), Toast.LENGTH_LONG).show();
                        });
    }

    private void getCameraAddress() {
        Bundle bundle = getArguments();
        mCameraAddress1 = bundle.getString(ARG_CAMERA_ADDRESS_1);
        mCameraAddress2 = bundle.getString(ARG_CAMERA_ADDRESS_2);
    }

}