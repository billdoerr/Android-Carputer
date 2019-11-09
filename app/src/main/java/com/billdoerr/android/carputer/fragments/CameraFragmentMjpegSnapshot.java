package com.billdoerr.android.carputer.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
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

import com.billdoerr.android.carputer.utils.GlobalVariables;
import com.billdoerr.android.carputer.R;
import com.billdoerr.android.carputer.settings.Camera;
import com.billdoerr.android.carputer.utils.FileStorageUtils;
import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.OnFrameCapturedListener;

import java.util.Objects;

/**
 * Child fragment that displays Mjpeg streaming video.
 * Also provides the ability to save the current frame to storage (snapshot).
 * Created by the CameraFragmentMjpeg fragment.
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

        // Calling Application class (see application tag in AndroidManifest.xml)
//        mGlobalVariables = (GlobalVariables) Objects.requireNonNull(getActivity()).getApplicationContext();

        mCameraAddress = getCameraAddress();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_mjpeg_snapshot, container, false);

        mjpegView = view.findViewById(R.id.video_view);

        mjpegView.setOnFrameCapturedListener(this);

        mImageView = view.findViewById(R.id.image_view);
        mImageView.setOnClickListener(view1 -> takeSnapshot());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
    @SuppressWarnings("unused")
    @SuppressLint("HandlerLeak")
    final Handler MjpegViewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.d("State : ", msg.obj.toString());

            switch (msg.obj.toString()){
                case "DISCONNECTED" :
                    Log.d(TAG, getString(R.string.msg_camera_disconnected));
                    writeSystemLog(FileStorageUtils.TABS + getString(R.string.msg_camera_disconnected));
                    break;
                case "CONNECTION_PROGRESS" :
                    Log.d(TAG, getString(R.string.msg_camera_connection_progress));
                    writeSystemLog(FileStorageUtils.TABS + getString(R.string.msg_camera_connection_progress));
                    break;
                case "CONNECTED" :
                    Log.d(TAG, getString(R.string.msg_camera_connected));
                    writeSystemLog(FileStorageUtils.TABS + getString(R.string.msg_camera_connected));
                    break;
                case "CONNECTION_ERROR" :
                    Log.d(TAG, getString(R.string.msg_camera_connection_error));
                    writeSystemLog(FileStorageUtils.TABS + getString(R.string.msg_camera_connection_error));
                    break;
                case "STOPPING_PROGRESS" :
                    Log.d(TAG, getString(R.string.msg_camera_stopping_progress));
                    writeSystemLog(FileStorageUtils.TABS + getString(R.string.msg_camera_stopping_progress));
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
                            Log.e(getClass().getSimpleName(), getString(R.string.toast_camera_connection_error), throwable);
                            writeSystemLog(FileStorageUtils.TABS + getString(R.string.toast_camera_connection_error));
                            Toast.makeText(getActivity(), getString(R.string.toast_camera_connection_error), Toast.LENGTH_LONG).show();
                        });
    }

    /**
     * Grab image of current frame (snapshot).
     */
    private void takeSnapshot() {
        if (mLastPreview != null) {
            mImageView.setImageBitmap(mLastPreview);
            try {
                FileStorageUtils.saveImage(Objects.requireNonNull(getActivity()), mLastPreview);
                //  Output to system log
                Log.d(TAG, getString(R.string.toast_image_saved));
                writeSystemLog(FileStorageUtils.TABS + getString(R.string.toast_image_saved));
                Toast.makeText(getActivity(), getString(R.string.toast_image_saved), Toast.LENGTH_LONG).show();
            } catch (FileStorageUtils.FreeSpaceException e) {
                //  Handle exception
                //  Output to system log
                Log.e(TAG, e.getMessage() );
                writeSystemLog(e.getMessage());
            }
        }
    }

    /**
     * Get camera address from fragment arguments.
     * @return String: Camera url.
     */
    private String getCameraAddress() {
        Bundle args = getArguments();
        Camera camera = (Camera) Objects.requireNonNull(args).getSerializable(ARGS_CAMERA_DETAIL);
        return Objects.requireNonNull(camera).getUrl();
    }

    //  Output to system log
    private void writeSystemLog(String msg) {
        FileStorageUtils.writeSystemLog(getActivity(), GlobalVariables.SYS_LOG, TAG + FileStorageUtils.TABS + msg + FileStorageUtils.LINE_SEPARATOR);
    }

}