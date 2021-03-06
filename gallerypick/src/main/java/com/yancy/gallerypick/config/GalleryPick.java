package com.yancy.gallerypick.config;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.yancy.gallerypick.activity.GalleryPickActivity;
import com.yancy.gallerypick.utils.FileUtils;

/**
 * GalleryPick 启动类 (单例)
 * Created by Yancy on 2016/1/27.
 */
public class GalleryPick {

    private GalleryPick(){
        Log.i("wang","GalleryPick is new");
    }

    private final static String TAG = "GalleryPick";

    private volatile static GalleryPick galleryPick;

    private GalleryConfig galleryConfig;

    public static synchronized GalleryPick getInstance() {
        if (galleryPick == null) {
            synchronized (GalleryPick.class) {
                if (galleryPick == null) {
                    Log.i("wang", "GalleryPick");
                    galleryPick = new GalleryPick();
                }
            }
        }
        return galleryPick;
    }


    public void open(Activity mActivity) {
        if (galleryPick.galleryConfig == null) {
            Log.e(TAG, "请配置 GalleryConfig");
            return;
        }
        if (galleryPick.galleryConfig.getImageLoader() == null) {
            Log.e(TAG, "请配置 ImageLoader");
            return;
        }
        if (galleryPick.galleryConfig.getIHandlerCallBack() == null) {
            Log.e(TAG, "请配置 IHandlerCallBack");
            return;
        }

        FileUtils.createFile(galleryPick.galleryConfig.getFilePath());

        Intent intent = new Intent(mActivity, GalleryPickActivity.class);
        mActivity.startActivity(intent);
    }

    public void openCamera(Activity mActivity) {
        if (galleryPick.galleryConfig == null) {
            Log.e(TAG, "请配置 GalleryConfig");
            return;
        }
        if (galleryPick.galleryConfig.getIHandlerCallBack() == null) {
            Log.e(TAG, "请配置 IHandlerCallBack");
            return;
        }

        FileUtils.createFile(galleryPick.galleryConfig.getFilePath());

        Intent intent = new Intent(mActivity, GalleryPickActivity.class);
        intent.putExtra("isOpenCamera", true);
        mActivity.startActivity(intent);
    }


    public GalleryPick setGalleryConfig(GalleryConfig galleryConfig) {
        boolean flag = galleryConfig==null?true:false;
        Log.i("wang","setGalleryConfig->galleryConfig==null:"+flag);
        this.galleryConfig = galleryConfig;
        return this;
    }

    public GalleryConfig getGalleryConfig() {
        return galleryConfig;
    }

}
/*
 *   ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 *     ┃　　　┃
 *     ┃　　　┃
 *     ┃　　　┗━━━┓
 *     ┃　　　　　　　┣┓
 *     ┃　　　　　　　┏┛
 *     ┗┓┓┏━┳┓┏┛
 *       ┃┫┫　┃┫┫
 *       ┗┻┛　┗┻┛
 *        神兽保佑
 *        代码无BUG!
 */