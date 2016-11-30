package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.squareup.leakcanary.RefWatcher;
import com.wxxiaomi.ming.bicyclewebmodule.MyApplication;
import com.wxxiaomi.ming.bicyclewebmodule.R;

public class WebActivity extends AppCompatActivity {

    WebViewDirector director;
//    private RelativeLayout activity_web;
    private LinearLayout content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        content = (LinearLayout) findViewById(R.id.content);
        director = new WebViewDirector(new MyBuilderImpl(this));
        BridgeWebView wview = director.getWview();

        content.addView(wview);
       // wview.loadUrl("http://www.baidu.com");
        director.openInit();
//        Demo.demo(this);
    }

    @Override
    protected void onDestroy() {
        //director = null;
        super.onDestroy();
//        Log.i("wang","onDestroy");
        RefWatcher refWatcher = MyApplication.sRefWatcher;
        refWatcher.watch(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
