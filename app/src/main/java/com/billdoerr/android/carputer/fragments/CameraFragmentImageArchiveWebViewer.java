package com.billdoerr.android.carputer.fragments;

import android.annotation.SuppressLint;
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

import com.billdoerr.android.carputer.R;

import java.util.Objects;


/**
 *  Child fragment of CameraFragmentImageArchive.
 *  Displays the phpSysInfo web page if enabled in shared preferences.
 *  pypSysInfo must be installed on the node.
 */
public class CameraFragmentImageArchiveWebViewer extends Fragment {

    private static final String ARGS_IMAGE_ARCHIVE_URL = "ARGS_IMAGE_ARCHIVE_URL";

    private WebView mWebView;

    private String mImageArchiveUrl;

    public CameraFragmentImageArchiveWebViewer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //  Enable options menu
        setHasOptionsMenu(true);

        //  Read args from bundle
        getArgs();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        //  Display the phpSysInfo admin console web page
        mWebView = view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mImageArchiveUrl);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.web_view_go_back, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //  Reload system log
        if (item.getItemId() == R.id.action_go_back) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
            return true;
        }

        return false;
    }

    /**
     * Get fragment arguments.
     */
    private void getArgs() {
        Bundle args = getArguments();
        mImageArchiveUrl = Objects.requireNonNull(args).getString(ARGS_IMAGE_ARCHIVE_URL);
    }

}

