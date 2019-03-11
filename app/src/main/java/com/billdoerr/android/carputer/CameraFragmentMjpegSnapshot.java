package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.billdoerr.android.carputer.settings.Camera;
import com.billdoerr.android.carputer.utils.ImageStorage;
import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.OnFrameCapturedListener;

/**
 *
 */
public class CameraFragmentMjpegSnapshot extends Fragment implements OnFrameCapturedListener {

    private static final String TAG = "MjpegSnapshot";

    private static final String ARGS_CAMERA_DETAIL = "ARGS_CAMERA_DETAIL";
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
        setHasOptionsMenu(true);
        mCameraAddress = getCameraAddress();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_mjpeg_snapshot, container, false);

        mjpegView = (com.github.niqdev.mjpeg.MjpegView) view.findViewById(R.id.video_view);

        mjpegView.setOnFrameCapturedListener(this);

        mImageView = (ImageView) view.findViewById(R.id.image_view);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeSnapshot();
            }
        });

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
     * Connect to Ip Camera
     * @param cameraAddress
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
                            Log.e(getClass().getSimpleName(), "mjpeg error", throwable);
                            //  TODO:  Why is the Toast throwing errors now?
//                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_camera_connection_error), Toast.LENGTH_LONG).show();
                        });
    }

    /**
     * Grab image of current frame
     */
    private void takeSnapshot() {
        Log.d(TAG, "Image clicked.");
        if (mLastPreview != null) {
            Log.d(TAG, "Image captured.");
            mImageView.setImageBitmap(mLastPreview);
            try {
                new ImageStorage().saveImage(getActivity(), mLastPreview);
            } catch (ImageStorage.FreeSpaceException e) {
                //  Handle exception
                Log.i(TAG, e.getMessage() );
            }

            Toast.makeText(getActivity(), getResources().getString(R.string.toast_image_saved), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Image saved!");
        }
    }

    /**
     * Get camera address from fragment arguments
     * @return
     */
    private String getCameraAddress() {
        Bundle args = getArguments();
        Camera camera = (Camera) args.getSerializable(ARGS_CAMERA_DETAIL);
        return camera.getUrl();
    }

}