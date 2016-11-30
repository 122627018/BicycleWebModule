package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.wxxiaomi.ming.bicyclewebmodule.service.WebMethods;
import com.wxxiaomi.ming.bicyclewebmodule.util.ParsMakeUtil;

import java.util.Map;

import rx.Observable;
import rx.Observer;

/**
 * Created by Administrator on 2016/11/30.
 */

public class MyBuilderImpl implements WebBuilder {
    private Context context;

    public MyBuilderImpl(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }


    @Override
    public void doGet(String data, final CallBackFunction function) {
        doGet(data)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        function.onCallBack(s);
                    }
                });
    }

    @Override
    public void doPost(String data, final CallBackFunction function) {
        doPost(data)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        function.onCallBack(s);
                    }
                });
    }

    @Override
    public String getTargetUrl() {
        return ((AppCompatActivity)context).getIntent().getStringExtra("url");
    }

    @Override
    public void doJsInitData(String data) {

    }

    @Override
    public void doForwardEvent(String data, CallBackFunction function) {

    }

    @Override
    public void doDialogEvent(String data) {

    }

    @Override
    public void doFinishEvent(String data) {

    }

    @Override
    public WebResourceResponse doInterceptRequest(WebView view, String url) {
        return null;
    }

    @Override
    public String getComeData() {
        return null;
    }

    @Override
    public void showLog(String data) {

    }

    /**
     * 解析url
     *
     * @param data js
     * @return s
     */
    private Observable<String> doGet(String data) {
        Map<String, String> pars = ParsMakeUtil.string2Map(data);
        String url = pars.get("url");
        pars.remove("url");
        return WebMethods.getInstance().sendget(url, pars);
    }

    /**
     * 向服务器post一段json数据
     */
    private Observable<String> doPost(String data) {
        Map<String, String> pars = ParsMakeUtil.string2Map(data);
        String url = pars.get("url");
        pars.remove("url");
        return WebMethods.getInstance().sendPost(url, pars);
    }
}
