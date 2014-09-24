package com.ddd.demo.flickrpicdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ddd.demo.flickrpicdemo.R;

/**
 * Created by administrator on 14-9-24.
 */
public class PhotoPageFragment extends Fragment {
    private String mUrl;
    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mUrl = getActivity().getIntent().getData().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_page, container, false);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setMax(100);
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }else {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
        mWebView.loadUrl(mUrl);
        return view;
    }
}
