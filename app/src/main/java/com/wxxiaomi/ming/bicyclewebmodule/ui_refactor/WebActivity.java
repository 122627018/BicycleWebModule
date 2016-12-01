package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.leakcanary.RefWatcher;
import com.wxxiaomi.ming.bicyclewebmodule.MyApplication;
import com.wxxiaomi.ming.bicyclewebmodule.R;
import com.wxxiaomi.ming.bicyclewebmodule.ui_refactor.builder.MyBuilderImpl;

public class WebActivity extends AppCompatActivity {

    WebDirector director;
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        content = (LinearLayout) findViewById(R.id.content);
        director = new WebDirector(new MyBuilderImpl(this));
//        director = new WebDirector(new FragBuilder(this));
        ViewGroup wview = director.getWview();
        content.addView(wview);
        director.openInit();
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
