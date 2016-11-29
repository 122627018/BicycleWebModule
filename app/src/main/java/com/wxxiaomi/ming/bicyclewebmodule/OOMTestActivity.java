package com.wxxiaomi.ming.bicyclewebmodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class OOMTestActivity extends AppCompatActivity {

    LinearLayout content;
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oom_test);
        content = (LinearLayout) findViewById(R.id.content);
        mWebView = new WebView(getApplicationContext());
        content.addView(mWebView);
        mWebView.loadUrl("http://www.youku.com");
    }

    @Override
    protected void onDestroy() {
//        Log.i("wang","onDestroy()");
        releaseWebViews();
        super.onDestroy();
    }

    public synchronized void releaseWebViews() {
        if(mWebView != null) {
            try {
                if(mWebView.getParent() != null) {
                    ((ViewGroup) mWebView.getParent()).removeView(mWebView);
                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//this is causing the segfault occasionally below 4.2
                mWebView.destroy();
                //                }
            }catch (IllegalArgumentException e) {
//                DLog.p(e);
            }
//            RefWatcher refWatcher = FApplication.getRefWatcher();
//            refWatcher.watch(webView);
            mWebView = null;
        }
    }
}
