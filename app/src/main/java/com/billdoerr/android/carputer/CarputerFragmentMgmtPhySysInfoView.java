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

import com.billdoerr.android.carputer.settings.Node;

public class CarputerFragmentMgmtPhySysInfoView extends Fragment {

    private static final String TAG = "phySysInfoView";
    private static final String ARGS_NODE_DETAIL = "ARGS_NODE_DETAIL";

    private String mPhpSysInfoUrl;

    public CarputerFragmentMgmtPhySysInfoView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgs();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carputer_mgmt_phpsysinfo_view, container, false);

        //  Display the phpSysInfo admin console web page
        WebView webView = (WebView) view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(mPhpSysInfoUrl);

        return view;
    }

    private void getArgs() {
        Bundle args = getArguments();
        Node node = (Node) args.getSerializable(ARGS_NODE_DETAIL);
        mPhpSysInfoUrl = node.getPhpSysInfoUrl();
    }

}

