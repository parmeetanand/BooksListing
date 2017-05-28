package com.example.anandparmeetsingh.books;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by ParmeetSingh on 5/28/2017.
 */

public class WebView extends AppCompatActivity {
    android.webkit.WebView mBrowser;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        Intent in = getIntent();
        String webUrl = in.getExtras().getString("locations");

        mBrowser = (android.webkit.WebView) findViewById(R.id.webView);
        mBrowser.setWebViewClient(new MyBrowser());

        mBrowser.getSettings().setJavaScriptEnabled(true);
        mBrowser.loadUrl(webUrl);

        final Activity activity = this;
        mBrowser.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(android.webkit.WebView view, int progress) {
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
                setTitle("Loading...");
                if (progress == 100) {
                    frameLayout.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                }
                super.onProgressChanged(view, progress);
            }

        });
//        mBrowser.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                // Activities and WebViews measure progress with different scales.
//                // The progress meter will automatically disappear when we reach 100%
//                activity.setProgress(progress * 1000);
//            }
//        });
        mBrowser.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setProgress(0);
    }

    @Override
    public void onBackPressed() {
        if (mBrowser.canGoBack())
            mBrowser.goBack();
        else
            super.onBackPressed();

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            view.loadUrl("javascript: $('.main-container').css('padding-Top','40px')");
            view.loadUrl(url);
            frameLayout.setVisibility(View.VISIBLE);

            return true;
        }
    }
}
