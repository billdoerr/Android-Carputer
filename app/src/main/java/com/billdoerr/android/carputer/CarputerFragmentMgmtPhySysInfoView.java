package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CarputerFragmentMgmtPhySysInfoView extends Fragment {

    private static final String TAG = "phySysInfoView";
    private static final String PREF_RASPBERRYPI_PHPSYSINFO_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_PHPSYSINFO_URL";

    private WebView mWebView;
    private String mPhpSysInfoUrl;

    public CarputerFragmentMgmtPhySysInfoView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgs();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carputer_mgmt_phpsysinfo_view, container, false);

        //  Display the phpSysInfo admin console web page
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mPhpSysInfoUrl);

        return view;
    }

    private void getArgs() {
        Bundle bundle = getArguments();
        mPhpSysInfoUrl = bundle.getString(PREF_RASPBERRYPI_PHPSYSINFO_URL);
    }

}

