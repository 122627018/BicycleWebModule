package com.wxxiaomi.ming.bicyclewebmodule;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.wxxiaomi.ming.bicyclewebmodule.support.cache.DiskCache;
import com.wxxiaomi.ming.bicyclewebmodule.support.aliyun.OssEngine;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by 12262 on 2016/11/24.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DiskCache.getInstance().open(getApplicationContext());
        OssEngine.getInstance().initOssEngine(getApplicationContext());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        enabledStrictMode();
        sRefWatcher = LeakCanary.install(this);
        // Normal app init code...
        //开启DiskLruCache缓存


    }
    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());
        }
    }



    public  static RefWatcher sRefWatcher;
}
