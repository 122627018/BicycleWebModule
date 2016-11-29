package com.wxxiaomi.ming.bicyclewebmodule;

import android.app.Application;

import com.wxxiaomi.ming.bicyclewebmodule.support.cache.DiskCache;
import com.wxxiaomi.ming.bicyclewebmodule.support.aliyun.OssEngine;

/**
 * Created by 12262 on 2016/11/24.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //开启DiskLruCache缓存
        DiskCache.getInstance().open(getApplicationContext());
        OssEngine.getInstance().initOssEngine(getApplicationContext());
    }
}
