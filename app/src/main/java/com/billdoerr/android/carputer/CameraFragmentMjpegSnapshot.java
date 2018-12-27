package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.billdoerr.android.carputer.utils.ImageStorage;
import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.OnFrameCapturedListener;

public class CameraFragmentMjpegSnapshot extends Fragment implements OnFrameCapturedListener {

    private static final String TAG = "CameraFragmentMjpegSnapshot";
    private static final String ARG_CAMERA_ADDRESS = "CAMERA_ADDRESS";
    private static final int TIMEOUT = 5;

    private com.github.niqdev.mjpeg.MjpegView mjpegView;
    private ImageView mImageView;
    private Bitmap mLastPreview = null;
    private String mCameraAddress;

    public CameraFragmentMjpegSnapshot() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCameraAddress();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_snapshot, container, false);

        mjpegView = (com.github.niqdev.mjpeg.MjpegView) view.findViewById(R.id.video_view);

        mjpegView.setOnFrameCapturedListener(this);

        mImageView = (ImageView) view.findViewById(R.id.image_view);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Image clicked.");
                if (mLastPreview != null) {
                    Log.d(TAG, "Image captured.");

                    mImageView.setImageBitmap(mLastPreview);

                    new ImageStorage().saveImage(getActivity(), mLastPreview);
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_image_saved), Toast.LENGTH_LONG).show();

                    Log.d(TAG, "Image saved!");

                }
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mjpegView.stopPlayback();
    }

    @Override
    public void onResume() {
        super.onResume();
        //  Begin streaming
        loadIpCam(mCameraAddress);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mjpegView != null) {
            mjpegView.stopPlayback();
        }
    }

    @Override
    public void onFrameCaptured(Bitmap bitmap) {
        mLastPreview = bitmap;
    }

    //  TODO :  Do I need this or should I comment out the code?
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
                            Log.e(getClass().getSimpleName(), "mjpeg error", throwable);
                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_camera_connection_error), Toast.LENGTH_LONG).show();
                        });
    }

    private void getCameraAddress() {
        Bundle bundle = getArguments();
        mCameraAddress = bundle.getString(ARG_CAMERA_ADDRESS);
    }

}